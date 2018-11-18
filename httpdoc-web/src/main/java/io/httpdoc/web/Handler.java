package io.httpdoc.web;

import io.httpdoc.core.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {

    void handle(Document document, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
