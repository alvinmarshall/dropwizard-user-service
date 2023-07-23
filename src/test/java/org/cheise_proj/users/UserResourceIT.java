package org.cheise_proj.users;

import org.cheise_proj.IntegrationTest;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


class UserResourceIT extends IntegrationTest {

    private Client client;

    @BeforeEach
    void setUp() {
        client = new JerseyClientBuilder().build();
    }

    @Test
    void register() {
        Response response = client.target(String.format("http://localhost:%d/users", SUPPORT.getLocalPort()))
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(new UserDto("Test", "test@me.com")));
        System.out.println("resp: " + response.readEntity(Object.class).toString());
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    void findById() {
        Response response = client.target(String.format("http://localhost:%d/users/100", SUPPORT.getLocalPort()))
                .request(MediaType.APPLICATION_JSON)
                .get();
        System.out.println("resp: " + response.readEntity(Object.class).toString());
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void findAll() throws Exception {
        Response response = client.target(String.format("http://localhost:%d/users", SUPPORT.getLocalPort()))
                .request(MediaType.APPLICATION_JSON)
                .get();
        System.out.println("resp: " + response.readEntity(Object.class).toString());
        Assertions.assertEquals(200, response.getStatus());
    }


}
