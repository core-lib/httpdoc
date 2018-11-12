package io.httpdoc.core.interpretation;

import com.sun.javadoc.*;
import com.sun.tools.javadoc.ClassDocImpl;
import com.sun.tools.javadoc.Main;
import io.httpdoc.core.Config;
import io.httpdoc.core.Lifecycle;
import io.httpdoc.core.kit.IOKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class SourceInterpreter implements Interpreter, Lifecycle {

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
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("@return", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("summary");
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("@summary", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("deprecated");
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("@deprecated", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("skip");
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("@skip", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("tag");
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("@tag", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("ignore");
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("@ignore", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("alias");
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("@alias", tags[i].name(), tags[i].text()));
        }
        {
            Tag[] tags = doc.tags("order");
            for (int i = 0; tags != null && i < tags.length; i++) notes.add(new Note("@order", tags[i].name(), tags[i].text()));
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

    @Override
    public void initial(Config config) throws Exception {
        Javadoc.initial();
    }

    @Override
    public void destroy() {
        Javadoc.destroy();
    }

    public static abstract class Javadoc {
        private static Logger logger = LoggerFactory.getLogger(Javadoc.class);

        private static RootDoc root;
        private static String srcPath;
        private static String libPath;
        private static String pkgPath;

        private static void initial() throws IOException {
            // 临时目录
            File directory = getDirectory();
            // 判断当前是不是Spring-Boot的单JAR包启动
            if (isSpringBoot()) {
                forSpringBoot(directory);
            }
            // 其他都是用通用方案
            else {
                forWebContent(directory);
            }
            // 构造RootDoc
            build();
        }

        @Deprecated
        public synchronized static boolean start(RootDoc root) {
            Javadoc.root = root;
            return true;
        }

        private static void build() throws IOException {
            File txt = new File(srcPath, "packages.txt");
            Set<String> folders = getSrcFolders(new File(srcPath));
            String separator = System.getProperty("line.separator");
            StringBuilder builder = new StringBuilder();
            for (String folder : folders) {
                String pkg = folder.substring(srcPath.length() + 1).replace(File.separator, ".");
                builder.append(pkg).append(separator);
            }
            IOKit.transfer(new StringReader(builder.toString().trim()), txt);
            pkgPath = txt.getPath();
            Main.execute(new String[]{
                    "-doclet",
                    Javadoc.class.getName(),
                    "-encoding",
                    "utf-8",
                    "-classpath",
                    libPath,
                    "-sourcepath",
                    srcPath,
                    "@" + pkgPath
            });
        }

        private static void forWebContent(File directory) {
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

            srcPath = directory.getPath();
            StringBuilder libraries = new StringBuilder();
            String separator = System.getProperty("path.separator");
            libraries.append("\"");
            for (URL url : resources) {
                try {
                    // 只处理本地文件
                    if (!"file".equalsIgnoreCase(url.getProtocol())) continue;
                    String file = URLDecoder.decode(url.getPath(), Charset.defaultCharset().name());
                    // 如果文件不存在则忽略掉
                    if (!new File(file).exists()) {
                        continue;
                    }
                    // 如果是一个jar包
                    if (file.endsWith(".jar")) {
                        extract(new JarFile(file, false), directory);
                    }
                    // 否则就是一个文件夹
                    else {
                        extract(file, new File(file), directory);
                    }
                    String path = new File(file).getPath();
                    libraries.append(path).append(separator);
                } catch (Exception e) {
                    logger.warn("error reading classpath: " + url, e);
                }
            }
            libraries.append("\"");
            libPath = libraries.toString();
        }

        private static void forSpringBoot(File directory) {
            JarFile boot = null;
            try {
                String classpath = System.getProperty("java.class.path");
                // 1. 将整个Spring Boot 打包之后的JAR包 解压
                String workspace = System.getProperty("user.dir");
                String filepath = workspace + File.separator + classpath;
                boot = new JarFile(filepath);
                Manifest manifest = boot.getManifest();
                Attributes attributes = manifest.getMainAttributes();

                String clsLocation = attributes.getValue("Spring-Boot-Classes");
                String libLocation = attributes.getValue("Spring-Boot-Lib");

                Enumeration<JarEntry> entries = boot.entries();
                while (entries != null && entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    if (jarEntry.isDirectory()) continue;
                    String name = jarEntry.getName();
                    if (name.startsWith(clsLocation) && name.endsWith(".java")) extract(boot, jarEntry, directory);
                    if (name.startsWith(libLocation) && name.endsWith(".jar")) extract(boot, jarEntry, directory);
                }

                File folder = new File(directory, clsLocation);
                srcPath = folder.getPath();

                File[] libs = new File(directory, libLocation).listFiles();
                StringBuilder libraries = new StringBuilder();
                String separator = System.getProperty("path.separator");
                libraries.append("\"");
                for (int i = 0; libs != null && i < libs.length; i++) {
                    String path = libs[i].getPath();
                    libraries.append(path).append(separator);

                    // 将其中的源码也提取到源码目录里面去
                    JarFile jarFile = new JarFile(libs[i]);
                    Enumeration<JarEntry> jarEntries = jarFile.entries();
                    while (jarEntries != null && jarEntries.hasMoreElements()) {
                        JarEntry jarEntry = jarEntries.nextElement();
                        if (jarEntry.isDirectory()) continue;
                        String name = jarEntry.getName();
                        if (name.endsWith(".java")) extract(jarFile, jarEntry, folder);
                    }

                    IOKit.close(jarFile);
                }
                libraries.append("\"");
                libPath = libraries.toString();
            } catch (IOException e) {
                logger.warn("error reading classpath" + (boot != null ? boot.getName() : ""), e);
            } finally {
                IOKit.close(boot);
            }
        }

        private static File getDirectory() throws IOException {
            File directory = new File(""
                    + System.getProperty("java.io.tmpdir")
                    + File.separator
                    + "httpdoc"
                    + File.separator
                    + UUID.randomUUID().toString().replace('-', ' ')
            );
            if (!directory.exists() && !directory.mkdirs()) {
                throw new IOException("could not create directory: " + directory);
            }
            return directory;
        }

        private static boolean isSpringBoot() {
            String classpath = System.getProperty("java.class.path");
            String[] jarLocations = classpath.split(";");
            if (jarLocations.length == 1) {
                JarFile jar = null;
                try {
                    String workspace = System.getProperty("user.dir");
                    String filepath = workspace + File.separator + classpath;
                    File file = new File(filepath);
                    jar = new JarFile(file, false);
                    Manifest manifest = jar.getManifest();
                    Attributes attributes = manifest.getMainAttributes();
                    String clsLocation = attributes.getValue("Spring-Boot-Classes");
                    String libLocation = attributes.getValue("Spring-Boot-Lib");
                    return clsLocation != null && libLocation != null;
                } catch (Exception e) {
                    return false;
                } finally {
                    IOKit.close(jar);
                }
            }
            return false;
        }

        private static Set<String> getSrcFolders(File file) {
            Set<String> folders = new LinkedHashSet<>();
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; files != null && i < files.length; i++) {
                    folders.addAll(getSrcFolders(files[i]));
                }
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                folders.add(file.getParent());
            }
            return folders;
        }

        private static void extract(final JarFile jarFile, final JarEntry jarEntry, final File directory) {
            InputStream in = null;
            File src = null;
            try {
                String name = jarEntry.getName();
                src = new File(directory, name);
                if (!src.getParentFile().exists() && !src.getParentFile().mkdirs()) {
                    logger.warn("could not create directory: " + src.getParentFile());
                    return;
                }
                in = jarFile.getInputStream(jarEntry);
                IOKit.transfer(in, src);
            } catch (Exception e) {
                logger.warn("error occur while copying jar src file: " + jarEntry + " to: " + src);
            } finally {
                IOKit.close(in);
            }
        }

        private static void extract(final String root, final File file, final File directory) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; files != null && i < files.length; i++) {
                    extract(root, files[i], directory);
                }
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                File src = null;
                InputStream in = null;
                try {
                    String name = file.getAbsolutePath().substring(new File(root).getAbsolutePath().length());
                    src = new File(directory, name);
                    if (!src.getParentFile().exists() && !src.getParentFile().mkdirs()) {
                        logger.warn("could not create directory: " + src.getParentFile());
                        return;
                    }
                    in = new FileInputStream(file);
                    IOKit.transfer(in, src);
                } catch (Exception e) {
                    logger.warn("error occur while copying src file: " + file + " to: " + src);
                } finally {
                    IOKit.close(in);
                }
            }
        }

        private static void extract(final JarFile jarFile, final File directory) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if (!name.endsWith(".java")) continue;
                extract(jarFile, jarEntry, directory);
            }
        }

        private static void destroy() {
            root = null;
            IOKit.delete(new File(srcPath), true);
        }

        private synchronized static ClassDoc getClassDoc(Class<?> clazz) {
            return root != null ? root.classNamed(clazz.getName()) : null;
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
