package io.git.lamdadm.controllers;

import io.git.lamdadm.services.FinanceService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.security.Principal;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api/finance")
public class FinanceController {

    private final FinanceService service;

    @Inject public FinanceController(FinanceService financeService) {
        service = financeService;
    }

    @PUT
    @Path("/interest")
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> changeInterest(Principal principal, @Body Double interest) {
        return service
            .changeInterest(principal.getName(), interest)
            .map(res -> res.ifErrorReturnElse(
                e -> {
                    e.printStackTrace();
                    return Response.status(400);
                },
                Response.noContent()
            )).map(Response.ResponseBuilder::build);
    }

    @PUT
    @Path("/balance")
    @ExecuteOn(TaskExecutors.IO)
    public Single<Response> changeDebit(Principal principal, @Body Double balance) {

        return service
            .changeBalance(principal.getName(), balance)
            .map(res -> res.ifErrorReturnElse(
                e -> {
                    e.printStackTrace();;
                    return Response.status(400);
                },
                Response.status(400)
            )).map(Response.ResponseBuilder::build);
    }
}
