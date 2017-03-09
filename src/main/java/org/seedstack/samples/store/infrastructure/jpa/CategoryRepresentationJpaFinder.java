/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.jpa;

import org.seedstack.business.finder.BaseRangeFinder;
import org.seedstack.business.finder.Range;
import org.seedstack.samples.store.domain.model.category.Category;
import org.seedstack.samples.store.interfaces.rest.category.CategoryRepresentation;
import org.seedstack.samples.store.interfaces.rest.category.CategoryRepresentationFinder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CategoryRepresentationJpaFinder extends BaseRangeFinder<CategoryRepresentation, String> implements CategoryRepresentationFinder {
    private final EntityManager entityManager;

    @Inject
    public CategoryRepresentationJpaFinder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<CategoryRepresentation> computeResultList(Range range, String filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryRepresentation> cq = cb.createQuery(CategoryRepresentation.class);
        Root<Category> r = cq.from(Category.class);
        cq.select(
                cb.construct(
                        CategoryRepresentation.class,
                        r.get("id"),
                        r.get("name"),
                        r.get("urlImg")
                )
        );

        applyCriteria(cb, r, cq, filter);

        return fillCriteria(entityManager.createQuery(cq), filter)
                .setFirstResult((int) range.getOffset())
                .setMaxResults((int) range.getSize())
                .getResultList();
    }

    @Override
    public long computeFullRequestSize(String filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Category> r = cq.from(Category.class);
        cq.select(cb.count(r));

        applyCriteria(cb, r, cq, filter);

        return fillCriteria(entityManager.createQuery(cq), filter).getSingleResult();
    }

    private void applyCriteria(CriteriaBuilder cb, Root<?> r, CriteriaQuery<?> cq, String filter) {
        if (filter != null) {
            cq.where(cb.like(r.get("name"), cb.parameter(String.class, "filter")));
        }
    }

    private <T> TypedQuery<T> fillCriteria(TypedQuery<T> q, String filter) {
        if (filter != null) {
            q.setParameter("filter", "%" + filter + "%");
        }
        return q;
    }
}
