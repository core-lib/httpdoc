package io.httpdoc.core.kit;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 配置加载器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 13:55
 **/
public class Loader {

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
                String file = url.getFile();
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
            } else {
                throw new IOException("unknown protocol " + url.getProtocol());
            }
        }
        return urls;
    }

}
