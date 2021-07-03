package io.git.lamdadm.controllers;

import io.git.lamdadm.data.dtos.ProjectBalanceDTO;
import io.git.lamdadm.services.InsightService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api/insights")
public class InsightsController {

    private final InsightService insightService;

    public InsightsController(InsightService insightService) {
        this.insightService = insightService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/balanceprojection")
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> projectBalance(@QueryParam("target") String targetDate,
                                           Principal principal) {

        LocalDate target;
        try {
            target = LocalDate.parse(targetDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            return Single.just(Response.status(400).build());
        }

        return insightService
            .projectBalance(principal.getName(), target)
            .map(res -> res.ifEmptyReturnElse(
                Response::ok,
                Response.status(400)
            )).map(Response.ResponseBuilder::build);
    }

}
