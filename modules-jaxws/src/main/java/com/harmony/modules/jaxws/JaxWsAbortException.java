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
package com.harmony.modules.jaxws;

/**
 * 在执行前如果有异常则抛出取消异常
 * 
 * @author wuxii@foxmail.com
 */
public class JaxWsAbortException extends Exception {

    private static final long serialVersionUID = 1L;

    public JaxWsAbortException() {
        super();
    }

    public JaxWsAbortException(String message, Throwable cause) {
        super(message, cause);
    }

    public JaxWsAbortException(String message) {
        super(message);
    }

    public JaxWsAbortException(Throwable cause) {
        super(cause);
    }

}
