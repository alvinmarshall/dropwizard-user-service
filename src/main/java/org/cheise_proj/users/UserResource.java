package org.cheise_proj.users;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.cheise_proj.resource.ResourceResponse;
import org.eclipse.jetty.http.HttpStatus;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api("User Service")
@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("User registration")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.CREATED_201, message = "User has been created"),
            @ApiResponse(code = 422, message = "Entity can't be processed"),
            @ApiResponse(code = 400, message = "Bad Request sent"),
            @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "Internal error"),
    })
    @Timed
    @ExceptionMetered
    @POST
    public Response register(@Valid final UserDto input) {
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

    @ApiOperation("Get User")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, message = "Get a User"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "Internal error"),
    })
    @Timed
    @ExceptionMetered
    @GET
    @Path("/{userId}")
    public Response findById(@PathParam("userId") final long userId) {
        UserResponse user = Optional.of(userService.getUser(userId)).map(UserResponse::toResponse)
                .orElse(null);
        ResourceResponse response = ResourceResponse.Builder.builder()
                .success(true)
                .data(user)
                .build();
        return Response.ok(response).build();
    }

    @ApiOperation("Get Users")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, message = "Get Users"),
            @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "Internal error"),
    })
    @Timed
    @ExceptionMetered
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
