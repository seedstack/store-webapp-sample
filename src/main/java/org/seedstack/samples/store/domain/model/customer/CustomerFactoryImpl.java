/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.model.customer;

import org.seedstack.business.domain.BaseFactory;

public class CustomerFactoryImpl extends BaseFactory<Customer> implements CustomerFactory {
    @Override
    public Customer create(String email) {
        return new Customer(new CustomerId(email));
    }
}
