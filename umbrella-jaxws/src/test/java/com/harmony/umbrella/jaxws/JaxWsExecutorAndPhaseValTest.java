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
package com.harmony.umbrella.jaxws;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.harmony.umbrella.jaxws.impl.JaxWsCXFExecutor;
import com.harmony.umbrella.jaxws.impl.SimpleJaxWsContext;
import com.harmony.umbrella.jaxws.services.HelloService;
import com.harmony.umbrella.jaxws.services.HelloServiceImpl;

/**
 * @author wuxii@foxmail.com
 */
public class JaxWsExecutorAndPhaseValTest {

    private static final String address = "http://localhost:8081/hello";
    private static final JaxWsExecutor executor = new JaxWsCXFExecutor();

    @BeforeClass
    public static void setUp() {
        JaxWsServerBuilder.newServerBuilder().publish(HelloServiceImpl.class, address);
        // executor.addHandler(new JaxWsAnnotationHandler());
    }

    @Test
    public void testHelloServicePhaseVal() {
        SimpleJaxWsContext context = new SimpleJaxWsContext(HelloService.class, "sayHi", new Object[] { "wuxii" });
        context.setAddress(address);
        Object result = executor.execute(context);
        assertNotNull(result);
        assertEquals("Hi wuxii", result);
    }

}
