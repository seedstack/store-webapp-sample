/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.jpa;

import org.seedstack.business.jpa.BaseJpaRepository;
import org.seedstack.samples.store.domain.category.Category;
import org.seedstack.samples.store.domain.category.CategoryRepository;

import javax.persistence.Query;

/**
 * JPA Repository Implementation.
 */
public class CategoryJpaRepository extends BaseJpaRepository<Category, Long> implements CategoryRepository {

    private static final int MAX_ID = 5000;

    @Override
    public Category saveCategory(Category category) {
        if (category.getEntityId() == null) {
            category.setEntityId(generateID());
        }
        return super.save(category);
    }

    @Override
    public void persistCategory(Category category) {
        if (category.getEntityId() == null) {
            category.setEntityId(generateID());
        }
        super.persist(category);
    }

    @Override
    public long count() {
        return (Long) entityManager.createQuery("SELECT count(*) FROM Category c").getSingleResult();
    }

    @Override
    public long deleteAll() {
        return entityManager.createQuery("DELETE FROM Category").executeUpdate();
    }

    // TODO change this for a identity handler strategy
    private long generateID() {
        long id = 0;
        Query query = entityManager.createQuery("select max(c.categoryId) from Category c");
        if (query.getSingleResult() != null) {
            id = Math.max(MAX_ID, (Long) query.getSingleResult());
        }
        return id + 1;
    }

}
