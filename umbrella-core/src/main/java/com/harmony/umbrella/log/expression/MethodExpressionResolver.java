package com.harmony.umbrella.log.expression;

import java.lang.reflect.Method;

import com.harmony.umbrella.log.IllegalExpressionException;
import com.harmony.umbrella.log.util.ExpressionUtils;
import com.harmony.umbrella.util.ReflectionUtils;

/**
 * 表达式指定为一个get方法，通过value对应的get方法取值。
 * <p>
 * 表达式默认由#号开头, 全字母连接
 *
 * @author wuxii@foxmail.com
 */
public class MethodExpressionResolver extends ComparableExpressionResolver {

    protected static final MethodExpressionResolver INSTANCE = new MethodExpressionResolver();

    public MethodExpressionResolver() {
        super(METHOD);
    }

    @Override
    public boolean support(String expression, Object value) {
        return ExpressionUtils.isMethodExpression(expression);
    }

    @Override
    public Object resolve(String expression, Object value) throws IllegalExpressionException {
        if (support(expression, value)) {
            Class<?> targetClass = value.getClass();
            try {
                Method method = ReflectionUtils.findReadMethod(targetClass, expression.substring(1));
                return ReflectionUtils.invokeMethod(method, value);
            } catch (Exception e) {
                throw new IllegalExpressionException("unsupported method expression " + expression, e);
            }
        }
        throw new IllegalExpressionException("unsupported method expression " + expression);
    }

}
