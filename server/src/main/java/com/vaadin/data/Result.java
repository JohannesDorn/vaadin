/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.data;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents the result of an operation that might fail, such as input
 * validation or type conversion. A result may contain either a value,
 * signifying a successful operation, or an error message in case of a failure.
 * <p>
 * Result instances are created using the factory methods {@link #ok(R)} and
 * {@link #error(String)}, denoting success and failure respectively.
 * <p>
 * Unless otherwise specified, {@code Result} method arguments cannot be null.
 *
 * @param <R>
 *            the result value type
 */
public interface Result<R> extends Serializable {

    /**
     * Returns a successful result wrapping the given value.
     *
     * @param <R>
     *            the result value type
     * @param value
     *            the result value, can be null
     * @return a successful result
     */
    public static <R> Result<R> ok(R value) {
        return new SimpleResult<>(value, null);
    }

    /**
     * Returns a failure result wrapping the given error message.
     *
     * @param <R>
     *            the result value type
     * @param message
     *            the error message
     * @return a failure result
     */
    public static <R> Result<R> error(String message) {
        Objects.requireNonNull(message, "message cannot be null");
        return new SimpleResult<R>(null, message);
    }

    /**
     * Returns a Result representing the result of invoking the given supplier.
     * If the supplier returns a value, returns a {@code Result.ok} of the
     * value; if an exception is thrown, returns the message in a
     * {@code Result.error}.
     *
     * @param <R>
     *            the result value type
     * @param supplier
     *            the supplier to run
     * @param onError
     *            the function to provide the error message
     * @return the result of invoking the supplier
     */
    public static <R> Result<R> of(Supplier<R> supplier,
            Function<Exception, String> onError) {
        Objects.requireNonNull(supplier, "supplier cannot be null");
        Objects.requireNonNull(onError, "onError cannot be null");

        try {
            return ok(supplier.get());
        } catch (Exception e) {
            return error(onError.apply(e));
        }
    }

    /**
     * If this Result has a value, returns a Result of applying the given
     * function to the value. Otherwise, returns a Result bearing the same error
     * as this one. Note that any exceptions thrown by the mapping function are
     * not wrapped but allowed to propagate.
     *
     * @param <S>
     *            the type of the mapped value
     * @param mapper
     *            the mapping function
     * @return the mapped result
     */
    public default <S> Result<S> map(Function<R, S> mapper) {
        return flatMap(value -> ok(mapper.apply(value)));
    }

    /**
     * If this Result has a value, applies the given Result-returning function
     * to the value. Otherwise, returns a Result bearing the same error as this
     * one. Note that any exceptions thrown by the mapping function are not
     * wrapped but allowed to propagate.
     *
     * @param <S>
     *            the type of the mapped value
     * @param mapper
     *            the mapping function
     * @return the mapped result
     */
    public <S> Result<S> flatMap(Function<R, Result<S>> mapper);

    /**
     * Invokes either the first callback or the second one, depending on whether
     * this Result denotes a success or a failure, respectively.
     *
     * @param ifOk
     *            the function to call if success
     * @param ifError
     *            the function to call if failure
     */
    public void handle(Consumer<R> ifOk, Consumer<String> ifError);

    /**
     * Applies the {@code consumer} if result is not an error.
     *
     * @param consumer
     *            consumer to apply in case it's not an error
     */
    public default void ifOk(Consumer<R> consumer) {
        handle(consumer, error -> {
        });
    }

    /**
     * Applies the {@code consumer} if result is an error.
     *
     * @param consumer
     *            consumer to apply in case it's an error
     */
    public default void ifError(Consumer<String> consumer) {
        handle(value -> {
        }, consumer);
    }

    /**
     * Returns {@code true} if result is an error.
     *
     * @return whether the result is an error
     */
    public boolean isError();

    /**
     * Returns an Optional of the result message, or an empty Optional if none.
     *
     * @return the optional message
     */
    public Optional<String> getMessage();

    /**
     * Returns an Optional of the value, or an empty Optional if none.
     *
     * @return the optional value
     */
    public Optional<R> getValue();
}
