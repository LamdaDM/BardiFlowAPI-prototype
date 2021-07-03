package io.git.lamdadm.services.mock;

import io.git.lamdadm.data.dtos.CreateProfileDto;
import io.git.lamdadm.data.models.Finance;
import io.git.lamdadm.data.models.Profile;
import io.git.lamdadm.data.models.Transformer;
import io.git.lamdadm.services.ProfileService;
import io.git.lamdadm.util.NullableResponse;
import io.reactivex.Single;

import java.time.LocalDate;
import java.util.Arrays;

public class MockProfileService extends ProfileService {

    @Override
    public Single<NullableResponse<Profile>> findProfileByEmail(String email) {
        return Single.just(new NullableResponse<>(
            null,
            true,
            new Exception("MockProfileService::findProfileByEmail is unusable")
        ));
    }

    @Override
    public Single<NullableResponse<Profile>> findProfileById(long id) {
        return Single.just(new NullableResponse<>(
            null,
            true,
            new Exception("MockProfileService::findProfileById is unusable")
        ));
    }

    @Override
    public Single<NullableResponse<Void>> createNewProfile(CreateProfileDto dto) {
        return Single.just(new NullableResponse<>(
            null,
            true,
            new Exception("MockProfileService::createNewProfile is unusable")
        ));
    }

    @Override
    public Single<NullableResponse<Profile>> findAndReturnFullProfileByEmail(String email) {

        // TODO: Return profile with certain product of balance projection.
        var currentDate = LocalDate.now();
        return Single.just(new NullableResponse<>(
           new Profile((long) 0, "", email, "", new Finance(
               (long) 0,
               0.00,
               0.00,
               Arrays.asList(
                   // Should apply five times
                   new Transformer(
                       (long) 0,
                       "Income",
                       "",
                       0.00,
                       0.00,
                       120.00,
                       73,
                       LocalDate.ofEpochDay(currentDate.toEpochDay() + 365)
                   ),
                   // Should apply once
                   new Transformer(
                       (long) 0,
                       "Car",
                       "",
                       0.00,
                       450.00,
                       -450.00,
                       365,
                       LocalDate.ofEpochDay(currentDate.toEpochDay() + 365)
                   ),
                   // Transformer should not be applied when projecting balance to target date of +365 days
                   new Transformer(
                       (long) 0,
                       "Next year's christmas",
                       "",
                       0.00,
                       0.00,
                       -1000.00,
                       400,
                       LocalDate.ofEpochDay(currentDate.toEpochDay() + 400)
                   ),
                   // Should be considered terminated before applying transformer
                   new Transformer(
                       (long) 0,
                       "Past",
                       "",
                       0.00,
                       49.00,
                       -49.00,
                       1,
                       LocalDate.ofEpochDay(currentDate.toEpochDay() - 1)
                   ),
                   // Should apply once
                   new Transformer(
                       (long) 0,
                       "Toothbrush",
                       "",
                       0.00,
                       97.00,
                       -97.00,
                       7,
                       LocalDate.ofEpochDay(currentDate.toEpochDay() + 8)
                   )
               )
           ))
        ));
    }

    @Override
    public void refreshProfile(String email) {}
}
