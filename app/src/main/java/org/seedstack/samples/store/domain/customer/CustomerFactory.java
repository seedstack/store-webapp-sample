/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.customer;


import org.seedstack.business.api.domain.GenericFactory;

/**
 * Customer Factory Interface.
 */
public interface CustomerFactory extends GenericFactory<Customer> {

    /**
     * Factory create method.
     * Create with id
     *
     * @param email the customer email
     * @return the customer
     */
    Customer createCustomer(String email);

    /**
     * Factory create method
     * Create with all fields
     *
     * @param email           the customer email
     * @param password        the customer password
     * @param firstName       the customer firstName
     * @param lastName        the customer lastName
     * @param address         the customer address
     * @param deliveryAddress the customer delivery address
     * @return the customer
     */
    Customer createCustomer(String email, String password, String firstName, String lastName, String address, String deliveryAddress);

    /**
     * Factory create method
     * Create with main fields
     *
     * @param email     the customer email
     * @param firstName the customer firstName
     * @param lastName  the customer lastName
     * @return the customer
     */
    Customer createCustomer(String email, String firstName, String lastName);

    /**
     * Creates a customer Id.
     *
     * @param customerId the customer Id
     * @return the customer Id
     */
    CustomerId createCustomerId(String customerId);
}
