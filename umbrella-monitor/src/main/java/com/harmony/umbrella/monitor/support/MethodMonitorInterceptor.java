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
package com.harmony.umbrella.monitor.support;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.harmony.umbrella.monitor.AbstractMonitor;
import com.harmony.umbrella.monitor.GraphAnalyzer;
import com.harmony.umbrella.monitor.MethodMonitor;
import com.harmony.umbrella.monitor.ResourceMatcher;
import com.harmony.umbrella.monitor.graph.DefaultMethodGraph;
import com.harmony.umbrella.monitor.matcher.MethodExpressionMatcher;
import com.harmony.umbrella.util.Assert;

/**
 * 基于拦截器的实现，默认来接{@link MethodMonitor#DEFAULT_METHOD_PATTERN} 表达式的方法.
 * <p>
 * 当然要连接器能拦截到方法
 * 
 * @author wuxii@foxmail.com
 */
public abstract class MethodMonitorInterceptor<I> extends AbstractMonitor<Method> implements MethodMonitor {

    /**
     * 资源模版匹配工具
     */
    protected final Map<String, ResourceMatcher<Method>> matcherMap = new ConcurrentHashMap<String, ResourceMatcher<Method>>();
    /**
     * 监控结果分析工具, 不可为空
     */
    protected GraphAnalyzer<MethodGraph> analyzer;

    /**
     * 监控的方法
     * 
     * @param ctx
     * @return
     * @see {@linkplain javax.interceptor.InvocationContext#getMethod()}
     */
    protected abstract Method getMethod(I ctx);

    /**
     * 监控的目标
     * 
     * @param ctx
     * @return
     * @see {@linkplain javax.interceptor.InvocationContext#getTarget()}
     */
    protected abstract Object getTarget(I ctx);

    /**
     * 执行监控的目标方法
     * 
     * @param ctx
     * @return
     * @throws Exception
     * @see {@linkplain javax.interceptor.InvocationContext#proceed()}
     */
    protected abstract Object process(I ctx) throws Exception;

    /**
     * 监控的请求参数
     * 
     * @param ctx
     * @return
     * @see {@linkplain javax.interceptor.InvocationContext#getParameters()}
     */
    protected abstract Object[] getParameters(I ctx);

    /**
     * 监控的入口(拦截器的入口)
     * 
     * @param ctx
     * @return
     * @throws Exception
     * @see {@linkplain javax.interceptor.AroundInvoke}
     */
    protected Object monitor(I ctx) throws Exception {
        Method method = getMethod(ctx);
        if (!isMonitored(method)) {
            return process(ctx);
        }
        Object result = null;
        Object target = getTarget(ctx);
        Object[] parameters = getParameters(ctx);
        LOG.debug("interceptor method [{}] of [{}]", method, target);
        DefaultMethodGraph graph = new DefaultMethodGraph(target, method, parameters);
        try {
            graph.setRequestTime(Calendar.getInstance());
            // process
            result = process(ctx);
            //
            graph.setResponseTime(Calendar.getInstance());
            graph.setResult(result);
        } catch (Exception e) {
            graph.setException(e);
            throw e;
        } finally {
            analyzer.analyze(graph);
        }
        return result;
    }

    @Override
    protected ResourceMatcher<Method> createMatcher(String pattern) {
        if (matcherMap.containsKey(pattern)) {
            matcherMap.put(pattern, new MethodExpressionMatcher(pattern));
        }
        return matcherMap.get(pattern);
    }

    public GraphAnalyzer<MethodGraph> getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(GraphAnalyzer<MethodGraph> analyzer) {
        Assert.notNull(analyzer, "ananlyzer can't set to null");
        this.analyzer = analyzer;
    }

    @Override
    public void cleanAll() {
        super.cleanAll();
        matcherMap.clear();
    }

}
