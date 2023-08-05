package org.cheise_proj.pubsub;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cheise_proj.users.UserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.function.Consumer;

public class UserCreationConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(UserCreationConsumer.class);
    private final MetricRegistry metricRegistry;
    private final ObjectMapper mapper;
    private final MessageConsumer messageConsumer;

    public UserCreationConsumer(MetricRegistry metricRegistry, ObjectMapper mapper, MessageConsumer messageConsumer) {
        this.metricRegistry = metricRegistry;
        this.mapper = mapper;
        this.messageConsumer = messageConsumer;
    }

    public void consume(Consumer<UserCreatedEvent> handler) {
        try {
            messageConsumer.setMessageListener(message -> {
                LOG.trace("Received message, {}.", message);
                handler.accept(translate((TextMessage) message, new TypeReference<UserCreatedEvent>() {
                }));
                try {
                    message.acknowledge();
                } catch (JMSException e) {
                    metricRegistry.meter("UserCreationConsumer.acknowledge.failed").mark();
                    throw new ArtemisClientException(e, "UserCreationConsumer.consume.acknowledge");
                }
            });
        } catch (JMSException e) {
            metricRegistry.meter("UserCreationConsumer.consume.failed").mark();
            throw new ArtemisClientException(e, "UserCreationConsumer.consume");
        }
    }

    private <T> T translate(TextMessage message, TypeReference<T> targetType) {
        try {
            return mapper.readValue(message.getText(), targetType);
        } catch (IOException | JMSException e) {
            metricRegistry.meter("UserCreationConsumer.translate.failed").mark();
            throw new ArtemisClientException(e, "UserCreationConsumer.translate");
        }
    }

}
