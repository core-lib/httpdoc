package io.httpdoc.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.File;
import java.io.IOException;

public class FileResponseBodyConverter implements Converter<ResponseBody, File> {

    @Override
    public File convert(ResponseBody value) throws IOException {
        return null;
    }

}
