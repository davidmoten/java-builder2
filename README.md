# java-builder2

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



## Building
```bash
mvn clean install
```





