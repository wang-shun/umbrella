package com.harmony.umbrella.log;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.harmony.umbrella.log.Level.StandardLevel;

/**
 * 统一日志消息
 *
 * @author wuxii@foxmail.com
 */
public class LogMessage {

    public static final Level DEFAULT_LEVEL = Level.INFO;

    public static final String LOGMESSAGE_FQNC = LogMessage.class.getName();

    private Log log;
    private MessageFactory messageFactory;

    private String module;
    private String action;

    private Object key;

    private Message message;
    private Throwable throwable;
    private Level level;

    private String operatorName;
    private Object operatorId;
    private String operatorHost;

    private Object result;

    private long startTime = -1;
    private long finishTime = -1;

    private String stack;
    private String threadName;

    private Map<String, Object> context;

    public LogMessage(Log log) {
        this.log = log;
        this.messageFactory = log.getMessageFactory();
    }

    public static LogMessage create(Log log) {
        return new LogMessage(log);
    }

    /**
     * 设置业务数据的id
     *
     * @param key
     *            业务数据的id
     * @return current logMessage
     */
    public LogMessage key(Object key) {
        this.key = key;
        return this;
    }

    public LogMessage message(String message) {
        this.message = messageFactory.newMessage(message);
        return this;
    }

    public LogMessage message(String message, Object... args) {
        this.message = messageFactory.newMessage(message, args);
        return this;
    }

    public LogMessage message(Message message) {
        this.message = message;
        return this;
    }

    /**
     * 设置日志所属于的模块
     *
     * @param module
     * @return
     */
    public LogMessage module(String module) {
        this.module = module;
        return this;
    }

    /**
     * 设置日志所表示的动作
     *
     * @param action
     *            日志表示的动作
     * @return current logMessage
     */
    public LogMessage action(String action) {
        this.action = action;
        return this;
    }

    /**
     * 设置结果
     *
     * @param result
     *            结果
     * @return current logMessage
     */
    public LogMessage result(Object result) {
        this.result = result;
        return this;
    }

    public LogMessage exception(Throwable exception) {
        this.throwable = exception;
        return this;
    }

    /**
     * 设置业务数据的操作人
     *
     * @param username
     *            操作人名称
     * @return current logMessage
     */
    public LogMessage operatorName(String username) {
        this.operatorName = username;
        return this;
    }

    public LogMessage operatorId(Object operatorId) {
        this.operatorId = operatorId;
        return this;
    }

    public LogMessage operatorHost(String address) {
        this.operatorHost = address;
        return this;
    }

    /**
     * 设置开始时间 startTime = Calendar.getInstance();
     *
     * @return current logMessage
     */
    public LogMessage start() {
        this.startTime = System.currentTimeMillis();
        return this;
    }

    public LogMessage start(long startTime) {
        this.startTime = startTime;
        return this;
    }

    /**
     * 设置开始时间
     *
     * @param startTime
     *            开始时间
     * @return current logMessage
     */
    public LogMessage start(Calendar startTime) {
        this.startTime = startTime.getTimeInMillis();
        return this;
    }

    public LogMessage start(Date startTime) {
        this.startTime = startTime.getTime();
        return this;
    }

    /**
     * 设置结束时间 finishTime = Calendar.getInstance();
     *
     * @return current logMessage
     */
    public LogMessage finish() {
        this.finishTime = System.currentTimeMillis();
        return this;
    }

    public LogMessage finish(long finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    /**
     * 设置结束时间
     *
     * @param finishTime
     *            结束时间
     * @return current logMessage
     */
    public LogMessage finish(Calendar finishTime) {
        this.finishTime = finishTime.getTimeInMillis();
        return this;
    }

    public LogMessage finish(Date finishTime) {
        this.finishTime = finishTime.getTime();
        return this;
    }

    /**
     * 用于收集其他扩展属性
     * <p>
     * 当要记录http相关信息时使用
     *
     * @param key
     * @param value
     * @return
     */
    public LogMessage put(String key, Object value) {
        if (this.context == null) {
            this.context = new HashMap<String, Object>();
        }
        this.context.put(key, value);
        return this;
    }

    /**
     * 设置日志级别
     *
     * @param level
     *            日志级别
     * @return current logMessage
     */
    public LogMessage level(Level level) {
        this.level = level;
        return this;
    }

    public LogMessage level(StandardLevel level) {
        this.level = Level.toLevel(level.name());
        return this;
    }

    public LogMessage currentStack() {
        this.stack = Logs.fullyQualifiedClassName(LogMessage.LOGMESSAGE_FQNC, 1);
        return this;
    }

    public LogMessage currentThread() {
        this.threadName = Thread.currentThread().getName();
        return this;
    }

    public LogMessage stack(String stack) {
        this.stack = stack;
        return this;
    }

    public LogMessage threadName(String threadName) {
        this.threadName = threadName;
        return this;
    }

    /**
     * 调用日志log记录本条日志
     */
    public void log() {
        log(level == null ? DEFAULT_LEVEL : level);
    }

    /**
     * 调用日志log记录本条日志
     *
     * @param level
     *            日志级别
     */
    public void log(Level level) {
        this.level = level;

        Log relative = log.relative(LOGMESSAGE_FQNC);
        LogInfo msg = asInfo();

        switch (level.standardLevel) {
        case ERROR:
            relative.error(msg);
            break;
        case WARN:
            relative.warn(msg);
            break;
        case INFO:
            relative.info(msg);
            break;
        case DEBUG:
            relative.debug(msg);
            break;
        case ALL:
        case TRACE:
            relative.trace(msg);
            break;
        case OFF:
            break;
        default:
            break;
        }
    }

    public LogInfo asInfo() {
        return new LogInfoImpl(this);
    }

    static final class LogInfoImpl implements LogInfo {

        private static final long serialVersionUID = 1L;

        private String module;
        private String action;
        private String message;
        private Throwable throwable;
        private StandardLevel level;
        private Object result;
        private long requestTime;
        private long responseTime;
        private String operatorName;
        private Object operatorId;
        private String operatorHost;

        private String stackLocation;
        private String threadName;

        private Object key;

        private Map<String, Object> contextMap;

        LogInfoImpl(LogMessage m) {
            this.module = m.module;
            this.action = m.action;
            this.message = m.message == null ? null : m.message.getFormattedMessage();
            this.throwable = m.throwable;
            this.level = m.level == null ? null : m.level.standardLevel;
            this.result = m.result;
            this.requestTime = m.startTime;
            this.responseTime = m.finishTime;
            this.operatorName = m.operatorName;
            this.operatorId = m.operatorId;
            this.operatorHost = m.operatorHost;
            this.key = m.key;
            this.stackLocation = m.stack;
            this.threadName = m.threadName;
            this.contextMap = m.context;
        }

        @Override
        public String getModule() {
            return module;
        }

        @Override
        public String getAction() {
            return action;
        }

        @Override
        public Object getKey() {
            return key;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public Object getResult() {
            return result;
        }

        @Override
        public Date getRequestTime() {
            return requestTime <= 0 ? null : new Date(requestTime);
        }

        @Override
        public Date getResponseTime() {
            return responseTime <= 0 ? null : new Date(responseTime);
        }

        @Override
        public Throwable getThrowable() {
            return throwable;
        }

        @Override
        public StandardLevel getLevel() {
            return level;
        }

        @Override
        public String getOperatorName() {
            return operatorName;
        }

        @Override
        public Object getOperatorId() {
            return operatorId;
        }

        @Override
        public String getOperatorHost() {
            return operatorHost;
        }

        @Override
        public String getStackLocation() {
            return stackLocation;
        }

        @Override
        public String getThreadName() {
            return threadName;
        }

        @Override
        public Map<String, Object> getContext() {
            return Collections.unmodifiableMap(contextMap);
        }

        @Override
        public String toString() {
            StringBuilder out = new StringBuilder();
            out.append("{\n        module : ").append(module)//
                    .append("\n        action : ").append(action)//
                    .append("\n           key : ").append(key)//
                    .append("\n       message : ").append(message)//
                    .append("\n         level : ").append(level)//
                    .append("\n  operatorName : ").append(operatorName)//
                    .append("\n  operatorHost : ").append(operatorHost)//
                    .append("\n        result : ").append(result)//
                    .append("\n         stack : ").append(stackLocation)//
                    .append("\n           use : ").append(interval(requestTime, responseTime))//
                    .append("\n}");
            return out.toString();
        }
    }

    private static long interval(long start, long end) {
        return (start == -1 || end == -1) ? -1 : end - start;
    }

}
