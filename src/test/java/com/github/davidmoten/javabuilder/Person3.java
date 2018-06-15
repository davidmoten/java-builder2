package com.github.davidmoten.javabuilder;


public final class Person3 {

    private final String firstName;
    private final String lastName;

    private Person3(String firstName, String lastName) {
        notNull(firstName, "firstName");
        notNull(lastName, "lastName");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Builder1 builder() {
        return new Builder1();
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public static final class Builder1 {

        private String firstName;
        private String lastName;

        Builder1(){
        }

        public Builder2 firstName(String firstName) {
            notNull(firstName, "firstName");
            this.firstName = firstName;
            return new Builder2(this);
        }

    }

    public static final class Builder2 {

        private final Builder1 b;

        Builder2(Builder1 b){
            this.b = b;
        }

        public Person3 lastName(String lastName) {
            notNull(lastName, "lastName");
            b.lastName = lastName;
            return new Person3(b.firstName, b.lastName);
        }

    }

    private static void notNull(Object o, String name) {
        if (o == null) {
            throw new NullPointerException(name + " cannot be null");
        }
    }
}