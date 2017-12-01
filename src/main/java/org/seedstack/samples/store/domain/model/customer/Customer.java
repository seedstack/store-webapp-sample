/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.samples.store.domain.model.customer;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.seedstack.business.domain.BaseAggregateRoot;

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
        // A default constructor is needed by the persistence framework
        // but can be kept private
    }

    public Customer(CustomerId customerId) {
        this.id = customerId;
    }

    @Override
    public CustomerId getId() {
        return id;
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
