package io.httpdoc.core.interpretation;

import com.sun.javadoc.*;
import com.sun.tools.javadoc.Main;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class SourceInterpreter implements Interpreter {

    @Override
    public ClassInterpretation interpret(Class<?> clazz) {
        ClassDoc doc = Javadoc.of(clazz);
        if (doc == null) return null;
        Tag[] tags = doc.tags();
        Note[] notes = new Note[tags != null ? tags.length : 0];
        for (int i = 0; tags != null && i < tags.length; i++) notes[i] = new Note(tags[i].kind(), tags[i].name(), tags[i].text());
        return new ClassInterpretation(doc.commentText(), notes, doc.getRawCommentText());
    }

    @Override
    public MethodInterpretation interpret(Method method) {
        MethodDoc doc = Javadoc.of(method);
        if (doc == null) return null;
        List<Note> notes = new ArrayList<>();
        {
            ParamTag[] tags = doc.paramTags();
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("param", tags[i].parameterName(), tags[i].parameterComment()));
        }
        {
            ThrowsTag[] tags = doc.throwsTags();
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("throws", tags[i].exceptionName(), tags[i].exceptionComment()));
        }
        {
            Tag[] tags = doc.tags("return");
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("return", null, tags[i].text()));
        }
        return new MethodInterpretation(doc.commentText(), notes.toArray(new Note[0]), doc.getRawCommentText());
    }

    @Override
    public FieldInterpretation interpret(Field field) {
        FieldDoc doc = Javadoc.of(field);
        if (doc == null) return null;
        Tag[] tags = doc.tags();
        Note[] notes = new Note[tags != null ? tags.length : 0];
        for (int i = 0; tags != null && i < tags.length; i++) notes[i] = new Note(tags[i].kind(), tags[i].name(), tags[i].text());
        return new FieldInterpretation(doc.commentText(), notes, doc.getRawCommentText());
    }

    @Override
    public EnumInterpretation interpret(Enum<?> constant) {
        FieldDoc doc = Javadoc.of(constant);
        if (doc == null) return null;
        Tag[] tags = doc.tags();
        Note[] notes = new Note[tags != null ? tags.length : 0];
        for (int i = 0; tags != null && i < tags.length; i++) notes[i] = new Note(tags[i].kind(), tags[i].name(), tags[i].text());
        return new EnumInterpretation(doc.commentText(), notes, doc.getRawCommentText());
    }

    @Override
    public Interpretation interpret(PropertyDescriptor descriptor) {
        try {
            Method method = descriptor.getReadMethod();
            Interpretation interpretation = interpret(method);
            if (interpretation != null && interpretation.getContent() != null && interpretation.getContent().trim().length() > 0) return interpretation;
            Class<?> clazz = method.getDeclaringClass();
            String name = descriptor.getName();
            Field field = clazz.getDeclaredField(name);
            return interpret(field);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public static abstract class Javadoc {
        private static Map<Class<?>, ClassDoc> cache = new HashMap<>();
        private static List<String> allJavaFiles;
        private static RootDoc root;

        public synchronized static boolean start(RootDoc root) {
            Javadoc.root = root;
            return true;
        }

        private synchronized static ClassDoc getClassDoc(Class<?> clazz) {
            if (cache.containsKey(clazz)) return cache.get(clazz);

            String path = getProjectClassAbsolutePath(clazz);
            if (path == null) return null;
            String classpath = System.getProperty("java.class.path");
            Main.execute(new String[]{
                    "-doclet",
                    Javadoc.class.getName(),
                    "-encoding",
                    "utf-8",
                    "-classpath",
                    classpath,
                    path
            });
            ClassDoc doc = root == null ? null : root.classNamed(clazz.getName());

            cache.put(clazz, doc);

            return doc;
        }

        private static String getProjectClassAbsolutePath(Class<?> clazz) {
            List<String> files = getProjectAllJavaFiles();
            String path = "/" + clazz.getName().replace('.', '/') + ".java";
            for (String file : files) if (file.endsWith(path)) return file;
            return null;
        }

        private static List<String> getProjectAllJavaFiles() {
            if (allJavaFiles != null) return allJavaFiles;
            String path = System.getProperty("httpdoc.src.path", System.getProperty("user.dir"));
            File root = new File(path);
            return allJavaFiles = getAllJavaFiles(root);
        }

        private static List<String> getAllJavaFiles(File root) {
            if (root.isDirectory()) {
                List<String> files = new ArrayList<>();
                File[] subs = root.listFiles();
                for (int i = 0; subs != null && i < subs.length; i++) files.addAll(getAllJavaFiles(subs[i]));
                return files;
            } else if (root.isFile() && root.getName().endsWith(".java")) {
                return Collections.singletonList(root.getAbsolutePath().replace('\\', '/'));
            } else {
                return Collections.emptyList();
            }
        }

        private static ClassDoc of(Class<?> clazz) {
            return getClassDoc(clazz);
        }

        private static FieldDoc of(Field field) {
            Class<?> clazz = field.getDeclaringClass();
            ClassDoc doc = getClassDoc(clazz);
            if (doc == null) return null;
            FieldDoc[] fields = doc.fields(false);
            for (FieldDoc fd : fields) if (fd.name().equals(field.getName())) return fd;
            return null;
        }

        private static FieldDoc of(Enum<?> constant) {
            Class<?> clazz = constant.getDeclaringClass();
            String name = constant.name();
            ClassDoc doc = getClassDoc(clazz);
            if (doc == null) return null;
            FieldDoc[] fields = doc.fields();
            for (FieldDoc fd : fields) if (fd.name().equals(name)) return fd;
            return null;
        }

        private static MethodDoc of(Method method) {
            Class<?> clazz = method.getDeclaringClass();
            ClassDoc doc = getClassDoc(clazz);
            if (doc == null) return null;
            StringBuilder builder = new StringBuilder();
            builder.append(method.getDeclaringClass().getName())
                    .append(".")
                    .append(method.getName())
                    .append("(");
            for (int i = 0; i < method.getParameterTypes().length; i++) {
                if (i > 0) builder.append(", ");
                Class<?> type = method.getParameterTypes()[i];
                String name = type.getPackage() != null && type.getPackage().equals(clazz.getPackage()) ? type.getSimpleName() : type.getName();
                if (type.isArray()) {
                    int dimensions = 0;
                    while (type.isArray()) {
                        dimensions++;
                        type = type.getComponentType();
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(name);
                    for (int d = 0; d < dimensions; d++) sb.append("[]");
                    builder.append(sb);
                } else {
                    builder.append(name);
                }
            }
            builder.append(")");
            String signature = builder.toString();
            for (MethodDoc md : doc.methods()) if (signature.equals(md.toString())) return md;
            return null;
        }

    }
}
