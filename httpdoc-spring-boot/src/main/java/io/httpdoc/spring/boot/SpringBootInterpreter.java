package io.httpdoc.spring.boot;

import io.httpdoc.core.exception.HttpdocRuntimeException;
import io.httpdoc.core.interpretation.*;
import io.httpdoc.core.kit.IOKit;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Spring Boot 源码解释器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-08-14 15:55
 **/
public class SpringBootInterpreter extends SourceInterpreter {
    // ${java.io.tmpdir}/httpdoc/${UUID}
    private final File directory = new File(""
            + System.getProperty("java.io.tmpdir")
            + File.separator
            + "httpdoc"
            + File.separator
            + UUID.randomUUID()
    );
    private volatile boolean extracted = false;

    {
        if (!directory.exists() && !directory.mkdirs()) throw new HttpdocRuntimeException("could not create directory: " + directory);
    }

    @Override
    public ClassInterpretation interpret(Class<?> clazz) {
        extract();
        return super.interpret(clazz);
    }

    @Override
    public MethodInterpretation interpret(Method method) {
        extract();
        return super.interpret(method);
    }

    @Override
    public FieldInterpretation interpret(Field field) {
        extract();
        return super.interpret(field);
    }

    @Override
    public EnumInterpretation interpret(Enum<?> constant) {
        extract();
        return super.interpret(constant);
    }

    @Override
    public Interpretation interpret(PropertyDescriptor descriptor) {
        extract();
        return super.interpret(descriptor);
    }

    private void extract() {
        if (extracted) return;
        String classpath = System.getProperty("java.class.path");
        // 判断当前是以JAR包方式启动还是在IDE直接启动，如果classpath包含多个JAR则是在IDE中启动，否则是以单个JAR包启动
        String[] jarLocations = classpath.split(";");
        // IDE启动
        if (jarLocations.length > 1) {
            extracted = true;
        }
        // JAR启动
        else try {
            // 扫描出包含源码的jar包
            List<JarFile> jars = scan();
            // 将源码从jar包中提取出来
            extract(jars);
            System.setProperty("httpdoc.src.path", directory.getPath());
            System.setProperty("httpdoc.lib.path", System.getProperty("user.dir") + File.separator + System.getProperty("java.class.path"));
        } catch (Exception e) {
            throw new HttpdocRuntimeException(e);
        } finally {
            extracted = true;
        }
    }

    private void extract(List<JarFile> jars) throws IOException {
        for (JarFile jar : jars) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries != null && entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (entry.isDirectory() || !name.endsWith(".java")) continue;
                InputStream in = jar.getInputStream(entry);
                File file = new File(directory, name);
                File folder = file.getParentFile();
                if (!folder.exists() && !folder.mkdirs()) throw new IOException("could not create directory: " + folder);
                IOKit.transfer(in, file);
                IOKit.close(in);
            }
        }
    }

    /**
     * 扫描出包含源码的jar包
     *
     * @return 包含源码的jar包
     * @throws IOException IO 异常
     */
    private List<JarFile> scan() throws IOException {
        List<JarFile> jars = new ArrayList<>();
        // 源码所在包名
        String pkgs = System.getProperty("httpdoc.pkg.path", "");
        // 类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 找到所有包含源码的jar包
        String[] packages = pkgs.split("[,; \r\n\t]+");
        for (String pkg : packages) {
            String name = "/" + pkg.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(name);
            while (resources != null && resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String[] levels = resource.getFile().split("!");
                StringBuilder builder = new StringBuilder("jar:");
                for (int j = 0; j < levels.length - 1; j++) {
                    if (j > 0) builder.append("!");
                    builder.append(levels[j]);
                }
                String path = builder.toString();
                // 在jar包里面的jar包
                if (path.endsWith(".jar")) {
                    URL url = new URL(path);
                    File file = File.createTempFile("httpdoc-", ".jar", directory);
                    IOKit.transfer(url.openStream(), file);
                    JarFile jar = new JarFile(file);
                    jars.add(jar);
                }
                // 就在最外层jar包
                else {
                    URL url = new URL(levels[0]);
                    File file = new File(url.getFile());
                    JarFile jar = new JarFile(file);
                    jars.add(jar);
                }
            }
        }
        return jars;
    }

}
