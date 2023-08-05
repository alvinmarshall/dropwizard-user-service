package org.cheise_proj.pubsub.jms;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JmsThreadLocalContextLookup {
    private static final String METRICS_PREFIX = "";
    private static final String METRICS_SENDER_THREAD_LOCAL = METRICS_PREFIX + "threadLocal.";
    private final Connection connection;
    private final MetricRegistry metricRegistry;

    public JmsThreadLocalContextLookup(Connection connection, MetricRegistry metricRegistry) {
        this.connection = connection;
        this.metricRegistry = metricRegistry;
    }

    public JmsProducerContext producer(String queueName) {
        Assert.notEmpty(Collections.singletonList(queueName.toCharArray()), "queueName cannot be blank");
        Assert.notNull(queueName, "queueName cannot be null");
        final String queue = METRICS_SENDER_THREAD_LOCAL + queueName;
        try (Timer.Context ignored = metricRegistry.timer(queue).time()) {
            try {
                return JmsProducerContext.getOrCreate(connection, queueName);
            } catch (JMSException e) {
                metricRegistry.meter(queue + ".exceptions").mark();
                throw new JmsContextLookupException(e, queue);
            }
        }
    }

    public static class JmsProducerContext {
        private static final Logger LOG = LoggerFactory.getLogger(JmsProducerContext.class);
        private static final ThreadLocal<Map<String, JmsProducerContext>> JMS_PRODUCER_OBJECTS_DELEGATE =
                ThreadLocal.withInitial(HashMap::new);
        private final Session session;
        private final Queue queue;
        private final MessageProducer producer;

        public JmsProducerContext(Connection connection, String queueName) throws JMSException {
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            queue = session.createQueue(queueName);
            producer = session.createProducer(queue);
        }


        static JmsProducerContext getOrCreate(Connection connection, String queueName) throws JMSException {
            if (!JMS_PRODUCER_OBJECTS_DELEGATE.get().containsKey(queueName)) {
                LOG.info("creating threadLocal instance with queue {} for thread {}", queueName, Thread.currentThread());
                JMS_PRODUCER_OBJECTS_DELEGATE.get().put(queueName, new JmsProducerContext(connection, queueName));
            }
            return JMS_PRODUCER_OBJECTS_DELEGATE.get().get(queueName);
        }


        public Session getSession() {
            return session;
        }

        public Queue getQueue() {
            return queue;
        }

        public MessageProducer getProducer() {
            return producer;
        }

        public void unload() {
            JMS_PRODUCER_OBJECTS_DELEGATE.remove();
        }
    }
}
