/*
 * Copyright 2002-2015 the original author or authors.
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
package com.harmony.umbrella.monitor;

import java.lang.reflect.Method;

/**
 * 方法监控结果视图
 */
public interface MethodGraph extends Graph {

    String GRAPH_TYPE = "method";

    /**
     * 方法的执行目标
     *
     * @return target
     */
    Object getTarget();

    /**
     * 获取目标类
     *
     * @return target class
     */
    Class<?> getTargetClass();

    /**
     * 拦截的方法
     *
     * @return method
     */
    Method getMethod();

    /**
     * 拦截方法的请求参数
     *
     * @return 方法的参数
     */
    Object[] getParameters();

    /**
     * 方法的返回值
     *
     * @return 返回值
     */
    Object getResult();

}