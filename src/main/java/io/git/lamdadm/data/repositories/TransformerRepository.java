package io.git.lamdadm.data.repositories;

import io.git.lamdadm.data.dtos.CreateTransformerDto;
import io.micronaut.runtime.http.scope.RequestScope;
import io.reactivex.Completable;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.Tuple;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;

@RequestScope
public class TransformerRepository extends RepositoryBase{

    private final MySQLPool client;

    @Inject public TransformerRepository(MySQLPool pool) {
        this.client = pool;
    }

    public Completable insert(String email, CreateTransformerDto dto){

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "INSERT INTO transformers " +
                        "(interest, name, description, amount, payment, cycle, reference_date, for_email) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                ).rxExecute(Tuple.tuple(
                    Arrays.asList(
                        dto.interest,
                        dto.name,
                        dto.description,
                        dto.amount,
                        dto.payment,
                        dto.cycle,
                        dto.reference_date,
                        email
                    )
                ))
                .ignoreElement()
            );
    }

    public Completable updateInterest(String email, Double interest) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE transformers " +
                        "SET interest = ? " +
                        "WHERE for_email = ?"
                ).rxExecute(Tuple.of(interest, email))
                .ignoreElement()
            );
    }

    public Completable updateInterest(String email, long transformer_id, Double interest) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE transformers " +
                        "SET interest = ? " +
                        "WHERE transformer_id = ? AND for_email = ?"
                ).rxExecute(Tuple.of(interest, transformer_id, email))
                .ignoreElement()
            );
    }

    public Completable updateAmount(String email, Double amount) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE transformers " +
                        "SET amount = ? " +
                        "WHERE for_email = ?"
                ).rxExecute(Tuple.of(amount, email))
                .ignoreElement()
            );
    }

    public Completable updateAmount(String email, long transformer_id, Double amount) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE transformers " +
                        "SET amount = ? " +
                        "WHERE transformer_id = ? AND for_email = ?"
                ).rxExecute(Tuple.of(amount, transformer_id, email))
                .ignoreElement()
            );
    }

    public Completable updatePayment(String email, Double payment) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE transformers " +
                        "SET payment = ? " +
                        "WHERE for_email = ?"
                ).rxExecute(Tuple.of(payment, email))
                .ignoreElement()
            );
    }

    public Completable updatePayment(String email, long transformer_id, Double payment) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE transformers " +
                        "SET payment = ? " +
                        "WHERE transformer_id = ? AND for_email = ?"
                ).rxExecute(Tuple.of(payment, transformer_id, email))
                .ignoreElement()
            );
    }

    public Completable updateCycle(String email, Integer cycle) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE transformers " +
                        "SET cycle = ? " +
                        "WHERE for_email = ?"
                ).rxExecute(Tuple.of(cycle, email))
                .ignoreElement()
            );
    }

    public Completable updateCycle(String email, long transformer_id, Integer cycle) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE transformers " +
                        "SET cycle = ? " +
                        "WHERE transformer_id = ? AND for_email = ?"
                ).rxExecute(Tuple.of(cycle, transformer_id, email))
                .ignoreElement()
            );
    }

    public Completable updatereference_date(String email, LocalDate reference_date) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE transformers " +
                        "SET reference_date = ? " +
                        "WHERE for_email = ?"
                ).rxExecute(Tuple.of(reference_date, email))
                .ignoreElement()
            );
    }

    public Completable updatereference_date(String email, long transformer_id, LocalDate reference_date) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE transformers " +
                        "SET reference_date = ?" +
                        "WHERE transformer_id = ? AND for_email = ?"
                ).rxExecute(Tuple.of(reference_date, transformer_id, email))
                .ignoreElement()
            );
    }

    public Completable updateName(String email, long transformer_id, String name) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "UPDATE transformers " +
                        "SET name = ? " +
                        "WHERE transformer_id = ? AND for_email = ?"
                ).rxExecute(Tuple.of(name, transformer_id, email))
                .ignoreElement()
            );
    }

    public Completable delete(String email, Long transformer_id) {

        return client
            .rxGetConnection()
            .flatMapCompletable(sqlConnection -> sqlConnection
                .preparedQuery(
                    "DELETE FROM transformers " +
                        "WHERE transformer_id = ? AND for_email = ?"
                ).rxExecute(Tuple.of(transformer_id, email))
                .ignoreElement()
            );
    }
}
