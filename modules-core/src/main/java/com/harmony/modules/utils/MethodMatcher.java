/*
 * Copyright 2013-2015 wuxii@foxmail.com.
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
package com.harmony.modules.utils;

import static com.harmony.modules.utils.ClassUtils.*;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 方法匹配过滤. 匹配{@linkplain java.lang.Class#getMethods()}
 * 
 * @author wuxii@foxmail.com
 */
public class MethodMatcher {

	/**
	 * 根据方法名过滤出source中符合方法名的方法
	 * 
	 * @param source 目表类
	 * @param methodName 方法名
	 * @return 如果没有符合条件的方法则返回null
	 */
	public static Method[] filterMethod(Class<?> source, String methodName) {
		List<Method> result = new LinkedList<Method>();
		for (Method method : source.getMethods()) {
			if (Object.class == method.getDeclaringClass())
				continue;
			if (method.getName().equals(method)) {
				result.add(method);
			}
		}
		return result.toArray(new Method[result.size()]);
	}

	/**
	 * 根据参数类型过滤出source中符合参数类型的方法<p>过滤出的方法参数可能为期待的参数类型的父类，但绝不能为子类
	 * 
	 * @param source
	 * @param parameterTypes 期待的参数类型
	 * @return
	 */
	public static Method[] filterMethod(Class<?> source, Class<?>[] parameterTypes) {
		List<Method> result = new LinkedList<Method>();
		for (Method method : source.getMethods()) {
			if (Object.class == method.getDeclaringClass())
				continue;
			Class<?>[] types = method.getParameterTypes();
			if (types.length == parameterTypes.length) {
				int i, max;
				for (i = 0, max = types.length; i < max; i++) {
					if (!isAssignableIgnoreClassLoader(types[i], parameterTypes[i]))
						continue;
				}
				if (i == max)
					result.add(method);
			}
		}
		return result.toArray(new Method[result.size()]);
	}

	/**
	 * 过滤符合方法名称和参数类型的方法<p>过滤出的方法参数可能为期待的参数类型的父类，但绝不能为子类
	 * @param source
	 * @param methodName 期待方法名
	 * @param parameterTypes 期待参数类型
	 * @return
	 */
	public static Method filterMethod(Class<?> source, String methodName, Class<?>[] parameterTypes) {
		Method result = null;
		for (Method method : source.getMethods()) {
			if (Object.class == method.getDeclaringClass())
				continue;
			if (method.getName().equals(methodName)) {
				Class<?>[] types = method.getParameterTypes();
				if (types.length == parameterTypes.length) {
					int i, max;
					for (i = 0, max = types.length; i < max; i++) {
						if (!isAssignableIgnoreClassLoader(types[i], parameterTypes[i]))
							continue;
					}
					if (i == max) {
						result = method;
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 过滤符合方法名，参数类型，返回类型相同的方法.
	 * <p>过滤出的方法参数可能为期待的参数类型的父类，但绝不能为子类
	 * @param source
	 * @param methodName 期待的方法名
	 * @param parameterTypes 期待的参数类型
	 * @param returnType 期待的返回类型
	 * @return
	 */
	public static Method filterMethod(Class<?> source, String methodName, Class<?>[] parameterTypes, Class<?> returnType) {
		Method result = null;
		for (Method method : source.getMethods()) {
			if (Object.class == method.getDeclaringClass())
				continue;
			if (method.getName().equals(methodName)) {
				Class<?>[] types = method.getParameterTypes();
				if (types.length == parameterTypes.length) {
					int i, max;
					for (i = 0, max = types.length; i < max; i++) {
						if (!isAssignableIgnoreClassLoader(types[i], parameterTypes[i]))
							continue;
					}
					if (i == max && isAssignableIgnoreClassLoader(returnType, method.getReturnType())) {
						result = method;
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 过滤目标类中符合期待方法的方法
	 * <p>符合的方法主要为：
	 * <ul>
	 * 	<li>参数相同</li>
	 *  <li>参数相同并且方法名相同</li>
	 * </ul>
	 * @param source
	 * @param exceptMethod
	 * @return
	 */
	public static Method[] filterMethod(Class<?> source, Method exceptMethod) {
		return filterMethod(source, exceptMethod.getParameterTypes());
	}

	/**
	 * 检测m2的参数是否符合m1
	 * @param m1
	 * @param m2
	 * @return
	 */
	public static boolean parameterTypeMatchers(Method m1, Method m2) {
		if (m1 == m2)
			return true;
		return typeEquals(m1.getParameterTypes(), m2.getParameterTypes());
	}

	public interface MethodFilter {

		boolean accept(Method method);

	}

}
