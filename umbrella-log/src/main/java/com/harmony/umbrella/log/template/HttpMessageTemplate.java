/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.harmony.umbrella.log.template;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.harmony.umbrella.log.HttpTemplate;

/**
 * @author wuxii@foxmail.com
 */
public class HttpMessageTemplate extends MessageTemplate implements HttpTemplate {

    private final HttpServletRequest request;

    public HttpMessageTemplate(Method method, HttpServletRequest request) {
        super(method);
        this.request = request;
    }

    @Override
    protected Object wrapObject(Object target, Object result, Object[] arguments) {
        return new HttpHolder(target, result, arguments);
    }

    @Override
    public HttpServletRequest getHttpRequest() {
        return request;
    }

    protected class HttpHolder extends Holder {

        private static final long serialVersionUID = 1L;

        public HttpHolder(Object target, Object result, Object[] arguments) {
            super(target, result, arguments);
        }

        public HttpServletRequest getRequest() {
            return request;
        }

        public HttpSession getSession() {
            return request.getSession();
        }

        public ServletContext getApplication() {
            return request.getServletContext();
        }

    }

}