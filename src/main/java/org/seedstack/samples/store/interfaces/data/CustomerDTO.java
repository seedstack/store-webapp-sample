/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.samples.store.interfaces.data;

import org.seedstack.business.assembler.AggregateId;
import org.seedstack.business.assembler.DtoOf;
import org.seedstack.business.assembler.FactoryArgument;
import org.seedstack.business.data.DataSet;
import org.seedstack.samples.store.domain.model.customer.Customer;
import org.seedstack.samples.store.domain.model.customer.CustomerId;

@DtoOf(Customer.class)
@DataSet(group = "store", name = "customers")
public class CustomerDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String deliveryAddress;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @AggregateId(index = 0)
    @FactoryArgument(index = 0)
    public CustomerId getCustomerId() {
        return new CustomerId(id);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
