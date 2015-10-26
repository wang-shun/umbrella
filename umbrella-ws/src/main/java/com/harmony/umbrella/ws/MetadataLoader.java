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
package com.harmony.umbrella.ws;

/**
 * 
 * 加载服务的元数据信息
 * 
 * @author wuxii@foxmail.com
 */
public interface MetadataLoader {

    /**
     * 根据指定的serviceClass加载指定的元数据
     * 
     * @param serviceClass
     * @return {@linkplain Metadata}
     */
    Metadata loadMetadata(Class<?> serviceClass);

    /**
     * 根据指定的service类名加载元数据
     * 
     * @param serviceClassName
     *            类名
     * @return {@linkplain Metadata}
     */
    Metadata loadMetadata(String serviceClassName);

}
