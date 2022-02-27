# java-builder2
<a href="https://github.com/davidmoten/java-builder2/actions/workflows/ci.yml"><img src="https://github.com/davidmoten/java-builder2/actions/workflows/ci.yml/badge.svg"/></a><br/>
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.davidmoten/java-builder2/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.github.davidmoten/java-builder2)<br/>
[![codecov](https://codecov.io/gh/davidmoten/java-builder2/branch/master/graph/badge.svg)](https://codecov.io/gh/davidmoten/java-builder2)

Generates chained or unchained java builders (see [java-builder-pattern-tricks](https://github.com/davidmoten/java-builder-pattern-tricks)).

* write concise java code to generate a class with a builder
* supports builder chaining for mandatory parameters
* represents non-mandatory parameters using `java.util.Optional`
* performs null checks
* converts `Number` classes to primitives in builder method parameters because cannot be null

## Getting started
Add this maven dependency to your pom.xml:

```xml
<dependency>
  <groupId>com.github.davidmoten</groupId>
  <artifactId>java-builder2</artifact>
  <version>VERSION_HERE</version>
  <scope>test</scope>
</dependency>
```
Normally you'll add it as a test scoped dependency.

## Usage
The code below (put in a unit test or a main method in a test class) generates a [`Person`](src/test/java/com/github/davidmoten/javabuilder/Person.java) class in the `target` directory *and* prints it to stdout.

```java
Generator
  .pkg("com.github.davidmoten.javabuilder") //
  .className("Person") //
  .type("String").name("firstName").mandatory().entryMethod().build() //
  .type("String").name("lastName").mandatory().build() //
  .type("Integer").name("age").build() //
  .type("int").name("height").defaultValue("0").build() //
  .type("String").name("nickname").defaultValue("\"bucko\"").build() //
  .generate();
```
There are other overloads of `generate` to write the class to a directory or an `OutputStream`. You can chain `generate` calls to do all of those things if you want.

## Building
```bash
mvn clean install
```





