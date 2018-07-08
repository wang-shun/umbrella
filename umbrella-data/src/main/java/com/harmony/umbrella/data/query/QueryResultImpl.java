package com.harmony.umbrella.data.query;

import com.harmony.umbrella.data.query.SpecificationAssembler.SpecificationType;
import com.harmony.umbrella.data.util.QueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.LongSupplier;

import static com.harmony.umbrella.data.query.QueryFeature.DISTINCT;
import static com.harmony.umbrella.data.query.QueryFeature.FULL_TABLE_QUERY;

/**
 * @author wuxii@foxmail.com
 */
public class QueryResultImpl<T> implements QueryResult<T> {

    private Class<T> domainClass;
    private SpecificationExecutor executor;
    private SpecificationAssembler assembler;

    public QueryResultImpl(EntityManager entityManager, QueryBundle<T> bundle) {
        this.domainClass = bundle.getEntityClass();
        this.executor = new SpecificationExecutor(bundle, entityManager);
        this.assembler = new SpecificationAssembler<>(bundle);
    }

    @Override
    public T getSingleResult() {
        return (T) getSingleResult((Selections) null, domainClass);
    }

    @Override
    public T getFirstResult() {
        return getFirstResult(null, domainClass);
    }

    @Override
    public List<T> getResultList() {
        return getResultList((Selections) null, domainClass);
    }

    @Override
    public List<T> getRangeResult() {
        Pageable pageable = assembler.getPageable();
        return getRangeList(null, pageable, domainClass);
    }

    @Override
    public Page<T> getResultPage() {
        return getResultPage(null, domainClass);
    }

    @Override
    public long countResult() {
        return getSingleResult(Selections.count(assembler.isEnable(DISTINCT)), Long.class);
    }

    @Override
    public <E> E getSingleResult(String column, Class<E> resultType) {
        return getSingleResult(Selections.of(column), resultType);
    }

    @Override
    public <E> E getSingleResult(String[] column, Class<E> resultType) {
        return getSingleResult(Selections.of(column), resultType);
    }

    @Override
    public <E> List<E> getResultList(String column, Class<E> resultType) {
        return getResultList(Selections.of(column), resultType);
    }

    @Override
    public <E> List<E> getResultList(String[] column, Class<E> resultType) {
        return getResultList(Selections.of(column), resultType);
    }

    @Override
    public <E> List<E> getRangeResult(String column, Class<E> resultType) {
        Pageable pageable = assembler.getPageable();
        return getRangeList(Selections.of(column), pageable, resultType);
    }

    @Override
    public <E> List<E> getRangeResult(String[] column, Class<E> resultType) {
        Pageable pageable = assembler.getPageable();
        return getRangeList(Selections.of(column), pageable, resultType);
    }

    @Override
    public <E> E getFunctionResult(String function, String column, Class<E> resultType) {
        return getSingleResult(Selections.function(function, column), resultType);
    }

    @Override
    public <E> E getSingleResult(Selections<T> selections, Class<E> resultType) {
        Specification<T> spec = assembler.assembly(selections, SpecificationType.values());
        return executor.getOne(spec, resultType);
    }

    @Override
    public <E> E getFirstResult(Selections<T> selections, Class<E> resultType) {
        List<E> list = getRangeList(selections, PageRequest.of(0, 1), resultType);
        if (list.isEmpty()) {
            throw new NoResultException("can't found first result");
        }
        return list.get(0);
    }

    @Override
    public <E> List<E> getResultList(Selections<T> selections, Class<E> resultType) {
        Specification spec = assembler.assembly(selections, SpecificationType.values());
        return executor.getAll(spec, resultType);
    }

    @Override
    public <E> List<E> getRangeResult(Selections<T> selections, Class<E> resultType) {
        return getRangeList(selections, assembler.getPageable(), resultType);
    }

    @Override
    public <E> Page<E> getResultPage(Selections<T> selections, Class<E> resultType) {
        Pageable pageable = assembler.getPageable();
        return PageableExecutionUtils.getPage(getRangeList(selections, pageable, resultType), pageable, new LongSupplier() {
            @Override
            public long getAsLong() {
                return countResult();
            }
        });
    }

    private <E> List<E> getRangeList(Selections<T> selections, Pageable pageable, Class<E> resultType) {
        Specification spec = assembler.assembly(selections, SpecificationType.values());
        return executor.getRange(pageable.getOffset(), pageable.getPageSize(), spec, resultType);
    }

    static final class SpecificationExecutor {

        private final EntityManager entityManager;
        private final CriteriaBuilder builder;
        private final Class domainClass;
        private final int queryFeature;

        private SpecificationExecutor(QueryBundle<?> bundle, EntityManager entityManager) {
            this.entityManager = entityManager;
            this.builder = entityManager.getCriteriaBuilder();
            this.domainClass = bundle.getEntityClass();
            this.queryFeature = bundle.getQueryFeature();
        }

        private <T> T getOne(Specification spec, Class<T> resultClass) {
            try {
                CriteriaQuery<T> query = createCriteriaQuery(spec, resultClass);
                return entityManager.createQuery(query).getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }

        private <T> List<T> getAll(Specification spec, Class<T> resultClass) {
            CriteriaQuery<T> query = createCriteriaQuery(spec, resultClass);
            if (!QueryUtils.hasRestriction(query) && !FULL_TABLE_QUERY.isEnable(queryFeature)) {
                throw new IllegalStateException("not allow empty condition full table query");
            }
            return entityManager.createQuery(query).getResultList();
        }

        private <T> List<T> getRange(long offset, int size, Specification spec, Class<T> resultClass) {
            CriteriaQuery<T> criteriaQuery = createCriteriaQuery(spec, resultClass);
            TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
            query.setFirstResult((int) offset);
            query.setMaxResults(size);
            return query.getResultList();
        }

        private <T> CriteriaQuery<T> createCriteriaQuery(Specification spec, Class<T> resultClass) {
            CriteriaQuery<T> query = builder.createQuery(resultClass);
            Root<T> root = query.from(domainClass);
            if (spec != null) {
                Predicate predicate = spec.toPredicate(root, query, builder);
                if (predicate != null) {
                    query.where(predicate);
                }
            }
            query.distinct(DISTINCT.isEnable(queryFeature));
            return query;
        }

    }

}
