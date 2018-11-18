package io.httpdoc.web;

import io.httpdoc.core.kit.IOKit;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;
import java.util.Stack;

public class ZipKit {

    public static void pack(String source, String target) throws IOException {
        pack(new File(source), new File(target));
    }

    public static void pack(File source, File target) throws IOException {
        try (OutputStream out = new FileOutputStream(target)) {
            pack(source, out);
        }
    }

    public static void pack(File source, OutputStream out) throws IOException {
        if (!source.exists()) {
            throw new FileNotFoundException("file not found: " + source.getAbsolutePath());
        }

        ZipArchiveOutputStream zipArchiveOutputStream = null;
        try {
            zipArchiveOutputStream = new ZipArchiveOutputStream(out);

            Stack<String> parent = new Stack<>();
            parent.push(null);
            Stack<File> file = new Stack<>();
            file.push(source);

            while (!file.isEmpty()) {
                File f = file.pop();
                String p = parent.pop();

                if (f.isDirectory()) {
                    File[] children = f.listFiles();
                    for (int i = 0; children != null && i < children.length; i++) {
                        File child = children[i];
                        parent.push(p == null ? f.getName() : p + File.separator + f.getName());
                        file.push(child);
                    }
                } else {
                    ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(f, p == null ? f.getName() : p + File.separator + f.getName());
                    zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(f);
                        IOKit.transfer(fis, zipArchiveOutputStream);
                        zipArchiveOutputStream.closeArchiveEntry();
                    } finally {
                        IOKit.close(fis);
                    }
                }
            }
        } finally {
            IOKit.close(zipArchiveOutputStream);
        }
    }

    public static void unpack(String source, String target) throws IOException {
        unpack(new File(source), new File(target));
    }

    public static void unpack(File source, File target) throws IOException {
        if (!source.exists()) {
            throw new FileNotFoundException("file not found: " + source.getAbsolutePath());
        }
        ZipArchiveInputStream zipArchiveInputStream = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            zipArchiveInputStream = new ZipArchiveInputStream(fileInputStream);
            ZipArchiveEntry zipArchiveEntry;
            while ((zipArchiveEntry = zipArchiveInputStream.getNextZipEntry()) != null) {
                if (zipArchiveEntry.isDirectory()) {
                    File directory = new File(target, zipArchiveEntry.getName());
                    if (!directory.exists() && !directory.mkdirs()) {
                        throw new IOException("could not make directory: " + directory);
                    }
                    continue;
                }
                FileOutputStream fileOutputStream = null;
                try {
                    File file = new File(target, zipArchiveEntry.getName());
                    if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                        throw new IOException("could not make directory: " + file.getParentFile());
                    }
                    fileOutputStream = new FileOutputStream(file);
                    IOKit.transfer(zipArchiveInputStream, fileOutputStream);
                } finally {
                    IOKit.close(fileOutputStream);
                }
            }
        } finally {
            IOKit.close(zipArchiveInputStream);
            IOKit.close(fileInputStream);
        }
    }

}