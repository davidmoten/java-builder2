package com.github.davidmoten.javabuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

public class GeneratorTest {

    private static final String TEST_OUTPUT_FOLDER = "src/test/java/com/github/davidmoten/javabuilder";

    @Test
    public void testToConsole() {
        Generator.pkg("au.gov.amsa.fax.gofax").className("Document") //
                .type("byte[]").name("data").mandatory().build() //
                .type("String").name("filename").mandatory().build() //
                .type("String").name("header").mandatory().build() //
                .type("String").name("from").mandatory().build() //
                .type("String").name("to").mandatory().build() //
                .generate();
    }
    
    @Test
    public void replaceNonMandatoryClass() throws IOException {
        Generator.pkg("com.github.davidmoten.javabuilder") //
                .className("Thing") //
                .type("String").name("firstName").defaultValue("\"hello\"").entryMethod().build() //
                .type("String").name("lastName").defaultValue("\"there\"").build() //
                .generate(TEST_OUTPUT_FOLDER) //
                .generate();
    }
    
    @Test
    public void replacePersonClass() throws IOException {
        Generator.pkg("com.github.davidmoten.javabuilder") //
                .className("Person") //
                .type("String").name("firstName").mandatory().entryMethod().build() //
                .type("String").name("lastName").mandatory().build() //
                .type("Integer").name("age").build() //
                .type("int").name("height").defaultValue("0").build() //
                .type("String").name("nickname").defaultValue("\"bucko\"").build() //
                .generate(TEST_OUTPUT_FOLDER) //
                .generate();
    }

    @Test
    public void testPersonBuilder() {
        Person p = Person.builder().firstName("dave").lastName("moten").nickname("bucko").age(23)
                .build();
        assertEquals("dave", p.firstName());
        assertEquals("moten", p.lastName());
        assertEquals("bucko", p.nickname());
        assertEquals(23, (int) p.age().get());
    }

    @Test
    public void replacePerson2ClassNoMandatory() throws IOException {
        Generator.pkg("com.github.davidmoten.javabuilder") //
                .className("Person2") //
                .type("String").name("firstName").entryMethod().build() //
                .type("String").name("lastName").entryMethod().build() //
                .generate(TEST_OUTPUT_FOLDER) //
                .generate();
    }

    @Test
    public void testPerson2Builder() {
        Person2 p = Person2.firstName("dave").lastName("moten").build();
        assertEquals("dave", p.firstName().get());
        assertEquals("moten", p.lastName().get());
    }

    @Test
    public void testPerson2BuilderNotSet() {
        Person2 p = Person2.builder().build();
        assertFalse(p.firstName().isPresent());
        assertFalse(p.lastName().isPresent());
    }

    @Test
    public void replacePerson3ClassOnlyMandatory() throws IOException {
        Generator.pkg("com.github.davidmoten.javabuilder") //
                .className("Person3") //
                .type("String").name("firstName").mandatory().build() //
                .type("String").name("lastName").mandatory().build() //
                .generate(TEST_OUTPUT_FOLDER) //
                .generate();
    }

    @Test
    public void testPerson3Builder() {
        Person3 p = Person3.builder().firstName("dave").lastName("moten");
        assertEquals("dave", p.firstName());
        assertEquals("moten", p.lastName());
    }

}
