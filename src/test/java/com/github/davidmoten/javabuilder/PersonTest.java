package com.github.davidmoten.javabuilder;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Optional;

import org.junit.Test;

public class PersonTest {

    @Test
    public void replacePersonClass() throws IOException {
        Generator.pkg("com.github.davidmoten.javabuilder") //
                .className("Person") //
                .imports(Optional.class) //
                .type("String").name("firstName").mandatory().entryMethod().build() //
                .type("String").name("lastName").mandatory().build() //
                .type("Integer").name("age").build() //
                .type("String").name("nickname").defaultValue("\"bucko\"").build() //
                .generate("src/test/java/com/github/davidmoten/javabuilder/Person.java");
    }

    @Test
    public void test() {
        Person p = Person.builder().firstName("dave").lastName("moten").nickname("bucko").age(23).build();
        assertEquals("dave", p.firstName());
        assertEquals("moten", p.lastName());
        assertEquals("bucko", p.nickname());
        assertEquals(23, (int) p.age().get());
    }
}
