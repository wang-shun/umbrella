package com.harmony.umbrella.log.spi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.springframework.util.Assert;

import com.harmony.umbrella.log.AbstractLog;
import com.harmony.umbrella.log.Level;
import com.harmony.umbrella.log.Log;
import com.harmony.umbrella.log.LogInfo;
import com.harmony.umbrella.log.LogProvider;
import com.harmony.umbrella.log.Message;

/**
 * 日志的log4j实现
 * 
 * @author wuxii@foxmail.com
 */
public class Log4j2LogProvider implements LogProvider {

    @Override
    public Log getLogger(String className) {
        return new Log4j2Log(className);
    }

    private final static class Log4j2Log extends AbstractLog {

        private ExtendedLogger logger;
        private final String caller;

        public Log4j2Log(String name) {
            this(name, FQCN);
        }

        private Log4j2Log(String name, String caller) {
            super(name);
            this.logger = (ExtendedLogger) LogManager.getLogger(name);
            this.caller = caller;
        }

        @Override
        public boolean isEnabled(Level level) {
            return logger.isEnabled(convert(level));
        }

        @Override
        protected Log relative(Object caller) {
            Assert.notNull(caller, "relative properties is null");
            return this.caller.equals(caller) ? this : new Log4j2Log(getName(), (String) caller);
        }

        @Override
        protected void logMessage(Level level, Message message, Throwable t) {
            logger.logIfEnabled(caller, convert(level), null, message.getFormattedMessage(), t);
        }

        @Override
        protected void logMessage(Level level, LogInfo logInfo) {
            logger.logIfEnabled(caller, convert(level), null, logInfo, logInfo.getThrowable());
        }

        private org.apache.logging.log4j.Level convert(Level level) {
            if (level != null) {
                switch (level.getStandardLevel()) {
                case ALL:
                    return org.apache.logging.log4j.Level.ALL;
                case DEBUG:
                    return org.apache.logging.log4j.Level.DEBUG;
                case ERROR:
                    return org.apache.logging.log4j.Level.ERROR;
                case INFO:
                    return org.apache.logging.log4j.Level.INFO;
                case OFF:
                    return org.apache.logging.log4j.Level.OFF;
                case TRACE:
                    return org.apache.logging.log4j.Level.TRACE;
                case WARN:
                    return org.apache.logging.log4j.Level.WARN;
                }
            }
            return org.apache.logging.log4j.Level.INFO;
        }

    }
}
