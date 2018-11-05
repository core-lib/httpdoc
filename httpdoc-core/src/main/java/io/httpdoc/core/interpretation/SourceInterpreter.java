package io.httpdoc.core.interpretation;

import com.sun.javadoc.*;
import com.sun.tools.javadoc.ClassDocImpl;
import com.sun.tools.javadoc.Main;
import io.httpdoc.core.kit.IOKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("@param", tags[i].parameterName(), tags[i].parameterComment()));
        }
        {
            ThrowsTag[] tags = doc.throwsTags();
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("@throws", tags[i].exceptionName(), tags[i].exceptionComment()));
        }
        {
            Tag[] tags = doc.tags("return");
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("@return", null, tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("summary");
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("@summary", null, tags[i].text()));
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
        private static Logger logger = LoggerFactory.getLogger(Javadoc.class);

        private static Map<Class<?>, ClassDoc> cache = new HashMap<>();
        private static RootDoc root;
        private static String srcPath;
        private static String libPath;

        static {
            // 找出所有classpath
            Set<URL> resources = new LinkedHashSet<>();
            ClassLoader classLoader = Javadoc.class.getClassLoader();
            while (classLoader != null) {
                if (classLoader instanceof URLClassLoader) {
                    URL[] urls = ((URLClassLoader) classLoader).getURLs();
                    resources.addAll(urls != null && urls.length > 0 ? Arrays.asList(urls) : Collections.<URL>emptySet());
                }
                classLoader = classLoader.getParent();
            }

            // 把所有 .java 源码文件放到一个统一的目录
            File directory = new File(System.getProperty("java.io.tmpdir") + File.separator + "httpdoc" + File.separator + UUID.randomUUID());
            srcPath = directory.getPath();
            StringBuilder builder = new StringBuilder();
            for (URL url : resources) {
                try {
                    // 只处理本地文件
                    if (!"file".equalsIgnoreCase(url.getProtocol())) continue;
                    String file = url.getFile();
                    // 如果文件不存在则忽略掉
                    if (!new File(file).exists()) {
                        continue;
                    }
                    // 如果是一个jar包
                    if (file.endsWith(".jar")) {
                        extractTo(new JarFile(file, false), directory);
                    }
                    // 否则就是一个文件夹
                    else {
                        extractTo(file, new File(file), directory);
                    }
                    builder.append(new File(url.getFile()).getPath()).append(";");
                } catch (Exception e) {
                    logger.warn("error reading classpath: " + url, e);
                }
            }
            libPath = builder.toString();
        }

        private static void extractTo(final String root, final File file, final File directory) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; files != null && i < files.length; i++) {
                    extractTo(root, files[i], directory);
                }
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                File src = new File(directory, file.getAbsolutePath().substring(new File(root).getAbsolutePath().length()));
                if (!src.getParentFile().exists() && !src.getParentFile().mkdirs()) {
                    logger.warn("error occur while making directory: " + src.getParentFile());
                    return;
                }
                InputStream in = null;
                try {
                    in = new FileInputStream(file);
                    IOKit.transfer(in, src);
                } catch (IOException e) {
                    logger.warn("error occur while copying src file: " + file + " to: " + src);
                } finally {
                    IOKit.close(in);
                }
            }
        }

        private static void extractTo(final JarFile jarFile, final File directory) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if (!name.endsWith(".java")) continue;
                File src = new File(directory, name);
                if (!src.getParentFile().exists() && !src.getParentFile().mkdirs()) {
                    logger.warn("error occur while making directory: " + src.getParentFile());
                    continue;
                }
                InputStream in = null;
                try {
                    in = jarFile.getInputStream(jarEntry);
                    IOKit.transfer(in, src);
                } catch (IOException e) {
                    logger.warn("error occur while copying jar src file: " + jarEntry + " to: " + src);
                } finally {
                    IOKit.close(in);
                }
            }
        }

        public synchronized static boolean start(RootDoc root) {
            Javadoc.root = root;
            return true;
        }

        private synchronized static ClassDoc getClassDoc(Class<?> clazz) {
            if (cache.containsKey(clazz)) return cache.get(clazz);

            String name = clazz.getName();
            File file = new File(srcPath, name.replace('.', '/') + ".java");
            if (!file.exists() || !file.isFile()) return null;

            Main.execute(new String[]{
                    "-doclet",
                    Javadoc.class.getName(),
                    "-encoding",
                    "utf-8",
                    "-classpath",
                    libPath,
                    "-sourcepath",
                    srcPath,
                    file.getPath()
            });
            ClassDoc doc = root == null ? null : root.classNamed(name);

            cache.put(clazz, doc);

            return doc;
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
