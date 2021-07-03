package io.git.lamdadm.services;

import io.git.lamdadm.caching.CacheKey;
import io.git.lamdadm.util.NullableResponse;
import io.git.lamdadm.caching.CacheableItem;
import io.git.lamdadm.data.dtos.CreateProfileDto;
import io.git.lamdadm.data.models.Profile;
import io.git.lamdadm.data.repositories.ProfileRepository;
import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.runtime.http.scope.RequestScope;
import io.reactivex.Single;

import javax.inject.Inject;

@RequestScope
public class ProfileService extends ServiceBase {

    private final ProfileRepository repo;

    protected ProfileService(){ repo = null; }

    @Inject ProfileService(ProfileRepository repository) {
        repo = repository;
    }

    public Single<NullableResponse<Profile>> findProfileByEmail(String email) {

        assert repo != null;
        return generateResponse(() -> repo
            .getSingleByEmail(email)
        );
    }

    public Single<NullableResponse<Profile>> findProfileById(long id) {

        assert repo != null;
        return generateResponse(() -> repo
                .getSingleById(id)
        );
    }

    public Single<NullableResponse<Void>> createNewProfile(CreateProfileDto dto) {

        assert repo != null;
        return generateResponseFromInsert(repo
            .insert(dto));
    }

    @Cacheable(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public Single<NullableResponse<Profile>> findAndReturnFullProfileByEmail(String email) {

        assert repo != null;
        return generateResponse(() -> repo
            .getFull(email)
        );
    }

    @CacheInvalidate(cacheNames = {CacheableItem.MAIN}, parameters = {CacheKey.IDENTIFIER})
    public void refreshProfile(String email) {}
}
