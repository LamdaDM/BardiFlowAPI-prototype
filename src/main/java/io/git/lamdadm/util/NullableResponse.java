package io.git.lamdadm.util;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Null object that holds immutable data, with a flag indicating whether there is data present or not.
 * @apiNote Changing data requires saving the data first,
 * then transforming and passing it to a new NullableResponse.
 * @implNote Error field is intended for coupling any exceptions associated with the data, which can
 * then be bubbled up the stack for further handling.
 * @param <T> Type of data held.
 */
public class NullableResponse<T> implements Serializable {

    private final T DATA;

    private final Boolean EMPTY;

    private final Exception ERROR;

    public T data() { return DATA; }

    public Boolean isEmpty() { return EMPTY; }

    public Exception error() { return ERROR; }

    public NullableResponse(T DATA) {
        this.DATA = DATA;
        this.EMPTY = false;
        this.ERROR = null;
    }

    public NullableResponse(T DATA, Boolean EMPTY) {
        this.DATA = DATA;
        this.EMPTY = EMPTY;
        this.ERROR = null;
    }

    public NullableResponse(T DATA, Boolean EMPTY, Exception error) {
        this.DATA = DATA;
        this.EMPTY = EMPTY;
        this.ERROR = error;
    }
    /**
     * Helper function to handle any error that exists.
     * @param handler Function to call if an error exists, with the error being passed as an argument.
     */
    public void ifErrorHandle(Consumer<Exception> handler){
        if(ERROR != null){ handler.accept(ERROR); }
    }

    public void ifErrorHandle(Consumer<Exception> onError, Handler noError){
        if(ERROR != null){ onError.accept(ERROR); }
        else{ noError.handle(); }
    }

    public void ifErrorHandle(Consumer<Exception> onError, Consumer<T> noError){
        if(ERROR != null){ onError.accept(ERROR); }
        else { noError.accept(DATA); }
    }

    /**
     * Helper function tu supply a value in case of an error.
     * @param onError Function to apply with the error passed, if the error exists.
     * @param noError Default value that's returned in case the error does not exist.
     * @param <R> Return type.
     */
    public <R> R ifErrorReturnElse(Function<Exception, R> onError, R noError){
        if(ERROR != null){ return onError.apply(ERROR); }
        return noError;
    }

    public <R> R ifErrorReturnElse(Function<Exception, R> onError, Function<T, R> noError){
        if(ERROR != null){ return onError.apply(ERROR); }
        return noError.apply(DATA);
    }

    /**
     * Helper function to provide state-dependent execution.
     * @param onError If error exists, apply handler to error.
     * @param otherwise If data exists and there is no error, apply transformer to data and return.
     * @param onEmpty Default value that's returned in case there is no data or error;
     * @param <R> Return type.
     */
    public <R> R ifErrorOrEmptyElse(Function<Exception, R> onError, R onEmpty, Function<T, R> otherwise) {
        if(ERROR != null){ return onError.apply(ERROR); }
        if(EMPTY){ return onEmpty; }
        return otherwise.apply(DATA);
    }

    /**
     * Helper function that returns A or B depending on whether data exists or not.  
     */
    public <R> R ifEmptyReturnElse(Function<T, R> notEmpty, R onEmpty) {
        return !isEmpty()
                ? notEmpty.apply(DATA)
                : onEmpty;
    }

    /**
     * Helper function that returns A or B depending on whether data exists or not.
     * @param notEmpty Function to apply to data if exists.
     * @param onEmpty Default value to return if data doesn't exist.
     * @param <R> Return type.
     */
    public <R> R ifEmptyReturnElse(Function<T, R> notEmpty, Supplier<R> onEmpty) {
        return ifEmptyReturnElse(notEmpty, onEmpty.get());
    }

    public void ifEmptyHandle(Handler funcIfEmpty){
        if(ERROR != null){ funcIfEmpty.handle(); }
    }

    public static NullableResponse<Void> transferThrowableDataToErrorField(
            NullableResponse<Throwable> throwableNullableResponse) {
        return new NullableResponse<>(null, true, (Exception) throwableNullableResponse.data());
    }

    /**
     * Overloading to avoid presenting unnecessary information in JSON responses.
     * @implNote Intended for serializing, to allow passing nullable objects with data to response builders.
     */
    @Override
    public String toString() { return DATA.toString(); }

    private interface Handler {
        void handle();
    }
}
