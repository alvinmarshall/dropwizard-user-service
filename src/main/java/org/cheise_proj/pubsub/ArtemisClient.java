package org.cheise_proj.pubsub;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.lifecycle.Managed;
import org.cheise_proj.pubsub.jms.JmsThreadLocalContextLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

/**
 * The {@code ArtemisClient} class provides a high-level interface for interacting with
 * Apache Artemis messaging broker, managing JMS resources and message production.
 *
 * <p>This class supports message creation and consumption, allowing messages to be
 * produced to and consumed from specific queues. It also provides start and stop
 * methods for managing the connection lifecycle.
 *
 * <p>Usage Example:
 * <pre>{@code
 * try {
 *      ArtemisClient artemisClient = new ArtemisClient(connection, metricRegistry, mapper)
 *     // Produce and consume messages using artemisClient
 * } catch (JMSException e) {
 *     // Handle exceptions
 * }
 * }</pre>
 */
public class ArtemisClient implements Managed {
    private static final Logger LOG = LoggerFactory.getLogger(ArtemisClient.class);
    private final Connection connection;
    private final Session autoAckSession;
    private final Session clientAckSession;
    private final MessageConsumer creationConsumer;
    private final MessageConsumer publishConsumer;
    private final ObjectMapper mapper;
    private final Meter amqMsgCreationError;
    private final JmsThreadLocalContextLookup jmsThreadLocalContextLookup;

    public static final String USER_CREATION_QUEUE_NAME = "userCreationQueue";
    static final String USER_PUBLISH_QUEUE_NAME = "userPublishQueue";
    static final String MSG_CREATION_ERROR = "message.creation.error";

    /**
     * Constructs an {@code ArtemisClient} instance using the provided resources.
     *
     * @param connection     The JMS connection to the Artemis broker.
     * @param metricRegistry The MetricRegistry for managing metrics.
     * @param mapper         The ObjectMapper for JSON serialization/deserialization.
     */
    public ArtemisClient(Connection connection, MetricRegistry metricRegistry, ObjectMapper mapper) throws JMSException {
        this(connection, new JmsThreadLocalContextLookup(connection, metricRegistry), metricRegistry, mapper);
    }

    protected ArtemisClient(
            Connection connection,
            JmsThreadLocalContextLookup lookup,
            MetricRegistry metricRegistry,
            ObjectMapper mapper
    ) throws JMSException {
        this.connection = connection;
        this.mapper = mapper;
        this.jmsThreadLocalContextLookup = lookup;
        this.autoAckSession = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.clientAckSession = this.connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue creationQueue = clientAckSession.createQueue(USER_CREATION_QUEUE_NAME);
        this.creationConsumer = clientAckSession.createConsumer(creationQueue);
        Queue publishQueue = clientAckSession.createQueue(USER_PUBLISH_QUEUE_NAME);
        this.publishConsumer = autoAckSession.createConsumer(publishQueue);
        this.amqMsgCreationError = metricRegistry.meter(MSG_CREATION_ERROR);
    }

    /**
     * Produces a message of type {@code T} to the specified queue.
     *
     * @param message   The message to be produced.
     * @param queueName The name of the queue to produce the message to.
     * @param <T>       The type of the message.
     * @throws ArtemisClientException If there's an error producing the message.
     */
    public <T> void produceMessage(T message, String queueName) {
        LOG.debug("Producing message for  {}", message);
        JmsThreadLocalContextLookup.JmsProducerContext context = jmsThreadLocalContextLookup.producer(queueName);
        try {
            context.getProducer().send(context.getSession().createTextMessage(mapper.writeValueAsString(message)));
        } catch (JMSException | JsonProcessingException e) {
            amqMsgCreationError.mark();
            throw new ArtemisClientException(e, "ArtemisClient.produceMessage");
        }
    }

    public MessageConsumer getCreationConsumer() {
        return creationConsumer;
    }

    public MessageConsumer getPublishConsumer() {
        return publishConsumer;
    }

    /**
     * Starts the connection to the Artemis broker.
     *
     * @throws Exception If there's an error starting the connection.
     */
    @Override
    public void start() throws Exception {
        connection.start();
    }

    /**
     * Stops the connection to the Artemis broker.
     *
     * @throws Exception If there's an error stopping the connection.
     */
    @Override
    public void stop() throws Exception {
        connection.stop();
    }

}
