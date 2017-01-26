/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.customer;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerFactoryDefaultUnitTest {

    private CustomerFactory underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new CustomerFactoryDefault();

    }

    @Test
    public void testCreateCustomer_with_email() throws Exception {
        String email = "test@mail.com";
        Customer customer = underTest.createCustomer(email);
        assertThat(customer).isNotNull();
        assertThat(customer.getEntityId().getValue()).isEqualTo(email);
        assertThat(customer.getFirstName()).isNull();
        assertThat(customer.getLastName()).isNull();
    }

    @Test
    public void testCreateCustomer_with_email_firstname_lastname_role_password() throws Exception {
        String email = "test@mail.com";
        String firstname = "firstname";
        String lastname = "lastname";

        Customer customer = underTest.createCustomer(email, firstname, lastname);
        assertThat(customer).isNotNull();

        assertThat(customer.getEntityId().getValue()).isEqualTo(email);
        assertThat(customer.getFirstName()).isEqualTo(firstname);
        assertThat(customer.getLastName()).isEqualTo(lastname);
    }


    @Test
    public void testCreateCustomer_with_email_firstname_lastname_password() throws Exception {
        String email = "test@mail.com";
        String firstname = "firstname";
        String lastname = "lastname";

        Customer customer = underTest.createCustomer(email, firstname, lastname);
        assertThat(customer).isNotNull();
        assertThat(customer.getEntityId().getValue()).isEqualTo(email);
        assertThat(customer.getFirstName()).isEqualTo(firstname);
        assertThat(customer.getLastName()).isEqualTo(lastname);
    }


}
