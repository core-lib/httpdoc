package io.httpdoc.core.description;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class SourceDescriber implements Describer {

    @Override
    public String describe(Class<?> clazz) {
        return Javadoc.describe(clazz);
    }

    @Override
    public String describe(Method method) {
        return Javadoc.describe(method);
    }

    @Override
    public String describe(Field field) {
        return Javadoc.describe(field);
    }

    @Override
    public String describe(Enum<?> constant) {
        return Javadoc.describe(constant);
    }

    @Override
    public String describe(PropertyDescriptor descriptor) {
        try {
            Method method = descriptor.getReadMethod();
            String description = describe(method);
            if (description != null) return description;
            Class<?> clazz = method.getDeclaringClass();
            String name = descriptor.getName();
            Field field = clazz.getDeclaredField(name);
            return describe(field);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public static abstract class Javadoc {
        private static Map<Class<?>, ClassDoc> cache = new HashMap<Class<?>, ClassDoc>();
        private static List<String> allJavaFiles;
        private static RootDoc root;

        public synchronized static boolean start(RootDoc root) {
            Javadoc.root = root;
            return true;
        }

        public synchronized static ClassDoc getClassDoc(Class<?> clazz) {
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
            return allJavaFiles = getAllJavaFiles(new File(System.getProperty("user.dir")));
        }

        private static List<String> getAllJavaFiles(File root) {
            if (root.isDirectory()) {
                List<String> files = new ArrayList<String>();
                File[] subs = root.listFiles();
                for (int i = 0; subs != null && i < subs.length; i++) files.addAll(getAllJavaFiles(subs[i]));
                return files;
            } else if (root.isFile() && root.getName().endsWith(".java")) {
                return Collections.singletonList(root.getAbsolutePath().replace('\\', '/'));
            } else {
                return Collections.emptyList();
            }
        }

        public static String describe(Class<?> clazz) {
            ClassDoc doc = getClassDoc(clazz);
            return doc != null ? doc.getRawCommentText() : null;
        }

        public static String describe(Field field) {
            Class<?> clazz = field.getDeclaringClass();
            ClassDoc doc = getClassDoc(clazz);
            if (doc == null) return null;
            FieldDoc[] fields = doc.fields(false);
            for (FieldDoc fd : fields) if (fd.name().equals(field)) return fd.getRawCommentText();
            return null;
        }

        public static String describe(Enum<?> constant) {
            Class<?> clazz = constant.getDeclaringClass();
            String name = constant.name();
            ClassDoc doc = getClassDoc(clazz);
            if (doc == null) return null;
            FieldDoc[] fields = doc.fields();
            for (FieldDoc fd : fields) if (fd.name().equals(name)) return fd.getRawCommentText();
            return null;
        }

        public static String describe(Method method) {
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
                Class<?> cl = method.getParameterTypes()[i];
                if (cl.isArray()) {
                    int dimensions = 0;
                    while (cl.isArray()) {
                        dimensions++;
                        cl = cl.getComponentType();
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(cl.getName());
                    for (int d = 0; d < dimensions; d++) sb.append("[]");
                    builder.append(sb);
                } else {
                    builder.append(cl.getName());
                }
            }
            builder.append(")");
            String signature = builder.toString();
            for (MethodDoc md : doc.methods()) if (signature.equals(md.toString())) return md.getRawCommentText();
            return null;
        }

    }
}
