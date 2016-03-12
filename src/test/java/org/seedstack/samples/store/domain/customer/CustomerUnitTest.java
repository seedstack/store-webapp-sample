/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.customer;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerUnitTest {

    private Customer underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new Customer();

    }

    @Test
    public void testSetEntityId() throws Exception {
        CustomerId customerId = new CustomerId("testId");
        underTest.setEntityId(customerId);
        assertThat(underTest.getEntityId()).isNotNull().isEqualTo(customerId);
    }

    @Test
    public void testSetFirstName() throws Exception {
        underTest.setFirstName("firstname");
        assertThat(underTest.getFirstName()).isNotNull().isEqualTo("firstname");
    }

    @Test
    public void testSetLastName() throws Exception {
        underTest.setLastName("lastname");
        assertThat(underTest.getLastName()).isNotNull().isEqualTo("lastname");
    }

    @Test
    public void testSetAddress() {
        underTest.setAddress("1234");
        assertThat(underTest.getAddress()).isNotNull().isEqualTo("1234");
    }

    @Test
    public void testSetDelivreryAddress() {
        underTest.setDeliveryAddress("567");
        assertThat(underTest.getDeliveryAddress()).isNotNull().isEqualTo("567");
    }
}

