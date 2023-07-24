package org.cheise_proj.users;

import org.cheise_proj.resource.ResourceResponse;

import javax.validation.Valid;
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
    public Response register(@Valid UserDto input) {
        User user = userService.register(input);
        UserCreatedResponse userCreatedResponse = UserCreatedResponse.Builder.builder().id(user.getId()).build();
        ResourceResponse response = ResourceResponse.Builder.builder()
                .success(true)
                .data(userCreatedResponse)
                .build();
        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }


    @GET
    @Path("/{userId}")
    public Response findById(@PathParam("userId") long userId) {
        UserResponse user = Optional.of(userService.getUser(userId)).map(UserResponse::toResponse)
                .orElse(null);
        ResourceResponse response = ResourceResponse.Builder.builder()
                .success(true)
                .data(user)
                .build();
        return Response.ok(response).build();
    }

    @GET
    public Response findAll() {
        List<UserResponse> responses = userService.getUsers().stream().map(UserResponse::toResponse).collect(Collectors.toList());
        ResourceResponse users = ResourceResponse.Builder.builder()
                .success(true)
                .data(responses)
                .build();
        return Response.ok(users).build();
    }
}
