package com.minhdev.project.util;

import com.minhdev.project.domain.RestResponse;
import com.minhdev.project.util.annotation.ApiMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {
    private final HttpServletResponse httpServletResponse;

    public FormatRestResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        int status = httpServletResponse.getStatus();

        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(status);

        if (status >= 400) {
            return body;
        } else {
            restResponse.setData(body);
            ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
            restResponse.setMessage(message != null ? message.value() : "Success");
        }
        return restResponse;
    }
}
