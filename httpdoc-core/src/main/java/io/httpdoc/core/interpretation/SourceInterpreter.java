package io.httpdoc.core.interpretation;

import com.sun.javadoc.*;
import com.sun.tools.javadoc.ClassDocImpl;
import com.sun.tools.javadoc.Main;
import io.httpdoc.core.Config;
import io.httpdoc.core.Lifecycle;
import io.httpdoc.core.kit.IOKit;
import io.loadkit.Loaders;
import io.loadkit.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class SourceInterpreter implements Interpreter, Lifecycle {

    @Override
    public ClassInterpretation interpret(Class<?> clazz) {
        ClassDoc doc = Javadoc.of(clazz);
        if (doc == null) return null;
        Tag[] tags = doc.tags();
        Note[] notes = new Note[tags != null ? tags.length : 0];
        for (int i = 0; tags != null && i < tags.length; i++)
            notes[i] = new Note(tags[i].kind(), tags[i].name(), tags[i].text());
        return new ClassInterpretation(doc.commentText(), notes, doc.getRawCommentText());
    }

    @Override
    public MethodInterpretation interpret(Method method) {
        MethodDoc doc = Javadoc.of(method);
        if (doc == null) return null;
        List<Note> notes = new ArrayList<>();
        {
            ParamTag[] tags = doc.paramTags();
            for (int i = 0; tags != null && i < tags.length; i++)
                notes.add(new Note("@param", tags[i].parameterName(), tags[i].parameterComment()));
        }
        {
            ThrowsTag[] tags = doc.throwsTags();
            for (int i = 0; tags != null && i < tags.length; i++)
                notes.add(new Note("@throws", tags[i].exceptionName(), tags[i].exceptionComment()));
        }
        {
            Tag[] tags = doc.tags("return");
            for (int i = 0; tags != null && i < tags.length; i++)
                notes.add(new Note("@return", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("summary");
            for (int i = 0; tags != null && i < tags.length; i++)
                notes.add(new Note("@summary", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("deprecated");
            for (int i = 0; tags != null && i < tags.length; i++)
                notes.add(new Note("@deprecated", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("skip");
            for (int i = 0; tags != null && i < tags.length; i++)
                notes.add(new Note("@skip", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("tag");
            for (int i = 0; tags != null && i < tags.length; i++)
                notes.add(new Note("@tag", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("ignore");
            for (int i = 0; tags != null && i < tags.length; i++)
                notes.add(new Note("@ignore", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("alias");
            for (int i = 0; tags != null && i < tags.length; i++)
                notes.add(new Note("@alias", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("order");
            for (int i = 0; tags != null && i < tags.length; i++)
                notes.add(new Note("@order", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("style");
            for (int i = 0; tags != null && i < tags.length; i++)
                notes.add(new Note("@style", tags[i].name(), tags[i].text()));
        }
        return new MethodInterpretation(doc.commentText(), notes.toArray(new Note[0]), doc.getRawCommentText());
    }

    @Override
    public FieldInterpretation interpret(Field field) {
        FieldDoc doc = Javadoc.of(field);
        if (doc == null) return null;
        Tag[] tags = doc.tags();
        Note[] notes = new Note[tags != null ? tags.length : 0];
        for (int i = 0; tags != null && i < tags.length; i++)
            notes[i] = new Note(tags[i].kind(), tags[i].name(), tags[i].text());
        return new FieldInterpretation(doc.commentText(), notes, doc.getRawCommentText());
    }

    @Override
    public EnumInterpretation interpret(Enum<?> constant) {
        FieldDoc doc = Javadoc.of(constant);
        if (doc == null) return null;
        Tag[] tags = doc.tags();
        Note[] notes = new Note[tags != null ? tags.length : 0];
        for (int i = 0; tags != null && i < tags.length; i++)
            notes[i] = new Note(tags[i].kind(), tags[i].name(), tags[i].text());
        return new EnumInterpretation(doc.commentText(), notes, doc.getRawCommentText());
    }

    @Override
    public Interpretation interpret(PropertyDescriptor descriptor) {
        try {
            Method method = descriptor.getReadMethod();
            Interpretation interpretation = interpret(method);
            if (interpretation != null && interpretation.getContent() != null && interpretation.getContent().trim().length() > 0)
                return interpretation;
            Class<?> clazz = method.getDeclaringClass();
            String name = descriptor.getName();
            Field field = clazz.getDeclaredField(name);
            return interpret(field);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @Override
    public void initial(Config config) throws Exception {
        Javadoc.initial(config);
    }

    @Override
    public void destroy() {
        Javadoc.destroy();
    }

    public static abstract class Javadoc {
        // 采用HashSet当List.retainAll(keywords) 时时间复杂度度可以从 O(n * m) 下降为 O(n) 因为HashSet的contains(o)方法时间复杂度为O(1)
        private static final Set<String> keywords = new HashSet<>(
                Arrays.asList(
                        "abstract", "assert", "boolean", "break", "byte",
                        "case", "catch", "char", "class", "const", "continue",
                        "default", "do", "double", "else", "enum", "extends",
                        "final", "finally", "float", "for", "goto", "if",
                        "implements", "import", "instanceof", "int", "interface",
                        "long", "native", "new", "package", "private", "protected",
                        "public", "return", "strictfp", "short", "static", "super",
                        "switch", "synchronized", "this", "throw", "throws",
                        "transient", "try", "void", "volatile", "while",
                        "null", "true", "false"
                )
        );
        private static final Logger logger = LoggerFactory.getLogger(SourceInterpreter.class);
        private static final Object lock = new Object();
        // 避免一直占用用户太多内存
        private static volatile SoftReference<RootDoc> rootDoc;
        private static String srcPath;
        private static String pkgPath;

        private static void initial(Config config) throws IOException {
            // 临时目录
            File toDIR = new File(""
                    + System.getProperty("java.io.tmpdir")
                    + File.separator
                    + "httpdoc"
                    + File.separator
                    + UUID.randomUUID()
            );
            // 事先创建
            if (!toDIR.exists() && !toDIR.mkdirs() && !toDIR.exists()) {
                throw new IOException("could not create directory: " + toDIR);
            }

            // 提取源码
            Set<String> folders = new LinkedHashSet<>();
            String[] packages = config.getInitParameter("packages").split("[,\\s\r\n]+");
            for (String pkg : packages) {
                Enumeration<Resource> resources = Loaders.ant(Javadoc.class.getClassLoader())
                        .load(pkg.replace('.', '/') + "/**.java");
                while (resources.hasMoreElements()) {
                    Resource resource = resources.nextElement();
                    File file = new File(toDIR, resource.getName());
                    File folder = file.getParentFile();
                    if (!folder.exists() && !folder.mkdirs() && !folder.exists()) {
                        throw new IOException("could not make directory: " + folder);
                    }
                    folders.add(folder.getPath());
                    try (
                            InputStream in = resource.getInputStream();
                            OutputStream out = new FileOutputStream(file)
                    ) {
                        IOKit.transfer(in, out);
                    }
                }
            }
            srcPath = toDIR.getPath();

            // 提取包名
            File toTXT = new File(toDIR, "packages.txt");
            try (
                    OutputStream out = new FileOutputStream(toTXT);
                    Writer writer = new OutputStreamWriter(out)
            ) {
                String separator = System.getProperty("line.separator");
                for (String folder : folders) {
                    String pkg = folder.substring(srcPath.length() + 1).replace(File.separator, ".");
                    // 检查包名是否合法
                    if (pkg.matches("[a-zA-Z_$]+[0-9a-zA-Z_$]*(\\.[a-zA-Z_$]+[0-9a-zA-Z_$]+)*")) {
                        List<String> parts = new ArrayList<>(Arrays.asList(pkg.split("\\.")));
                        // 包含关键字
                        if (parts.removeAll(keywords)) {
                            continue;
                        }
                        writer.append(pkg).append(separator);
                    }
                }
            }
            pkgPath = toTXT.getPath();
        }

        @Deprecated
        public static boolean start(RootDoc rootDoc) {
            Javadoc.rootDoc = new SoftReference<>(rootDoc);
            return true;
        }

        private static RootDoc build() {
            RootDoc doc = rootDoc != null ? rootDoc.get() : null;
            if (doc != null) {
                return doc;
            }
            // double check
            synchronized (lock) {
                PrintWriter error = null;
                PrintWriter warn = null;
                PrintWriter notice = null;
                try {
                    error = new PrintWriter(new FileOutputStream(new File(srcPath, "errors.log"), true));
                    warn = new PrintWriter(new FileOutputStream(new File(srcPath, "warns.log"), true));
                    notice = new PrintWriter(new FileOutputStream(new File(srcPath, "notices.log"), true));

                    doc = rootDoc != null ? rootDoc.get() : null;
                    if (doc != null) return doc;
                    logger.info("start building root doc soft reference, if building frequently you should increase the JVM memories!");

                    Main.execute(
                            "httpdoc",
                            error,
                            warn,
                            notice,
                            "javadoc",
                            Javadoc.class.getClassLoader(),
                            "-doclet",
                            Javadoc.class.getName(),
                            "-verbose",
                            "-encoding",
                            "utf-8",
                            "-sourcepath",
                            srcPath,
                            "@" + pkgPath
                    );

                    doc = rootDoc != null ? rootDoc.get() : null;
                    logger.info("end building root doc found " + (doc != null && doc.classes() != null ? doc.classes().length : 0) + " class(es)");
                    logger.info("more logs is located in directory: " + srcPath);
                } catch (IOException e) {
                    logger.error("error building httpdoc", e);
                } finally {
                    IOKit.close(error);
                    IOKit.close(warn);
                    IOKit.close(notice);
                }
            }
            return doc;
        }

        private static void destroy() {
            rootDoc = null;
            IOKit.delete(new File(srcPath), true);
        }

        private static ClassDoc of(Class<?> clazz) {
            RootDoc rootDoc = build();
            return rootDoc != null ? rootDoc.classNamed(clazz.getName()) : null;
        }

        private static FieldDoc of(Field field) {
            Class<?> clazz = field.getDeclaringClass();
            ClassDoc doc = of(clazz);
            if (doc == null) return null;
            FieldDoc[] fields = doc.fields(false);
            for (FieldDoc fd : fields) if (fd.name().equals(field.getName())) return fd;
            return null;
        }

        private static FieldDoc of(Enum<?> constant) {
            Class<?> clazz = constant.getDeclaringClass();
            String name = constant.name();
            ClassDoc doc = of(clazz);
            if (doc == null) return null;
            FieldDoc[] fields = doc.fields();
            for (FieldDoc fd : fields) if (fd.name().equals(name)) return fd;
            return null;
        }

        private static MethodDoc of(Method method) {
            Class<?> clazz = method.getDeclaringClass();
            ClassDoc doc = of(clazz);
            if (!(doc instanceof ClassDocImpl)) return null;
            ClassDocImpl impl = (ClassDocImpl) doc;
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            String[] types = new String[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> type = parameterTypes[i];
                types[i] = type.getName();
            }
            return impl.findMethod(name, types);
        }

    }
}
