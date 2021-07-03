package io.git.lamdadm.services;

import io.git.lamdadm.data.models.Profile;
import io.git.lamdadm.util.NullableResponse;
import io.reactivex.Single;

public interface ProfileReq {

    public Single<NullableResponse<Profile>> findAndReturnFullProfileByEmail(String email);
}
