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
package com.harmony.umbrella.jaxws.cxf.interceptor;

import java.lang.reflect.Method;

import org.apache.cxf.message.Message;

import com.harmony.umbrella.jaxws.Phase;

public class HandleInInterceptor extends AbstractHandleInterceptor {

	// private static final Logger log =
	// LoggerFactory.getLogger(HandleInInterceptor.class);

	public HandleInInterceptor() {
		// super(POST_UNMARSHAL);
		super(Phase.PRE_INVOKE.render());
	}

	@Override
	protected void handleServerValidation(Message message, Object resourceInstance, Method method, Object[] args) {
		// try {
		// HandleMethodInvoker[] hms = finder.findHandleMethods(method, getJaxWsPhase());
		// for (HandleMethodInvoker hm : hms) {
		// System.out.println(hm);
		// }
		// } finally {
		// PREVIOUS_SERVER_MESSAGE.set(message);
		// }
	}

	@Override
	protected void handleProxyValidation(Message message, Method method, Object[] responseArgs) {
		// try {
		// Message requestMessage = PREVIOUS_PROXY_MESSAGE.get();
		// if (requestMessage == null) {
		// log.warn("please add handle out interceptor");
		// return;
		// }
		// final Object[] requestArgs = resolverArguments(requestMessage);
		// Method[] handlMethods = finder.findHandlerMethod(method, Phase.POST_INVOKE);
		// List<Object> argList = new ArrayList<Object>();
		// Collections.addAll(argList, responseArgs);
		// Collections.addAll(argList, requestArgs);
		// for (Method m : handlMethods) {
		// Object target = beanLoader.loadBean(m.getDeclaringClass());
		// try {
		// invoker.invok(target, m, argList.toArray());
		// } catch (InvokException e) {
		// log.error("", e);
		// }
		// }
		// } finally {
		// PREVIOUS_PROXY_MESSAGE.set(null);
		// }
	}

}