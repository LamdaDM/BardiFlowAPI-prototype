package io.git.lamdadm.services;

import io.git.lamdadm.util.NullableResponse;
import io.micronaut.runtime.http.scope.RequestScope;
import io.reactivex.Single;

import javax.inject.Inject;
import java.time.LocalDate;

@RequestScope
public class InsightService {

    private final ProfileService profileService;
    private final BalanceProjectingService balanceProjectingService;

    @Inject public InsightService(ProfileService service, BalanceProjectingService balanceProjectingService) {
        profileService = service;
        this.balanceProjectingService = balanceProjectingService;
    }

    public Single<NullableResponse<Double>> projectBalance(String email, LocalDate targetDate) {
        return profileService
            .findAndReturnFullProfileByEmail(email)
            .map(profileNullableResponse -> profileNullableResponse.ifEmptyReturnElse(
                profile -> new NullableResponse<>(
                    balanceProjectingService
                        .projectBalance(profile, targetDate)
                        .blockingGet()
                ),
                new NullableResponse<>(null, true, profileNullableResponse.error())
            ));
    }
}