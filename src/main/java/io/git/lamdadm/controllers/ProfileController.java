package io.git.lamdadm.controllers;

import io.git.lamdadm.data.dtos.CreateProfileDto;
import io.git.lamdadm.services.ProfileService;
import io.lettuce.core.api.StatefulRedisConnection;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.security.Principal;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/profile")
public class ProfileController {

    private final ProfileService service;
    private StatefulRedisConnection<String, String> redisClient;

    @Inject ProfileController(ProfileService profileService, StatefulRedisConnection<String, String> redis) {
        redisClient = redis;
        service = profileService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/find/{id}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> findProfile(long id) {

        return service.findProfileById(id)
            .map(res -> res.ifEmptyReturnElse(
                Response::ok,
                Response.status(404)
            )).map(Response.ResponseBuilder::build);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> findFullProfile(Principal principal) {
//        if(!principal.getName().equals(email)){ return Single.just(Response.status(401).build()); }

        return service
            .findAndReturnFullProfileByEmail(principal.getName())
            .map(res -> res.ifEmptyReturnElse(
                Response::ok,
                Response.status(404)
            )).map(Response.ResponseBuilder::build);
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/new")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> newProfile(@Body CreateProfileDto dto) {

        return service.createNewProfile(dto)
            .map(res -> res.ifErrorReturnElse(
                e -> Response.status(400),
                Response.created(URI.create("/api/profile/get"))
            )).map(Response.ResponseBuilder::build);
    }

    @POST
    @Path("/refresh")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @ExecuteOn(TaskExecutors.IO)
    public Response refresh(Principal principal) {

        service.refreshProfile(principal.getName());
        return Response.noContent().build();
    }
}
