package io.git.lamdadm.data.repositories;

import io.git.lamdadm.data.models.Finance;
import io.micronaut.runtime.http.scope.RequestScope;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.Tuple;

import javax.inject.Inject;

@RequestScope
public class FinanceRepository extends RepositoryBase {

    private final MySQLPool client;

    @Inject public FinanceRepository(MySQLPool pool) {
        client = pool;
    }

    public Completable updateInterest(String email, double interest) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE finances " +
                        "SET interest = ? " +
                        "WHERE owner_email = ?"
                ).rxExecute(Tuple.of(interest, email))
                .ignoreElement()
            );
    }

    public Completable updateBalance(String email, double balance) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE finances " +
                        "SET balance = ? " +
                        "WHERE owner_email = ?"
                ).rxExecute(Tuple.of(balance, email))
                .ignoreElement()
            );
    }

    public Single<Finance> getFinance(String email) {

        return client
            .rxGetConnection()
            .flatMap(sqlConnection -> sqlConnection
                .preparedQuery(
                    "SELECT * " +
                        "FROM finances " +
                        "WHERE owner_email = ?"
                ).rxExecute(Tuple.of(email))
            ).map(RowSet::iterator)
            .flatMap(this::financeFullFromRowSet);
    }
}
