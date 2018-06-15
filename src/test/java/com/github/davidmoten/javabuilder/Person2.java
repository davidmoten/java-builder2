package com.github.davidmoten.javabuilder;

import java.util.Optional;


public final class Person2 {

    private final Optional<String> firstName;
    private final Optional<String> lastName;

    private Person2(Optional<String> firstName, Optional<String> lastName) {
        notNull(firstName, "firstName");
        notNull(lastName, "lastName");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Builder1 builder() {
        return new Builder1();
    }

    public Optional<String> firstName() {
        return firstName;
    }

    public Optional<String> lastName() {
        return lastName;
    }

    public static final class Builder1 {

        private Optional<String> firstName = Optional.empty();
        private Optional<String> lastName = Optional.empty();

        Builder1() {
        }

        public Builder1 firstName(String firstName) {
            notNull(firstName, "firstName");
            this.firstName = Optional.of(firstName);
            return this;
        }

        public Builder1 lastName(String lastName) {
            notNull(lastName, "lastName");
            this.lastName = Optional.of(lastName);
            return this;
        }

        public Person2 build() {
            return new Person2(this.firstName, this.lastName);
        }
    }

    private static void notNull(Object o, String name) {
        if (o == null) {
            throw new NullPointerException(name + " cannot be null");
        }
    }
}