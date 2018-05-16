package io.httpdoc.retrofit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

import java.io.File;
import java.io.IOException;

public class FileRequestBodyConverter implements Converter<File, RequestBody> {

    @Override
    public RequestBody convert(File file) throws IOException {
        return RequestBody.create(MediaType.parse("application/octet-stream"), file);
    }

}
