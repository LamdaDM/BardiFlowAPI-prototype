package io.git.lamdadm.services;

import io.git.lamdadm.caching.CacheKey;
import io.git.lamdadm.caching.CacheableItem;
import io.git.lamdadm.data.models.Finance;
import io.git.lamdadm.data.repositories.FinanceRepository;
import io.git.lamdadm.util.NullableResponse;
import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.runtime.http.scope.RequestScope;
import io.reactivex.Single;

import javax.inject.Inject;

@RequestScope
public class FinanceService extends ServiceBase {

    private final FinanceRepository repo;

    @Inject public FinanceService(FinanceRepository repository) {
        repo = repository;
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> changeInterest(String email, double interest) {

        return generateResponseFromInsert(repo
            .updateInterest(email, interest));
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> changeBalance(String email, double balance) {

        return generateResponseFromInsert(repo
            .updateBalance(email, balance));
    }

    @Cacheable(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Finance>> getFinance(String email) {

        return generateResponse(() -> repo
            .getFinance(email));
    }
}