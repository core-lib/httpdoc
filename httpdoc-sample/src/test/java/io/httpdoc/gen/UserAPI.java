package io.httpdoc.gen;

import io.httpdoc.spring.mvc.User;
import org.qfox.jestful.client.Message;
import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.core.http.Path;
import org.qfox.jestful.core.http.Query;

@HTTP("/users")
public interface UserAPI {

    @GET("/{id}")
    Message test(@Path("id") String id, @Query("*") User user, @Query("dateCreated") String dateCreated);

}
