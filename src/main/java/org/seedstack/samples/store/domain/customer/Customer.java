/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.customer;

import org.seedstack.business.domain.BaseAggregateRoot;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;


/**
 * The customer aggregate root.
 */
@Entity
public class Customer extends BaseAggregateRoot<CustomerId> {

    @EmbeddedId
    protected CustomerId entityId;
	private String firstName;
	private String lastName;
	private String address;
	private String deliveryAddress;
	private String password;

	/**
	 * Constructor are in visibility package because only Factories can create
	 * aggregates and entities.
	 * <p/>
	 * Factories are in the same package so he can access package visibility.
	 */
	Customer() {
	}

	void setEntityId(CustomerId entityId) {
		this.entityId = entityId;
	}

    @Override
    public CustomerId getEntityId() {
        return entityId;
    }
	/**
	 * Getter firstName
	 * 
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setter firstName
	 * 
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Getter lastName
	 * 
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setter lastName
	 * 
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Getter address
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Setter address
	 * 
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Getter deliveryAddress
	 * 
	 * @return the deliveryAddress
	 */
	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	/**
	 * Setter deliveryAddress
	 * 
	 * @param deliveryAddress
	 *            the deliveryAddress to set
	 */
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	/**
	 * Getter password
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter password
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
