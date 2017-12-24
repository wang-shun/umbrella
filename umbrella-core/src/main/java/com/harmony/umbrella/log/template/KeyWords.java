package com.harmony.umbrella.log.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.harmony.umbrella.log.annotation.Logging.Scope;

/**
 * @author wuxii@foxmail.com
 */
public class KeyWords {

    static final List<KeyWord> allKeyWords;

    static {
        List<KeyWord> keyWords = new ArrayList<>();
        Collections.addAll(keyWords, TargetKeyWord.values());
        Collections.addAll(keyWords, HttpKeyWord.values());
        allKeyWords = Collections.unmodifiableList(keyWords);
    }

    public static KeyWord find(String keyWord) {
        return find(keyWord, true);
    }

    static KeyWord find(String name, boolean fullMatch) {
        for (KeyWord kw : allKeyWords) {
            String[] aliases = kw.alias();
            for (String alias : aliases) {
                if (fullMatch ? name.equals(alias) : name.startsWith(alias)) {
                    return kw;
                }
            }
        }
        return null;
    }

    /**
     * 设计被监控对象的关键字
     * 
     * @author wuxii@foxmail.com
     */
    public enum TargetKeyWord implements KeyWord {

        TARGET(Arrays.asList(Scope.IN, Scope.OUT), "target", "$") {

            @Override
            public Object resolve(LoggingContext context) {
                return context.getTarget();
            }

        }, //
        ARGS(Arrays.asList(Scope.IN, Scope.OUT), "args", "arg") {

            @Override
            public Object resolve(LoggingContext context) {
                return context.getArguments();
            }
        }, //
        RESULT(Arrays.asList(Scope.OUT), "result") {

            @Override
            public Object resolve(LoggingContext context) {
                return context.getResult();
            }

        }, //
        EXCEPTION(Arrays.asList(Scope.OUT), "exception", "ex") {

            @Override
            public Object resolve(LoggingContext context) {
                return context.getException();
            }

        };

        private String[] alias;
        private Set<Scope> scopes;

        private TargetKeyWord(Collection<Scope> scopes, String... alias) {
            this.alias = alias;
            this.scopes = Collections.unmodifiableSet(new HashSet<>(scopes));
        }

        @Override
        public String[] alias() {
            String[] dest = new String[alias.length];
            System.arraycopy(alias, 0, dest, 0, alias.length);
            return dest;
        }

        @Override
        public Set<Scope> scopes() {
            return scopes;
        }

    }

    /**
     * 与http属性相关的关键字
     * 
     * @author wuxii@foxmail.com
     */
    public enum HttpKeyWord implements KeyWord {

        PARAMETER(Arrays.asList(Scope.IN), "parameter", "param") {

            @Override
            public Object resolve(LoggingContext context) {
                return new ParameterMap(context.getHttpRequest());
            }
        }, //
        REQUEST(Arrays.asList(Scope.IN, Scope.OUT), "req", "request", "attr") {

            @Override
            public Object resolve(LoggingContext context) {
                return new RequestAttributeMap(context.getHttpRequest());
            }
        }, //
        RESPONSE(Arrays.asList(Scope.IN, Scope.OUT), "resp", "response") {

            @Override
            public Object resolve(LoggingContext context) {
                return context.getHttpResponse();
            }
        }, //
        SESSION(Arrays.asList(Scope.IN, Scope.OUT), "session") {

            @Override
            public Object resolve(LoggingContext context) {
                return new SessionAttributeMap(context.getHttpSession());
            }
        }, //
        HEADER(Arrays.asList(Scope.IN, Scope.OUT), "header") {

            @Override
            public Object resolve(LoggingContext context) {
                return new HeaderMap(context.getHttpRequest());
            }
        };

        private String[] alias;
        private Set<Scope> scopes;

        private HttpKeyWord(Collection<Scope> scopes, String... alias) {
            this.alias = alias;
            this.scopes = Collections.unmodifiableSet(new HashSet<>(scopes));
        }

        @Override
        public String[] alias() {
            String[] dest = new String[alias.length];
            System.arraycopy(alias, 0, dest, 0, alias.length);
            return dest;
        }

        @Override
        public Set<Scope> scopes() {
            return scopes;
        }
    }

    public static class ParameterMap extends HttpMember {

        private HttpServletRequest request;

        ParameterMap(HttpServletRequest request) {
            this.request = request;
        }

        public String get(String name) {
            return request.getParameter(name);
        }

    }

    public static class HeaderMap {

        private HttpServletRequest request;

        HeaderMap(HttpServletRequest request) {
            this.request = request;
        }

        public String get(String name) {
            return request.getHeader(name);
        }

    }

    public static class RequestAttributeMap {

        private HttpServletRequest request;

        RequestAttributeMap(HttpServletRequest request) {
            this.request = request;
        }

        public Object get(String name) {
            return request.getAttribute(name);
        }

    }

    public static class SessionAttributeMap {

        private HttpSession session;

        SessionAttributeMap(HttpSession session) {
            this.session = session;
        }

        public Object get(String name) {
            return session.getAttribute(name);
        }
    }

}
