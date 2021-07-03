package io.git.lamdadm.controllers;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api")
public class LiveTestController {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello!";
    }
}
