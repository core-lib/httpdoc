package io.httpdoc.core.kit;

import java.io.*;

public abstract class IOKit {

    public static void close(Closeable closeable) {
        try {
            close(closeable, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Closeable closeable, boolean quietly) throws IOException {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
            if (!quietly) throw e;
        }
    }

    public static <T extends Serializable> T clone(T source) throws IOException, ClassNotFoundException {
        if (source == null) throw new NullPointerException();
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(source);
            bais = new ByteArrayInputStream(baos.toByteArray());
            ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } finally {
            close(ois);
            close(bais);
            close(oos);
            close(baos);
        }
    }

}
