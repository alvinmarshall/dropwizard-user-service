package org.cheise_proj.pubsub;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

public class ArtemisConfig {
    private final String brokers;
    private final String failOverArgs;

    @JsonCreator
    public ArtemisConfig(
            @JsonProperty("brokers") String brokers,
            @JsonProperty("failOverArgs") String failOverArgs
    ) {
        this.brokers = brokers;
        this.failOverArgs = failOverArgs;
    }

    Connection getConnection() throws JMSException {
        final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(getBrokerConnUrl());//NOSONAR
        return factory.createConnection();
    }

    private String getBrokerConnUrl() {
        return brokers;
    }

    public ArtemisClient artemisClient(
            final LifecycleEnvironment lifecycleEnvironment,
            final MetricRegistry metricRegistry,
            final ObjectMapper mapper
    ) {
        try {
            ArtemisClient client = new ArtemisClient(getConnection(), metricRegistry, mapper);
            lifecycleEnvironment.manage(new Managed() {
                @Override
                public void start() throws Exception {
                    client.start();
                }

                @Override
                public void stop() throws Exception {
                    client.stop();
                }
            });
            return client;
        } catch (JMSException e) {
            throw new ArtemisClientException(e, "ArtemisClient.init");
        }

    }

}
