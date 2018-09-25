package com.github.davidmoten.javabuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Generator {
    
    private Generator() {
        // prevent instantiation
    }

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
        final List<Field> fields = new ArrayList<>();
        final List<String> imports = new ArrayList<>();

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

        public Builder2 imports(Class<?> cls) {
            return imports(cls.getName());
        }

        public Builder2 imports(String cls) {
            b.imports.add(cls);
            return this;
        }

        public Builder3 type(String type) {
            return new Builder3(this, type);
        }

        public GeneratorMore generate(String directory) {
            try (OutputStream os = new FileOutputStream(directory + File.separator + b.className + ".java")) {
                generate(os);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new GeneratorMore(this);
        }

        public GeneratorMore generate() {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            generate(bytes);
            try {
                Files.write(new File("target" + File.separator + b.className + ".java").toPath(), bytes.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(new String(bytes.toByteArray(), StandardCharsets.UTF_8));
            return new GeneratorMore(this);
        }

        public GeneratorMore generate(OutputStream os) {
            try (PrintStream out = new PrintStream(os)) {
                // write builders
                List<Field> mandatory = b.fields.stream().filter(f -> f.mandatory).collect(Collectors.toList());
                List<Field> nonMandatory = b.fields.stream().filter(f -> !f.mandatory).collect(Collectors.toList());
                boolean allNonMandatoryHaveDefaults = b.fields.stream().allMatch(f -> f.mandatory || f.defaultValue != null);

                out.format("package %s;\n\n", b.pkg);

                if (!nonMandatory.isEmpty() && !allNonMandatoryHaveDefaults) {
                    out.println("import java.util.Optional;\n");
                }
                for (String imp : b.imports) {
                    if (!"java.util.Optional".equals(imp)) {
                        out.format("import %s;\n", imp);
                    }
                }
                out.println();
                out.format("public final class %s {\n\n", b.className);

                // write private final fields
                out.println(b.fields.stream().map(f -> "    private final " + declaredType(f) + " " + f.name + ";")
                        .collect(Collectors.joining("\n")));
                out.println();

                // write constructor
                String constructorParameters = b.fields.stream().map(f -> declaredType(f) + " " + f.name)
                        .collect(Collectors.joining(", "));
                out.format("    private %s(%s) {\n", b.className, constructorParameters);

                // write null checks
                String notNulls = b.fields.stream()
                        .map(f -> String.format("        notNull(%s, \"%s\");", f.name, f.name))
                        .collect(Collectors.joining("\n"));
                out.println(notNulls);

                // write field assignments
                String constructorAssignments = b.fields.stream()
                        .map(f -> "        this." + f.name + " = " + f.name + ";") //
                        .collect(Collectors.joining("\n"));
                out.println(constructorAssignments);
                out.format("    }\n\n");

                out.println("    public static Builder1 builder() {");
                out.println("        return new Builder1();");
                out.println("    }\n");

                // add static entry fields
                if (!mandatory.isEmpty()) {
                    Field f = mandatory.get(0);
                    if (f.isEntryMethod) {
                        out.format("    public static Builder2 %s(%s %s) {\n;", f.name, parameterType(f.type), f.name);
                        out.format("        return builder().%s(%s);\n", f.name,f .name);
                        out.format("    }\n\n");
                    }
                } else {
                    for (Field f: nonMandatory) {
                        if (f.isEntryMethod) {
                            out.format("    public static Builder1 %s(%s %s) {\n;", f.name, parameterType(f.type), f.name);
                            out.format("        return builder().%s(%s);\n", f.name,f .name);
                            out.format("    }\n\n");    
                        }
                    }
                }
                
                // write getters
                for (Field f : b.fields) {
                    out.format("    public %s %s() {\n", declaredType(f), f.name);
                    out.format("        return %s;\n", f.name);
                    out.format("    }\n\n");
                }

                int n = 1;
                for (int i = 0; i < mandatory.size(); i++) {
                    out.format("    public static final class Builder%s {\n\n", n);
                    if (n == 1) {
                        writeBuilder1Fields(out);
                        out.println();
                        out.format("        Builder%s(){\n", n);
                    } else {
                        out.format("        private final Builder1 b;\n\n");
                        out.format("        Builder%s(Builder1 b){\n", n);
                        out.format("            this.b = b;\n");
                    }
                    out.format("        }\n\n");
                    Field f = mandatory.get(i);
                    if (i < mandatory.size() - 1 || !nonMandatory.isEmpty()) {
                        out.format("        public Builder%s %s(%s %s) {\n", n + 1, f.name, parameterType(f.type),
                                f.name);
                        out.format("            notNull(%s, \"%s\");\n", f.name, f.name);
                        if (n == 1) {
                            out.format("            this.%s = %s;\n", f.name, f.name);
                        } else {
                            out.format("            b.%s = %s;\n", f.name, f.name);
                        }
                        if (n == 1) {
                            out.format("            return new Builder%s(this);\n", n + 1);
                        } else {
                            out.format("            return new Builder%s(b);\n", n + 1);
                        }
                        out.format("        }\n\n");
                    } else {
                        out.format("        public %s %s(%s %s) {\n", b.className, f.name, parameterType(f.type),
                                f.name);
                        out.format("            notNull(%s, \"%s\");\n", f.name, f.name);
                        out.format("            b.%s = %s;\n", f.name, f.name);
                        out.format("            return new %s(%s);\n", b.className, parameters());
                        out.format("        }\n\n");
                    }
                    out.format("    }\n\n");
                    n++;
                }
                // add nonMandatory builder
                if (!nonMandatory.isEmpty()) {
                    out.format("    public static final class Builder%s {\n", n);
                    out.println();
                    if (mandatory.isEmpty()) {
                        writeBuilder1Fields(out);
                    } else {
                        out.format("        private final Builder1 b;\n");
                    }
                    out.println();
                    if (mandatory.isEmpty()) {
                        out.format("        Builder%s() {\n", n);
                        out.format("        }\n");
                    } else {
                        out.format("        Builder%s(Builder1 b) {\n", n);
                        out.format("             this.b = b;\n");
                        out.format("        }\n");
                    }
                    for (Field m : nonMandatory) {
                        out.format("\n");
                        out.format("        public Builder%s %s(%s %s) {\n", n, m.name, parameterType(m.type), m.name);
                        out.format("            notNull(%s, \"%s\");\n", m.name, m.name);
                        final String field;
                        if (mandatory.isEmpty()) {
                            field = "this";
                        } else {
                            field = "b";
                        }
                        if (m.defaultValue != null) {
                            out.format("            %s.%s = %s;\n", field, m.name, m.name);
                        } else {
                            out.format("            %s.%s = Optional.of(%s);\n", field, m.name, m.name);
                        }
                        out.format("            return this;\n");
                        out.format("        }\n");
                    }

                    out.format("\n        public %s build() {\n", b.className);
                    out.format("            return new %s(%s);\n", b.className, parameters(mandatory.isEmpty()));
                    out.format("        }\n");

                    out.format("    }\n\n");
                }

                out.format("    private static void notNull(Object o, String name) {\n");
                out.format("        if (o == null) {\n");
                out.format("            throw new NullPointerException(name + \" cannot be null\");\n");
                out.format("        }\n");
                out.format("    }\n");
                out.format("}");
                return new GeneratorMore(this);
            }
        }

        private void writeBuilder1Fields(PrintStream out) {
            for (Field field : b.fields) {
                String d;
                if (field.defaultValue != null) {
                    d = " = " + field.defaultValue;
                } else if (!field.mandatory) {
                    d = " = Optional.empty()";
                } else {
                    d = "";
                }
                out.format("        private %s %s%s;\n", declaredType(field), field.name, d);
            }
        }

        private String parameters() {
            return parameters(false);
        }

        private String parameters(boolean useThis) {
            final String field = useThis ? "this" : "b";
            return b.fields.stream().map(x -> field + "." + x.name).collect(Collectors.joining(", "));
        }

    }

    private static String declaredType(Field f) {
        if (f.mandatory || f.defaultValue != null) {
            return f.type;
        } else {
            return "Optional<" + f.type + ">";
        }
    }

    private static String parameterType(String type) {
        if (type.equals("Integer")) {
            return "int";
        } else if (type.equals("Double")) {
            return "double";
        } else if (type.equals("Short")) {
            return "short";
        } else if (type.equals("Boolean")) {
            return "boolean";
        } else if (type.equals("Long")) {
            return "long";
        } else {
            return type;
        }
    }

    public static final class GeneratorMore {
        private final Builder2 b;

        GeneratorMore(Builder2 b) {
            this.b = b;
        }

        public GeneratorMore generate() {
            b.generate();
            return this;
        }

        public GeneratorMore generate(OutputStream os) {
            b.generate(os);
            return this;
        }

        public GeneratorMore generate(String directory) {
            b.generate(directory);
            return this;
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

}
