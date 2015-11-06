/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.jpa;

import org.seedstack.jpa.BaseJpaRepository;
import org.seedstack.samples.store.domain.customer.Customer;
import org.seedstack.samples.store.domain.customer.CustomerId;
import org.seedstack.samples.store.domain.customer.CustomerRepository;

/**
 * JPA Repository implementation.
 */
public class CustomerJpaRepository extends BaseJpaRepository<Customer, CustomerId> implements CustomerRepository {
    @Override
    public long count() {
        return (Long) entityManager.createQuery("SELECT count(*) FROM Customer c").getSingleResult();
    }

    @Override
    public long deleteAll() {
        return entityManager.createQuery("DELETE FROM Customer").executeUpdate();
    }
}