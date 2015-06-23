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
package com.harmony.umbrella.data.bond;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.Assert;

import com.harmony.umbrella.data.query.QueryUtils;
import com.harmony.umbrella.util.StringUtils;

/**
 * @author wuxii@foxmail.com
 */

public abstract class JunctionBond implements Bond {

    private static final long serialVersionUID = 2484162931403228104L;

    protected final Operator operator;
    protected final List<Bond> bonds = new ArrayList<Bond>();

    public JunctionBond(Operator operator, List<Bond> bonds) {
        Assert.notNull(operator, "operator must not be null");
        this.operator = operator;
        if (bonds != null)
            this.bonds.addAll(bonds);
    }

    @Override
    @Deprecated
    public String getName() {
        return isInline() ? "1" : null;
    }

    @Override
    @Deprecated
    public Object getValue() {
        return isInline() ? "1" : null;
    }

    @Override
    @Deprecated
    public Link getLink() {
        return isInline() ? Link.EQUAL : null;
    }

    @Override
    public boolean isInline() {
        return bonds.isEmpty();
    }

    public Iterable<Bond> getBonds() {
        return bonds;
    }

    public boolean isConjunction() {
        return operator == Operator.OR;
    }

    public boolean isDisjunction() {
        return operator == Operator.AND;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String toSQL() {
        return toSQL("");
    }

    @Override
    public String toSQL(String tableAlias) {
        if (bonds.isEmpty()) {
            return QueryUtils.trueCondition();
        }

        if (bonds.size() == 1) {
            return bonds.get(0).toSQL(tableAlias);
        }

        Iterator<Bond> it = bonds.iterator();

        StringBuilder buf = new StringBuilder(isOr() ? "(" : "");
        while (it.hasNext()) {
            buf.append(it.next().toSQL(tableAlias));
            if (it.hasNext()) {
                buf.append(" ").append(operator.desc()).append(" ");
            }
        }
        return buf.append(isOr() ? ")" : "").toString();
    }

    public String toXQL(String tableAlias, AliasGenerator aliasGen) {

        if (bonds.isEmpty()) {
            return QueryUtils.trueCondition();
        }

        if (bonds.size() == 1) {
            return parseXQL(bonds.get(0), tableAlias, aliasGen);
        }

        StringBuilder buf = new StringBuilder(isOr() ? "(" : "");

        Iterator<Bond> it = bonds.iterator();

        while (it.hasNext()) {

            buf.append(parseXQL(it.next(), tableAlias, aliasGen));

            if (it.hasNext()) {
                buf.append(" ").append(operator.desc()).append(" ");
            }

        }

        // BUG FIX for hibernate multi value if param :id) end with ')' not
        // append '(' ')'
        if (isOr() && isEndWithMultiValue()) {
            buf.append(" or ").append(QueryUtils.falseCondition());
        }

        return buf.append(isOr() ? ")" : "").toString();
    }

    /**
     * if last one bond of {@linkplain JunctionBond#getBonds()}, value is array
     * or collection, return true
     */
    private boolean isEndWithMultiValue() {
        if (bonds.isEmpty())
            return false;
        Object endValue = bonds.get(bonds.size() - 1).getValue();
        return endValue.getClass().isArray() || endValue instanceof Collection;
    }

    /**
     * if instance of {@link JunctionBond} iterator it to parseXQL
     */
    private String parseXQL(Bond bond, String tableAlias, AliasGenerator aliasGen) {
        if (bond instanceof JunctionBond) {
            return ((JunctionBond) bond).toXQL(tableAlias, aliasGen);
        }
        return bond.toXQL(tableAlias, aliasGen.generateAlias(bond));
    }

    @Override
    public String toXQL(String nameAlias) {
        return toXQL("", nameAlias);
    }

    @Override
    public String toXQL(String tableAlias, final String nameAlias) {
        return toXQL(tableAlias, new AliasGenerator() {
            int i = 0;

            @Override
            public String generateAlias(Bond bond) {
                return nameAlias + (StringUtils.isBlank(bond.getName()) ? "" : "_" + bond.getName().replace(".", "")) + "_" + i++;
            }

        });
    }

    public boolean isAnd() {
        return Operator.AND == operator;
    }

    public boolean isOr() {
        return Operator.OR == operator;
    }

    @Override
    public String toString() {
        return toSQL();
    }

    /**
     * and or
     * 
     * @author wuxii@foxmail.com
     */
    public enum Operator {

        AND("and") {
            @Override
            protected Operator negated() {
                return OR;
            }
        },
        OR("or") {
            @Override
            protected Operator negated() {
                return AND;
            }
        };

        private String desc;

        private Operator(String desc) {
            this.desc = desc;
        }

        /**
         * {@linkplain Operator}的字符描述
         */
        public String desc() {
            return desc;
        }

        /**
         * 对当前的{@linkplain Operator}取反
         */
        protected abstract Operator negated();

    }

    /**
     * 别名生成
     * 
     * @author wuxii@foxmail.com
     */
    public interface AliasGenerator {

        /**
         * 根据{@linkplain Bond}生成一个别名
         */
        String generateAlias(Bond bond);

    }

}
