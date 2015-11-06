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
import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.business.finder.Range;
import org.seedstack.business.finder.Result;
import org.seedstack.jpa.BaseJpaRangeFinder;
import org.seedstack.samples.store.domain.product.Product;
import org.seedstack.samples.store.rest.product.ProductRepresentation;
import org.seedstack.samples.store.rest.product.ProductRepresentationFinder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

/**
 * Product Finder JPA Implementation.
 */
public class JpaProductRepresentationFinder extends BaseJpaRangeFinder<ProductRepresentation> implements ProductRepresentationFinder {

    @Inject
    private EntityManager entityManager;
    @Inject
    private FluentAssembler fluentAssembler;

    private String whereClauseEnd;

    @Override
    public List<ProductRepresentation> findAllProducts() {
        TypedQuery<ProductRepresentation> query = entityManager.createQuery("select new " + ProductRepresentation.class.getName() + "(p.entityId, p.designation, p.summary, p.details, p.picture, p.price,p.categoryId,cat.name)" + " from Product p,Category cat where p.categoryId=cat.categoryId ", ProductRepresentation.class);
        return query.getResultList();
    }

    @Override
    public Result<ProductRepresentation> findAllProducts(Range range, Map<String, Object> criteria) {
        return this.find(range, criteria);
    }

    @Override
    public ProductRepresentation findProductById(long value) {
        Product product = entityManager.find(Product.class, value);
        if (product != null) {
            return fluentAssembler.assemble(product).to(ProductRepresentation.class);
        }
        return null;
    }

    @Override
    public List<ProductRepresentation> findProductsByCategory(long id) {
        TypedQuery<ProductRepresentation> query = entityManager.createQuery("select new " + ProductRepresentation.class.getName() + "(p.entityId, p.designation, p.summary, p.details, p.picture, p.price)" + " from Product p where p.categoryId =:id", ProductRepresentation.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    protected long computeFullRequestSize(Map<String, Object> criteria) {
        buildWhereClauseEnd("p", criteria);
        Query query = entityManager.createQuery("select count(*) from Product p, Category cat where p.categoryId=cat.categoryId " + getWhereClauseEnd());
        return (Long) query.getSingleResult();
    }

    @Override
    protected List<ProductRepresentation> computeResultList(Range range, Map<String, Object> criteria) {
        TypedQuery<ProductRepresentation> query = entityManager.createQuery("select new " + ProductRepresentation.class.getName()
                        + "(p.entityId, p.designation, p.summary, p.details, p.picture, p.price,p.categoryId,cat.name)"
                        + " from Product p,Category cat where p.categoryId=cat.categoryId "
                        + getWhereClauseEnd()
                        + " order by p.categoryId, p.entityId",
                ProductRepresentation.class);
        query.setFirstResult((int) range.getOffset());
        query.setMaxResults((int) range.getSize());
        return query.getResultList();
    }

    /**
     * Custom where clause end built from criteria map.
     * Note : this is not a reference implementation for building where clause !
     *
     * @param product  the product
     * @param criteria the criteria
     */
    private void buildWhereClauseEnd(String product, Map<String, Object> criteria) {

        StringBuilder whereClauseCriteria = new StringBuilder("");
        String categoryIdKey = "categoryId";
        String fieldSeparator = ".";
        String space = " ";
        String equal = " = ";
        String upperCaseBegin = " upper(";
        String parenthesesEnd = ") ";
        String andOpenParentheses = " and ( ";
        String or = " or ";

        // Check and set CategoryId
        Long categoryId = null;
        if (criteria != null) {
            categoryId = (Long) criteria.get(categoryIdKey);
        }

        // filter on category
        if (categoryId != null) {
            whereClauseCriteria.append(" and p.categoryId = ").append(categoryId);
            criteria.remove(categoryIdKey);
        }

        // filter on other fields' content
        if (MapUtils.isNotEmpty(criteria)) {

            Boolean firstClause = true;
            for (Map.Entry<String, Object> entry : criteria.entrySet()) {

                if (firstClause) {
                    whereClauseCriteria.append(andOpenParentheses);
                    firstClause = false;
                } else {
                    whereClauseCriteria.append(or);
                }

                //find string in fields is case insensitive
                if (entry.getValue() instanceof Pair) {
                    String operator = (String) ((Pair) entry.getValue()).getValue0();
                    String value = (String) ((Pair) entry.getValue()).getValue1();
                    whereClauseCriteria
                            .append(upperCaseBegin).append(product).append(fieldSeparator).append(entry.getKey()).append(parenthesesEnd) //field
                            .append(space).append(operator).append(space) // operator
                            .append(upperCaseBegin).append(value).append(parenthesesEnd); //value
                } else {
                    whereClauseCriteria.append(product).append(fieldSeparator).append(entry.getKey()).append(equal).append(entry.getValue());
                }
            }
            // if at least one clause was found, close the parentheses
            if (!firstClause) {
                whereClauseCriteria.append(parenthesesEnd);
            }
        }
        whereClauseEnd = whereClauseCriteria.toString();
    }

    private String getWhereClauseEnd() {
        return whereClauseEnd;
    }
}
