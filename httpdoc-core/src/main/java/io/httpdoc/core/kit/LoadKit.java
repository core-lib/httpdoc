package io.httpdoc.core.kit;

import io.httpdoc.core.exception.HttpdocRuntimeException;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 配置加载器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 13:55
 **/
public class LoadKit {

    public static Set<URL> load(ClassLoader classLoader) throws IOException {
        Set<URL> urls = new LinkedHashSet<>();
        Enumeration<URL> enumeration = classLoader.getResources("httpdoc");
        enumeration = enumeration != null && enumeration.hasMoreElements() ? enumeration : null;
        while (enumeration != null && enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();
            if (url == null) {
                throw new NullPointerException();
            } else if (url.getProtocol().equalsIgnoreCase("file")) {
                File file = new File(url.getFile());
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (int i = 0; files != null && i < files.length; i++) {
                        File f = files[i];
                        if (f.isDirectory()) {
                            continue;
                        }
                        if (f.isFile() && f.getName().endsWith(".properties")) {
                            urls.add(f.toURI().toURL());
                        }
                    }
                }
            } else if (url.getProtocol().equalsIgnoreCase("jar")) {
                // 有可能是jar里面还包含jar
                String file = url.getFile();
                String[] paths = file.split("!");
                if (paths.length > 2) {
                    File jar = null;
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        StringBuilder path = new StringBuilder();
                        for (int i = 0; i < paths.length - 1; i++) {
                            if (i == 0) path.append("jar:");
                            else path.append("!");
                            path.append(paths[i]);
                        }
                        jar = File.createTempFile("httpdoc-", ".jar");
                        in = new URL(path.toString()).openStream();
                        out = new FileOutputStream(jar);
                        IOKit.transfer(in, out);
                        out.flush();
                        JarFile jarFile = new JarFile(jar);
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry jarEntry = entries.nextElement();
                            if (jarEntry.isDirectory()) {
                                continue;
                            }
                            String name = jarEntry.getName();
                            if (name.startsWith("httpdoc/") && name.endsWith(".properties")) {
                                urls.add(new URL(path + "!/" + jarEntry.getName()));
                            }
                        }
                    } finally {
                        IOKit.delete(jar);
                        IOKit.close(in);
                        IOKit.close(out);
                    }
                } else {
                    String path = file.substring(file.indexOf(":") + 1, file.lastIndexOf("!"));
                    JarFile jarFile = new JarFile(path);
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        if (jarEntry.isDirectory()) {
                            continue;
                        }
                        String name = jarEntry.getName();
                        if (name.startsWith("httpdoc/") && name.endsWith(".properties")) {
                            urls.add(new URL("jar:file:" + jarFile.getName() + "!/" + jarEntry.getName()));
                        }
                    }
                }
            } else {
                throw new IOException("unknown protocol " + url.getProtocol());
            }
        }
        return urls;
    }

    public static <T> Map<String, T> load(ClassLoader classLoader, Class<T> type) {
        Map<String, T> map = new LinkedHashMap<>();
        try {
            Set<URL> urls = LoadKit.load(classLoader);
            for (URL url : urls) {
                if (url.getFile().endsWith(".properties")) {
                    Properties properties = new Properties();
                    properties.load(url.openStream());
                    if (properties.isEmpty()) continue;
                    Enumeration<Object> keys = properties.keys();
                    while (keys.hasMoreElements()) {
                        String name = (String) keys.nextElement();
                        String value = (String) properties.get(name);
                        Class<? extends T> clazz = classForName(value, type);
                        if (clazz == null) continue;
                        T bean = clazz.newInstance();
                        map.put(name, bean);
                    }
                }
            }
        } catch (Exception e) {
            throw new HttpdocRuntimeException(e);
        }
        return map;
    }

    public static <T> Class<? extends T> classForName(String className, Class<T> superType) {
        try {
            Class<?> subType = Class.forName(className);
            if (superType.isAssignableFrom(subType)) return subType.asSubclass(superType);
            else return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
