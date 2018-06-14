package com.github.davidmoten.javabuilder;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Generator {

    public static final class Field {
        private final String type;
        private final String name;
        private final String defaultValue;
        private final boolean mandatory;
        private final boolean isEntryMethod;

        Field(String type, String name, String defaultValue, boolean mandatory, boolean isEntryMethod) {
            this.type = type;
            this.name = name;
            this.defaultValue = defaultValue;
            this.mandatory = mandatory;
            this.isEntryMethod = isEntryMethod;
        }
    }

    public static Builder pkg(String pkg) {
        return new Builder(pkg);
    }

    public static final class Builder {

        private final String pkg;
        private String className;
        public final List<Field> fields = new ArrayList<>();

        Builder(String pkg) {
            this.pkg = pkg;
        }

        public Builder2 className(String className) {
            this.className = className;
            return new Builder2(this);
        }
    }

    public static final class Builder2 {

        private final Builder b;

        Builder2(Builder b) {
            this.b = b;
        }

        Builder3 type(String type) {
            return new Builder3(this, type);
        }

        public void generate(OutputStream os) {
            try (PrintStream out = new PrintStream(os)) {
                out.format("package %s;\n\n", b.pkg);
                out.format("public final class %s {\n\n", b.className);
                List<Field> mandatory = b.fields.stream().filter(f -> f.mandatory).collect(Collectors.toList());
                List<Field> nonMandatory = b.fields.stream().filter(f -> !f.mandatory).collect(Collectors.toList());
                int n = 1;
                for (int i = 0; i < mandatory.size(); i++) {
                    out.format("    public static final class Builder%s {\n\n", n);
                    if (n == 1) {
                        out.format("        Builder%s(){\n", n);
                    } else {
                        out.format("        private final Builder1 b;\n\n");
                        out.format("        Builder%s(Builder1 b){\n", n);
                        out.format("            this.b = b;\n");
                    }
                    out.format("        }\n\n");
                    Field f = mandatory.get(i);
                    if (i < mandatory.size() - 1 || !!nonMandatory.isEmpty()) {
                        out.format("        public Builder%s %s(%s %s) {\n", n + 1, f.name, f.type, f.name);
                        out.format("            b.%s = %s;\n", f.name, f.name);
                        out.format("            return new Builder%s(b);\n", n + 1);
                        out.format("        }\n\n");
                    } else {
                        out.format("        public %s %s(%s %s) {\n", b.className, f.name, f.type, f.name);
                        out.format("            b.%s = %s;\n", f.name, f.name);
                        out.format("            return new %s();\n", b.className);
                        out.format("        }\n\n");

                    }
                    n++;
                    out.format("    }\n\n");
                }
                out.format("}");

                // add nonMandatory builder
            }
        }
    }

    public static final class Builder3 {

        private final Builder2 b;
        private final String type;
        private String name;
        private String defaultValue;
        private boolean mandatory;
        private boolean isEntryMethod;

        Builder3(Builder2 b, String type) {
            this.b = b;
            this.type = type;
        }

        public Builder4 name(String name) {
            this.name = name;
            return new Builder4(this);
        }

    }

    public static final class Builder4 {
        private final Builder3 b;

        Builder4(Builder3 b) {
            this.b = b;
        }

        public Builder4 mandatory() {
            b.mandatory = true;
            return this;
        }

        public Builder4 entryMethod() {
            b.isEntryMethod = true;
            return this;
        }

        public Builder4 defaultValue(String defaultValue) {
            b.defaultValue = defaultValue;
            return this;
        }

        public Builder2 build() {
            b.b.b.fields.add(new Field(b.type, b.name, b.defaultValue, b.mandatory, b.isEntryMethod));
            return b.b;
        }

    }

    public static void main(String[] args) {
        Generator.pkg("au.gov.amsa").className("Person") //
                .type("String").name("firstName").mandatory().entryMethod().build() //
                .type("String").name("lastName").mandatory().build() //
                .type("Optional<Integer>").name("age").build() //
                .generate(System.out);
    }

}
