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
import java.util.Enumeration;
import java.util.UUID;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Spring Boot 源码解释器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-08-14 15:55
 **/
public class SpringBootInterpreter extends SourceInterpreter {
    private static volatile boolean prepared = false;

    private synchronized static void prepare() {
        if (prepared) return;
        else prepared = true;

        String classpath = System.getProperty("java.class.path");
        // 判断当前是以JAR包方式启动还是在IDE直接启动，如果classpath包含多个JAR则是在IDE中启动，否则是以单个JAR包启动
        String[] jarLocations = classpath.split(";");
        // IDE启动
        if (jarLocations.length > 1) return;
        // JAR启动
        JarFile boot = null;
        try {
            File directory = new File(""
                    + System.getProperty("java.io.tmpdir")
                    + File.separator
                    + "httpdoc"
                    + File.separator
                    + UUID.randomUUID()
            );
            if (!directory.exists() && !directory.mkdirs()) throw new HttpdocRuntimeException("could not create directory: " + directory);
            directory.deleteOnExit();

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
                if (name.startsWith(clsLocation) && name.endsWith(".java")) extract(boot, jarEntry, directory.getPath());
                if (name.startsWith(libLocation) && name.endsWith(".jar")) extract(boot, jarEntry, directory.getPath());
            }

            String src = new File(directory, clsLocation).getPath();
            System.setProperty("httpdoc.src.path", src);

            File[] libs = new File(directory, libLocation).listFiles();
            StringBuilder lib = new StringBuilder();
            for (int i = 0; libs != null && i < libs.length; i++) {
                if (i > 0) lib.append(";");
                lib.append(libs[i].getPath());

                // 将其中的源码也提取到源码目录里面去
                JarFile jarFile = new JarFile(libs[i]);
                Enumeration<JarEntry> jarEntries = jarFile.entries();
                while (jarEntries != null && jarEntries.hasMoreElements()) {
                    JarEntry jarEntry = jarEntries.nextElement();
                    if (jarEntry.isDirectory()) continue;
                    String name = jarEntry.getName();
                    if (name.endsWith(".java")) extract(jarFile, jarEntry, src);
                }
                IOKit.close(jarFile);
            }
            System.setProperty("httpdoc.lib.path", lib.toString());
        } catch (Exception e) {
            throw new HttpdocRuntimeException(e);
        } finally {
            IOKit.close(boot);
        }
    }

    private static void extract(JarFile jarFile, JarEntry jarEntry, String directory) throws IOException {
        InputStream in = jarFile.getInputStream(jarEntry);
        String name = jarEntry.getName();
        File file = new File(directory, name);
        File parent = file.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) throw new IOException("could not create directory for " + parent);
        IOKit.transfer(in, file);
        IOKit.close(in);
        file.deleteOnExit();
    }

    @Override
    public ClassInterpretation interpret(Class<?> clazz) {
        if (!prepared) prepare();
        return super.interpret(clazz);
    }

    @Override
    public MethodInterpretation interpret(Method method) {
        if (!prepared) prepare();
        return super.interpret(method);
    }

    @Override
    public FieldInterpretation interpret(Field field) {
        if (!prepared) prepare();
        return super.interpret(field);
    }

    @Override
    public EnumInterpretation interpret(Enum<?> constant) {
        if (!prepared) prepare();
        return super.interpret(constant);
    }

    @Override
    public Interpretation interpret(PropertyDescriptor descriptor) {
        if (!prepared) prepare();
        return super.interpret(descriptor);
    }

}
