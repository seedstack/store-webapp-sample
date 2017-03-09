/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.jpa;

import org.seedstack.business.finder.Range;
import org.seedstack.business.finder.Result;
import org.seedstack.samples.store.domain.model.product.Product;
import org.seedstack.samples.store.interfaces.rest.product.ProductRepresentation;
import org.seedstack.samples.store.interfaces.rest.product.ProductRepresentationFinder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ProductRepresentationJpaFinder implements ProductRepresentationFinder {
    private final EntityManager entityManager;

    @Inject
    public ProductRepresentationJpaFinder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Result<ProductRepresentation> findProductsFromCategory(Range range, long categoryId) {
        return new Result<>(computeResultList(range, null, categoryId), range.getOffset(), computeFullRequestSize(null, categoryId));
    }

    @Override
    public Result<ProductRepresentation> findProducts(Range range, String filter) {
        return new Result<>(computeResultList(range, filter, null), range.getOffset(), computeFullRequestSize(filter, null));
    }

    private List<ProductRepresentation> computeResultList(Range range, String filter, Long categoryId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductRepresentation> cq = cb.createQuery(ProductRepresentation.class);
        Root<Product> c = cq.from(Product.class);
        cq.select(
                cb.construct(
                        ProductRepresentation.class,
                        c.get("id"),
                        c.get("designation"),
                        c.get("summary"),
                        c.get("details"),
                        c.get("picture"),
                        c.get("price")
                )
        );

        applyCriteria(cb, c, cq, filter, categoryId);

        return fillCriteria(entityManager.createQuery(cq), filter, categoryId)
                .setFirstResult((int) range.getOffset())
                .setMaxResults((int) range.getSize())
                .getResultList();
    }

    private long computeFullRequestSize(String filter, Long categoryId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Product> r = cq.from(Product.class);
        cq.select(cb.count(r));

        applyCriteria(cb, r, cq, filter, categoryId);

        return fillCriteria(entityManager.createQuery(cq), filter, categoryId).getSingleResult();
    }

    private void applyCriteria(CriteriaBuilder cb, Root<?> r, CriteriaQuery<?> cq, String filter, Long categoryId) {
        if (categoryId != null) {
            cq.where(cb.equal(r.get("categoryId"), cb.parameter(Long.class, "categoryId")));
        }
        if (filter != null) {
            cq.where(cb.or(
                    cb.like(r.get("designation"), cb.parameter(String.class, "filter")),
                    cb.like(r.get("summary"), cb.parameter(String.class, "filter")),
                    cb.like(r.get("details"), cb.parameter(String.class, "filter"))
            ));
        }
    }

    private <T> TypedQuery<T> fillCriteria(TypedQuery<T> q, String filter, Long categoryId) {
        if (categoryId != null) {
            q.setParameter("categoryId", categoryId);
        }
        if (filter != null) {
            q.setParameter("filter", "%" + filter + "%");
        }
        return q;
    }
}
