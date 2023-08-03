# User-Service [![Pull Request Build](https://github.com/alvinmarshall/dropwizard-user-service/actions/workflows/pr-build.yaml/badge.svg)](https://github.com/alvinmarshall/dropwizard-user-service/actions/workflows/pr-build.yaml)

How to start the App application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/user-service-1.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
