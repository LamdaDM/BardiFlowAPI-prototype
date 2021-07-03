package io.git.lamdadm.authentication;

import io.git.lamdadm.util.NullableResponse;
import io.git.lamdadm.services.ProfileService;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class AuthProviderLocal implements AuthenticationProvider {

    @Inject private ProfileService service;

    @Override
    public Publisher<AuthenticationResponse> authenticate(
        HttpRequest<?> httpRequest,
        AuthenticationRequest<?, ?> authenticationRequest
    ) {
        return Flowable.create(emitter -> {
            var iden = (String) authenticationRequest.getIdentity();
            var pw = (String) authenticationRequest.getSecret();

            // If iden and pw do not match credentials in database, throw auth error.
            // Else, return UserDetails and a list of roles.
            var res = service
                    .findProfileByEmail(iden)
                    .map(nResponse -> nResponse.ifEmptyReturnElse(
                        data -> {
                            if(iden.equals(data.email) && pw.equals(data.password)){
                                return new NullableResponse<>(
                                    new UserDetails(iden, new ArrayList<>())
                                );
                            }
                            else {
                                return new NullableResponse<UserDetails>(
                                    null,
                                    true,
                                    new AuthenticationException(new AuthenticationFailed())
                                );
                            }
                        },
                        new NullableResponse<UserDetails>(
                            null,
                            true,
                            new AuthenticationException(new AuthenticationFailed())
                        )
                    )).blockingGet();

            res.ifErrorHandle(
                emitter::onError,
                data -> {
                    emitter.onNext(data);
                    emitter.onComplete();
                }
            );
        }, BackpressureStrategy.ERROR);
    }
}
