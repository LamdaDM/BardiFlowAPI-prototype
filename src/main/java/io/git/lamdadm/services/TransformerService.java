package io.git.lamdadm.services;

import io.git.lamdadm.caching.CacheKey;
import io.git.lamdadm.caching.CacheableItem;
import io.git.lamdadm.data.dtos.CreateTransformerDto;
import io.git.lamdadm.data.repositories.TransformerRepository;
import io.git.lamdadm.util.NullableResponse;
import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.runtime.http.scope.RequestScope;
import io.reactivex.Single;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.function.Supplier;

@RequestScope
public class TransformerService extends ServiceBase {

    private final TransformerRepository repo;

    @Inject public TransformerService(TransformerRepository repository){ repo = repository; }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> addTransformer(String email, CreateTransformerDto dto) {

        return generateResponseFromInsert(
            repo.insert(email, dto)
        );
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> changeInterest(String email, Double interest) {

        return generateResponseFromInsert(
            repo.updateInterest(email, interest)
        );
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> changeInterest(String email, Long transformer_id,
                                                         Double interest) {

        return generateResponseFromInsert(
            repo.updateInterest(email, transformer_id, interest)
        );
    }


    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> changeAmount(String email, Double amount) {

        return generateResponseFromInsert(
            repo.updateAmount(email, amount)
        );
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> changeAmount(String email, Long transformer_id,
                                                       Double amount) {

        return generateResponseFromInsert(
            repo.updateAmount(email, transformer_id, amount)
        );
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> changePayment(String email, Double payment) {

        return generateResponseFromInsert(
            repo.updatePayment(email, payment)
        );
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> changePayment(String email, Long transformer_id,
                                                        Double amount) {

        return generateResponseFromInsert(
            repo.updatePayment(email, transformer_id, amount)
        );
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> changeCycle(String email, Integer cycle) {

        return generateResponseFromInsert(
            repo.updateCycle(email, cycle)
        );
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> changeCycle(String email, Long transformer_id,
                                                      Integer cycle) {

        return generateResponseFromInsert(
            repo.updateCycle(email, transformer_id, cycle)
        );
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> changereference_date(String email, LocalDate reference_date) {

        return generateResponseFromInsert(
            repo.updatereference_date(email, reference_date)
        );
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> changereference_date(String email, Long transformer_id,
                                                            LocalDate reference_date) {

        return generateResponseFromInsert(
            repo.updatereference_date(email, transformer_id, reference_date)
        );
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Void>> removeWhole(String email, Long transformer_id) {

        return generateResponseFromInsert(
            repo.delete(email, transformer_id)
        );
    }
}
