package com.harmony.umbrella.ws.ext;

import java.lang.reflect.Method;

import com.harmony.umbrella.log.Log;
import com.harmony.umbrella.log.Logs;

import com.harmony.umbrella.json.Json;
import com.harmony.umbrella.core.MethodGraph;
import com.harmony.umbrella.monitor.ext.LogUtils;
import com.harmony.umbrella.monitor.ext.LogUtils.GraphFormat;
import com.harmony.umbrella.util.Exceptions;
import com.harmony.umbrella.util.StringUtils;
import com.harmony.umbrella.ws.Context;
import com.harmony.umbrella.ws.ProxyExecutor;
import com.harmony.umbrella.ws.visitor.AbstractContextVisitor;

/**
 * webservice客户端同步部分的日志记录工具类
 * 
 * @author wuxii@foxmail.com
 */
public class HarmonyContextVisitor extends AbstractContextVisitor {

    private static final Log log = Logs.getLog(HarmonyContextVisitor.class);

    @Override
    public void visitFinally(Object result, Throwable throwable, final Context context) {
        MethodGraph graph = (MethodGraph) context.get(ProxyExecutor.WS_EXECUTION_GRAPH);

        if (graph != null) {

            LogUtils.log(graph, new GraphFormat<MethodGraph>() {

                @Override
                public String format(MethodGraph graph) {

                    StringBuilder buf = new StringBuilder();
                    try {

                        buf.append("接口名称:").append(StringUtils.getMethodId(context.getMethod())).append("\n");
                        buf.append("服务地址:").append(context.getAddress()).append("\n");

                        String username = context.getUsername();
                        if (username != null) {
                            buf.append("用户名称:").append(username).append("\n");
                        }

                        String password = context.getPassword();
                        if (password != null) {
                            buf.append("用户密码:").append(password).append("\n");
                        }

                        buf.append("详细参数:").append(LogUtils.parameterToJson(graph.getMethodArguments())).append("\n");
                        buf.append("返回结果:");
                        Method method = graph.getMethod();
                        if (method.getReturnType() == void.class) {
                            buf.append("(void)");
                        } else {
                            buf.append(Json.toJson(graph.getMethodResult()));
                        }
                        buf.append("\n");

                        buf.append("交互耗时:").append(graph.use()).append("ms\n");

                        if (graph.isException()) {
                            buf.append("异常信息:").append(Exceptions.getRootCause(graph.getException())).append("\n");
                        }

                    } catch (NoSuchMethodException e) {
                        log.warn("", e);
                    }

                    return buf.toString();
                }

            });

        }
    }

}