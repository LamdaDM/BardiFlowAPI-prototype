package io.git.lamdadm.controllers;

import io.git.lamdadm.data.dtos.CreateTransformerDto;
import io.git.lamdadm.services.TransformerService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api/transformers")
public class TransformerController {

    private final TransformerService service;

    @Inject
    TransformerController(TransformerService transformerService) {
        service = transformerService;
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/add")
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> addTransformer(Principal principal, CreateTransformerDto dto) {

        return service
            .addTransformer(principal.getName(), dto)
            .map(res -> res.ifErrorReturnElse(
                e -> {
                    e.printStackTrace();
                    return Response.status(400);
                },
                Response.created(URI.create("placeholder"))
            )).map(Response.ResponseBuilder::build);
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/edit/interest")
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> editInterest(
            Principal principal,
            @QueryParam("interest") Double interest,
            @QueryParam("id") Long transformerId) {

        return service
            .changeInterest(principal.getName(), transformerId, interest)
            .map(res -> res.ifErrorReturnElse(
                e -> {
                    e.printStackTrace();
                    return Response.status(400);
                },
                Response.noContent()
            )).map(Response.ResponseBuilder::build);
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/edit/amount")
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> editAmount(
            Principal principal,
            @QueryParam("amount") Double amount,
            @QueryParam("id") Long transformerId) {

        return service
            .changeAmount(principal.getName(), transformerId, amount)
            .map(res -> res.ifErrorReturnElse(
                e -> {
                    e.printStackTrace();
                    return Response.status(400);
                },
                Response.noContent()
            )).map(Response.ResponseBuilder::build);
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/edit/payment")
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> editPayment(
            Principal principal,
            @QueryParam("payment") Double payment,
            @QueryParam("id") Long transformerId) {

        return service
            .changePayment(principal.getName(), transformerId, payment)
            .map(res -> res.ifErrorReturnElse(
                e -> {
                    e.printStackTrace();
                    return Response.status(400);
                },
                Response.noContent()
            )).map(Response.ResponseBuilder::build);
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/edit/cycle")
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> editCycle(
            Principal principal,
            @QueryParam("cycle") Integer cycle,
            @QueryParam("id") Long transformerId) {

        return service
            .changeCycle(principal.getName(), transformerId, cycle)
            .map(res -> res.ifErrorReturnElse(
                e -> {
                    e.printStackTrace();
                    return Response.status(400);
                },
                Response.noContent()
            )).map(Response.ResponseBuilder::build);
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/edit/reference_date")
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> editreference_date(
            Principal principal,
            @QueryParam("reference_date") String termDate,
            @QueryParam("id") Long transformerId) {

        LocalDate reference_date;
        try {
            reference_date = LocalDate.parse(termDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            return Single.just(Response.status(400).build());
        }

        return service
            .changereference_date(principal.getName(), transformerId, reference_date)
            .map(res -> res.ifErrorReturnElse(
                e -> {
                    e.printStackTrace();
                    return Response.status(400);
                },
                Response.noContent()
            )).map(Response.ResponseBuilder::build);
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/remove")
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> removeWhole(
            Principal principal,
            @QueryParam("transformer_id") Long transformer_id) {

        return service
            .removeWhole(principal.getName(), transformer_id)
            .map(res -> res.ifErrorReturnElse(
                e -> {
                    e.printStackTrace();
                    return Response.status(400);
                },
                Response.noContent()
            )).map(Response.ResponseBuilder::build);
    }
}
