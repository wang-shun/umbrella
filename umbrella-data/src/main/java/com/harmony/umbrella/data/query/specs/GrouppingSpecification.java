package com.harmony.umbrella.data.query.specs;

import java.io.Serializable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.harmony.umbrella.data.query.QueryResult.Selections;

/**
 * 
 * @author wuxii@foxmail.com
 */
public class GrouppingSpecification<T> implements Specification<T>, Serializable {

    private static final long serialVersionUID = 5937892268297805525L;
    private Selections<T> selections;

    public GrouppingSpecification(Selections<T> selections) {
        this.selections = selections;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        query.groupBy(selections.selection(root, cb));
        return null;
    }

}