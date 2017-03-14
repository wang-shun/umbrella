package com.harmony.umbrella.data.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.harmony.umbrella.data.Queryable;

/**
 * @deprecated spring-data repository or
 *             {@linkplain com.harmony.umbrella.data.repository.QueryableRepository}
 * @author wuxii@foxmail.com
 */
public interface EntityDAO<T> extends JpaDAO, DAO, Queryable<T> {

    int remove(Specification<T> spec);

    T removeById(Object ID);

    List<T> removeById(Object... ID);

    T findById(Object ID);

    T findOne(Specification<T> spec);

    List<T> findAll();

    List<T> findAll(Sort sort);

    List<T> findAll(Specification<T> spec);

    List<T> findAll(Specification<T> spec, Sort sort);

    Page<T> findAll(Pageable pageable, Specification<T> spec);

    long countAll();

    long count(Specification<T> spec);

}