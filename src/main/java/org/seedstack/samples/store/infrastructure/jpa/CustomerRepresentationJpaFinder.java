/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.jpa;

import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.business.finder.BaseRangeFinder;
import org.seedstack.business.finder.Range;
import org.seedstack.business.finder.Result;
import org.seedstack.samples.store.domain.customer.Customer;
import org.seedstack.samples.store.domain.customer.CustomerFactory;
import org.seedstack.samples.store.domain.customer.CustomerId;
import org.seedstack.samples.store.rest.customer.CustomerRepresentation;
import org.seedstack.samples.store.rest.customer.CustomerRepresentationFinder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Customer Finder JPA Implementation.
 */
public class CustomerRepresentationJpaFinder extends BaseRangeFinder<CustomerRepresentation, Map<String, Object>> implements CustomerRepresentationFinder {

    @Inject
    private EntityManager entityManager;
    @Inject
    private FluentAssembler fluentAssembler;
    @Inject
    private CustomerFactory customerFactory;

    @Override
    public List<CustomerRepresentation> findAllCustomers() {

        TypedQuery<CustomerRepresentation> query = entityManager
                .createQuery(
                        "select new "
                                + CustomerRepresentation.class.getName()
                                + "(c.entityId.customerId, c.firstName, c.lastName,c.password,c.address,"
                                + "c.deliveryAddress )" + "from Customer c ",
                        CustomerRepresentation.class);
        return query.getResultList();
    }

    @Override
    public CustomerRepresentation findCustomerById(String value) {
        CustomerId id = customerFactory.createCustomerId(value);
        Customer customer = entityManager.find(Customer.class, id);

        if (customer != null) {
            return fluentAssembler.assemble(customer).to(CustomerRepresentation.class);
        }
        return null;
    }


    @Override
    public Result<CustomerRepresentation> findAllCustomers(Range range, Map<String, Object> criteria) {
        return this.find(range, criteria);
    }

    private CriteriaQuery<Customer> getCriteriaQuery(Map<String, Object> criteria) {
        String searchStringKey = "searchString";

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> q = cb.createQuery(Customer.class);
        Root<Customer> customer = q.from(Customer.class);
        q.select(customer);

        List<Predicate> restrictions = new ArrayList<Predicate>();

        // Check and set searchString
        String searchString = null;
        if (criteria != null) {
            searchString = (String) criteria.get(searchStringKey);
        }

        if (searchString != null) {
            Field[] fields = Customer.class.getDeclaredFields();
            for (Field field : fields) {
                if (String.class.isAssignableFrom(field.getType())) {
                    restrictions.add(cb.like(cb.upper(customer.<String>get(field.getName())), "%" + searchString.toUpperCase() + "%"));
                }
            }
        }
        if (!restrictions.isEmpty()) {
            q.where(cb.or(restrictions.toArray(new Predicate[restrictions.size()])));
        }
        return q;
    }

    @Override
    protected long computeFullRequestSize(Map<String, Object> criteria) {
        TypedQuery<Customer> query = entityManager.createQuery(getCriteriaQuery(criteria));
        return query.getResultList().size();
    }

    @Override
    protected List<CustomerRepresentation> computeResultList(Range range,
                                                             Map<String, Object> criteria) {

        TypedQuery<Customer> query = entityManager.createQuery(getCriteriaQuery(criteria));
        query.setFirstResult((int) range.getOffset());
        query.setMaxResults((int) range.getSize());

        List<CustomerRepresentation> result = new ArrayList<CustomerRepresentation>();
        for (Customer customer : query.getResultList()) {
            result.add(fluentAssembler.assemble(customer).to(CustomerRepresentation.class));
        }

        return result;
    }
}
