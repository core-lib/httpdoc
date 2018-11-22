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
        try (OutputStream outputStream = new FileOutputStream(target)) {
            pack(source, outputStream);
        }
    }

    public static void pack(File source, OutputStream out) throws IOException {
        try (ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(out)) {
            pack(source, zipArchiveOutputStream);
        }
    }

    public static void pack(File source, ZipArchiveOutputStream zipArchiveOutputStream) throws IOException {
        Stack<String> parents = new Stack<>();
        Stack<File> files = new Stack<>();

        if (source.exists()) {
            parents.push(null);
            files.push(source);
        }

        while (!files.isEmpty()) {
            File file = files.pop();
            String parent = parents.pop();

            if (file.isDirectory()) {
                File[] children = file.listFiles();
                for (int i = 0; children != null && i < children.length; i++) {
                    File child = children[i];
                    parents.push(parent == null ? file.getName() : parent + File.separator + file.getName());
                    files.push(child);
                }
            } else {
                ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, parent == null ? file.getName() : parent + File.separator + file.getName());
                zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(file);
                    IOKit.transfer(fileInputStream, zipArchiveOutputStream);
                    zipArchiveOutputStream.closeArchiveEntry();
                } finally {
                    IOKit.close(fileInputStream);
                }
            }
        }
    }

    public static void unpack(String source, String target) throws IOException {
        unpack(new File(source), new File(target));
    }

    public static void unpack(File source, File target) throws IOException {
        try (InputStream inputStream = new FileInputStream(source)) {
            unpack(inputStream, target);
        }
    }

    public static void unpack(InputStream in, File target) throws IOException {
        try (ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(in)) {
            unpack(zipArchiveInputStream, target);
        }
    }

    public static void unpack(ZipArchiveInputStream zipArchiveInputStream, File target) throws IOException {
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
    }

}