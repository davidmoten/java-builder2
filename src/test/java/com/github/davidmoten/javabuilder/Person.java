package com.github.davidmoten.javabuilder;

import java.util.Optional;


public final class Person {

    private final String firstName;
    private final String lastName;
    private final Optional<Integer> age;
    private final int height;
    private final String nickname;

    private Person(String firstName, String lastName, Optional<Integer> age, int height, String nickname) {
        notNull(firstName, "firstName");
        notNull(lastName, "lastName");
        notNull(age, "age");
        notNull(height, "height");
        notNull(nickname, "nickname");
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.height = height;
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
        return age;
    }

    public int height() {
        return height;
    }

    public String nickname() {
        return nickname;
    }

    public static final class Builder1 {

        private String firstName;
        private String lastName;
        private Optional<Integer> age = Optional.empty();
        private int height = 0;
        private String nickname = "bucko";

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

        public Builder3 lastName(String lastName) {
            notNull(lastName, "lastName");
            b.lastName = lastName;
            return new Builder3(b);
        }

    }

    public static final class Builder3 {

        private final Builder1 b;

        Builder3(Builder1 b) {
             this.b = b;
        }

        public Builder3 age(int age) {
            notNull(age, "age");
            b.age = Optional.of(age);
            return this;
        }

        public Builder3 height(int height) {
            notNull(height, "height");
            b.height = height;
            return this;
        }

        public Builder3 nickname(String nickname) {
            notNull(nickname, "nickname");
            b.nickname = nickname;
            return this;
        }

        public Person build() {
            return new Person(b.firstName, b.lastName, b.age, b.height, b.nickname);
        }
    }

    private static void notNull(Object o, String name) {
        if (o == null) {
            throw new NullPointerException(name + " cannot be null");
        }
    }
}