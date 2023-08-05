package org.cheise_proj;

import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

//lookup: https://java.testcontainers.org/test_framework_integration/manual_lifecycle_control/#singleton-containers
public abstract class IntegrationTest {

    private static final String POSTGRES_IMAGE = "postgres:12-alpine";
    private static final String ACTIVE_MQ_IMAGE = "symptoma/activemq:5.18.0";
    public static PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER;
    public static DropwizardTestSupport<AppConfiguration> SUPPORT;
    public static GenericContainer<?> ACTIVE_MQ_Container;
    static {
        //mac issues: https://stackoverflow.com/questions/61108655/test-container-test-cases-are-failing-due-to-could-not-find-a-valid-docker-envi
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(POSTGRES_IMAGE);
        ACTIVE_MQ_Container = new GenericContainer<>(ACTIVE_MQ_IMAGE).withExposedPorts(61616);

        POSTGRE_SQL_CONTAINER.start();
        ACTIVE_MQ_Container.start();
        initSupport();
    }


    private static void initSupport() {
        if (POSTGRE_SQL_CONTAINER.isRunning() && ACTIVE_MQ_Container.isRunning()) {
            SUPPORT = new DropwizardTestSupport<>(AppApplication.class,
                    ResourceHelpers.resourceFilePath("config.yml"),
                    ConfigOverride.config("database.url", IntegrationTest.POSTGRE_SQL_CONTAINER.getJdbcUrl()),
                    ConfigOverride.config("database.user", IntegrationTest.POSTGRE_SQL_CONTAINER.getUsername()),
                    ConfigOverride.config("database.password", IntegrationTest.POSTGRE_SQL_CONTAINER.getPassword()),
                    ConfigOverride.config("migrationFileLocation", "test-migrations.xml"),
                    ConfigOverride.config("artemis.brokers", "tcp://localhost:" + IntegrationTest.ACTIVE_MQ_Container.getMappedPort(61616))
            );
            SUPPORT.before();
        }
    }
}
