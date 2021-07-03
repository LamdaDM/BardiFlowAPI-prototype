package io.git.lamdadm.data.repositories;

import io.git.lamdadm.data.dtos.CreateProfileDto;
import io.git.lamdadm.data.models.Finance;
import io.git.lamdadm.data.models.Profile;
import io.git.lamdadm.data.models.Transformer;
import io.micronaut.runtime.http.scope.RequestScope;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.*;

import javax.inject.Inject;
import java.util.Vector;

@RequestScope
public class ProfileRepository extends RepositoryBase {
    private final MySQLPool client;

    @Inject public ProfileRepository(MySQLPool pool) {
        this.client = pool;
    }

    private Single<Profile> fromSingleDetails(RowIterator<Row> iterator){

        return Single.create(emitter -> {
           if(iterator.hasNext()){
               var row = iterator.next();

               try{
                   emitter.onSuccess(new Profile(
                       row.getLong("profile_id"),
                       row.getString("name"),
                       row.getString("email"),
                       row.getString("password"))
                   );
               } catch(Exception e) { emitter.onError(e);}
           } else { emitter.onError(new Exception("No row found")); }
        });
    }

    private Single<Profile> fromSingleFull(RowIterator<Row> iterator) throws Exception {

        return profileFullFromRowSet(iterator);
    }

    public Single<Profile> getFull(String email){
        return client
            .preparedQuery(
                "SELECT * FROM profiles " +
                    "INNER JOIN finances ON email = owner_email " +
                    "INNER JOIN transformers ON owner_email = for_email " +
                    "AND email = ?"
            ).rxExecute(Tuple.of(email))
            .map(RowSet::iterator)
            .flatMap(this::fromSingleFull);
    }

    public Single<Profile> getSingleById(long id) {
        return client
            .preparedQuery(
                "SELECT * " +
                    "FROM profiles " +
                    "WHERE profile_id = ?"
            ).rxExecute(Tuple.of(id))
            .map(RowSet::iterator)
            .flatMap(this::fromSingleDetails);
    }

    public Single<Profile> getSingleByEmail(String name) {
        return client
            .preparedQuery(
                "SELECT * " +
                    "FROM profiles " +
                    "WHERE email = ?"
            ).rxExecute(Tuple.of(name))
            .map(RowSet::iterator)
            .flatMap(this::fromSingleDetails);
    }

    public Completable insert(CreateProfileDto dto) {

    return client
        .rxGetConnection()
        .compose(upstream -> {
            var task1 = upstream
                .flatMapCompletable(conn1 -> conn1.preparedQuery(
                    "INSERT INTO profiles " +
                        "(name, email, password) " +
                        "VALUES (?, ?, ?)"
                  ).rxExecute(Tuple.of(dto.name, dto.email, dto.password))
                  .ignoreElement());

            var task2 = upstream
                  .flatMapCompletable(conn2 -> conn2.preparedQuery(
                      "INSERT INTO finances " +
                          "(owner_email) " +
                          "VALUES (?)"
                  ).rxExecute(Tuple.of(dto.email))
                  .ignoreElement());

            return Single.just(task1.mergeWith(task2));
          }).blockingGet();
    }
}
