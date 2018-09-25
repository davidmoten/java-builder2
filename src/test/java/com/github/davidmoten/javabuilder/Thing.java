package com.github.davidmoten.javabuilder;


public final class Thing {

    private final String firstName;
    private final String lastName;

    private Thing(String firstName, String lastName) {
        notNull(firstName, "firstName");
        notNull(lastName, "lastName");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Builder1 builder() {
        return new Builder1();
    }

    public static Builder1 firstName(String firstName) {
;        return builder().firstName(firstName);
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public static final class Builder1 {

        private String firstName = "hello";
        private String lastName = "there";

        Builder1() {
        }

        public Builder1 firstName(String firstName) {
            notNull(firstName, "firstName");
            this.firstName = firstName;
            return this;
        }

        public Builder1 lastName(String lastName) {
            notNull(lastName, "lastName");
            this.lastName = lastName;
            return this;
        }

        public Thing build() {
            return new Thing(this.firstName, this.lastName);
        }
    }

    private static void notNull(Object o, String name) {
        if (o == null) {
            throw new NullPointerException(name + " cannot be null");
        }
    }
}