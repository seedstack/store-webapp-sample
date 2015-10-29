/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.jpa;

import org.apache.commons.collections.MapUtils;
import org.javatuples.Pair;
import org.seedstack.business.api.interfaces.assembler.FluentAssembler;
import org.seedstack.business.api.interfaces.finder.Range;
import org.seedstack.business.api.interfaces.finder.Result;
import org.seedstack.business.jpa.BaseJpaRangeFinder;
import org.seedstack.samples.store.domain.category.Category;
import org.seedstack.samples.store.rest.category.CategoryRepresentation;
import org.seedstack.samples.store.rest.category.CategoryRepresentationFinder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

/**
 * Category Finder JPA Implementation.
 */
public class JpaCategoryRepresentationFinder extends BaseJpaRangeFinder<CategoryRepresentation> implements CategoryRepresentationFinder {

    @Inject
    private EntityManager entityManager;
    @Inject
    private FluentAssembler fluentAssembler;

    @Override
    public CategoryRepresentation findCategoryById(long value) {
        Category category = entityManager.find(Category.class, value);
        if (category != null) {
            return fluentAssembler.assemble(category).to(CategoryRepresentation.class);
        }
        return null;
    }

    @Override
    public List<CategoryRepresentation> findAllCategory() {
        TypedQuery<CategoryRepresentation> query = entityManager.createQuery(
                "select new " + CategoryRepresentation.class.getName()
                        + " (c.categoryId, c.name,c.urlImg) from Category c order by c.categoryId",
                CategoryRepresentation.class);
        return query.getResultList();
    }

    @Override
    public Result<CategoryRepresentation> findAllCategory(Range range, Map<String, Object> criteria) {
        return this.find(range, criteria);
    }

    @Override
    protected List<CategoryRepresentation> computeResultList(Range range,
                                                             Map<String, Object> criteria) {

        TypedQuery<CategoryRepresentation> query = entityManager.createQuery(
                "select new " + CategoryRepresentation.class.getName()
                        + " (c.categoryId, c.name,c.urlImg) from "
                        + " Category c" + whereCategoryClause("c", criteria)
                        + " order by c.categoryId", CategoryRepresentation.class);

        query.setFirstResult((int) range.getOffset());
        query.setMaxResults((int) range.getSize());
        return query.getResultList();
    }

    /**
     * Custom where clause building from criteria map
     * Note : this is not a reference implementation for building where clause !
     *
     * @param category
     * @param criteria
     * @return
     */
    private String whereCategoryClause(String category, Map<String, Object> criteria) {
        if (MapUtils.isEmpty(criteria)) {
            return "";
        }
        String fieldSeparator = ".";
        String space = " ";
        String equal = " = ";
        String upperCaseBegin = " upper(";
        String parenthesesEnd = ") ";
        String or = " or ";
        String where = " where ";

        StringBuilder whereClauseCriteria = new StringBuilder(where);
        Boolean firstClause = true;
        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            if (!firstClause) {
                whereClauseCriteria.append(or);
            } else {
                firstClause = false;
            }

            //find string in fields is case insensitive
            if (entry.getValue() instanceof Pair) {
                String operator = (String) ((Pair) entry.getValue()).getValue0();
                String value = (String) ((Pair) entry.getValue()).getValue1();
                whereClauseCriteria
                        .append(upperCaseBegin).append(category).append(fieldSeparator).append(entry.getKey()).append(parenthesesEnd)//field
                        .append(space).append(operator).append(space)//operator
                        .append(upperCaseBegin).append(value).append(parenthesesEnd);//value
            } else {
                whereClauseCriteria.append(category).append(fieldSeparator).append(entry.getKey()).append(equal).append(entry.getValue());
            }
        }
        return whereClauseCriteria.toString();
    }

    @Override
    protected long computeFullRequestSize(Map<String, Object> criteria) {
        Query query = entityManager.createQuery("select count(*) from "
                + "Category c" + whereCategoryClause("c", criteria));
        return (Long) query.getSingleResult();
    }
}
