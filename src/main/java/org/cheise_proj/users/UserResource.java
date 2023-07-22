package org.cheise_proj.users;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    public Response register(UserDto input) {
        User register = userService.register(input);
        return Response.status(Response.Status.CREATED)
                .entity(UserCreatedResponse.Builder.builder().id(register.getId()).build())
                .build();
    }


    @GET
    @Path("/{userId}")
    public Response findById(@PathParam("userId") long userId) {
        UserResponse response = Optional.of(userService.getUser(userId)).map(UserResponse::toResponse)
                .orElse(null);
        return Response.ok(response).build();
    }

    @GET
    public Response findAll() {
        List<UserResponse> responses = userService.getUsers().stream().map(UserResponse::toResponse).collect(Collectors.toList());
        return Response.ok(responses).build();
    }
}
