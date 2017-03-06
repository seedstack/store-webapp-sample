/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.model.customer;

import org.seedstack.business.domain.BaseAggregateRoot;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Customer extends BaseAggregateRoot<CustomerId> {
    @EmbeddedId
    private CustomerId id;
    private String firstName;
    private String lastName;
    private String address;
    private String deliveryAddress;
    private String password;

    private Customer() {
        // for JPA
    }

    Customer(CustomerId customerId) {
        // used by the factory only
        this.id = customerId;
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

    @Override
    public CustomerId getEntityId() {
        return id;
    }
}
