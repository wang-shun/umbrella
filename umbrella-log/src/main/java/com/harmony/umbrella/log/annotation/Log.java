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
package com.harmony.umbrella.log.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.harmony.umbrella.log.ErrorHandler;
import com.harmony.umbrella.log.Level.StandardLevel;

@Target({ METHOD })
@Retention(RUNTIME)
public @interface Log {

    /**
     * 模块
     */
    String module() default "";

    /**
     * 业务模块
     */
    String bizModule() default "";

    /**
     * 操作名称
     */
    String action() default "";

    /**
     * 日志级别
     */
    StandardLevel level() default StandardLevel.INFO;

    /**
     * id所在的位置或名称
     */
    String id() default "";

    /**
     * 日志消息
     * <p>
     * 可以通过模版的方式对消息日志进行装配
     */
    String message() default "";

    /**
     * 异常处理
     */
    Class<? extends ErrorHandler> errorHandler() default ErrorHandler.class;

}
