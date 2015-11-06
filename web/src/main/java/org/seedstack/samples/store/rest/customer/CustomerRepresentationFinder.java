/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.rest.customer;


import org.seedstack.business.finder.Finder;
import org.seedstack.business.finder.Range;
import org.seedstack.business.finder.Result;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.seed.transaction.Transactional;

import java.util.List;
import java.util.Map;

/**
 * CustomerFinder interface.
 */
@Finder
@Transactional
@JpaUnit("ecommerce-domain")
public interface CustomerRepresentationFinder {

    /**
     * Find all customers
     *
     * @return List<CustomerRepresentation>
     */
    List<CustomerRepresentation> findAllCustomers();

    /**
     * Find a customer by id
     *
     * @param value the customer id
     * @return CustomerRepresentation
     */
    CustomerRepresentation findCustomerById(String value);

    /**
     * Find all customers - with pagination
     *
     * @param range    the range
     * @param criteria the criteria
     * @return Result<CustomerRepresentation>
     */
    Result<CustomerRepresentation> findAllCustomers(Range range, Map<String, Object> criteria);
}
