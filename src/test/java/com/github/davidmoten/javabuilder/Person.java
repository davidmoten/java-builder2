package com.github.davidmoten.javabuilder;

import java.util.Optional;


public final class Person {

    private final String firstName;
    private final String lastName;
    private final Integer age;
    private final String nickname;

    private Person(String firstName, String lastName, Integer age, String nickname) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.nickname = nickname;
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

    public Optional<Integer> age() {
        return Optional.ofNullable(age);
    }

    public String nickname() {
        return nickname;
    }

    public static final class Builder1 {

        private String firstName;
        private String lastName;
        private Integer age;
        private String nickname = "bucko";

        Builder1(){
        }

        public Builder2 firstName(String firstName) {
            this.firstName = firstName;
            return new Builder2(this);
        }

    }

    public static final class Builder2 {

        private final Builder1 b;

        Builder2(Builder1 b){
            this.b = b;
        }

        public Builder3 lastName(String lastName) {
            b.lastName = lastName;
            return new Builder3(b);
        }

    }

    public static final class Builder3 {

        private final Builder1 b;

        Builder3(Builder1 b) {
             this.b = b;
        }

        public Builder3 age(Integer age) {
            b.age = age;
            return this;
        }

        public Builder3 nickname(String nickname) {
            b.nickname = nickname;
            return this;
        }

        public Person build() {
            return new Person(b.firstName, b.lastName, b.age, b.nickname);
        }
    }
}