package io.git.lamdadm.controllers;

import io.lettuce.core.api.StatefulRedisConnection;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.session.Session;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.sql.Date;
import java.time.Instant;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/auth")
public class AuthController {

    @Inject
    private StatefulRedisConnection<String, String> redisClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/success")
    public Response loginSuccess(Session session, Principal principal) {
        var sessId= session.getId();
        var redisCommands = redisClient.reactive();

        redisCommands.set(sessId + ":lastauth", Date.from(Instant.now()).toString());

        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/fail")
    public Response loginFailed() { return Response.status(400).build(); }
}