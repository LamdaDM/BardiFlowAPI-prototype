package io.git.lamdadm.services;

import io.git.lamdadm.util.NullableResponse;
import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.function.Supplier;

public class ServiceBase {

    protected <T> Single<NullableResponse<T>> generateResponse(Supplier<Single<T>> source) {

        try{
            return source.get().map(NullableResponse::new);
        } catch (Exception e) {
            return Single.just(new NullableResponse<>(null, false, e));
        }
    }

    protected Single<NullableResponse<Void>> generateResponseFromInsert(Throwable source) {
        return generateResponse(() -> Single.just(source))
            .map(NullableResponse::transferThrowableDataToErrorField);
    }

    protected Single<NullableResponse<Void>> generateResponseFromInsert(Completable source) {

        return generateResponseFromInsert(source.blockingGet());
    }

    protected Single<NullableResponse<Void>> generateResponseFromInsert(Supplier<Completable> source) {
        return generateResponseFromInsert(source.get());
    }
}