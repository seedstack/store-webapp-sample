/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.rest.customer;


import org.seedstack.business.assembler.MatchingEntityId;
import org.seedstack.business.assembler.MatchingFactoryParameter;

/**
 * CustomerRepresentation.
 */
public class CustomerRepresentation {
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String deliveryAddress;
    private String password;

	/**
	 * Constructor.
	 */
    public CustomerRepresentation() {
    }

    /**
     * Constructor with main fields.
     *
     * @param id the id
     * @param firstname the firstname
     * @param lastname the lastname
     */
    public CustomerRepresentation(String id, String firstname, String lastname) {
        this.id = id;
        this.firstName = firstname;
        this.lastName = lastname;
    }

    /**
     * Constructor with all fields.
     *
     * @param id the id
     * @param firstName the firstName
     * @param lastName the lastName
     * @param password the password
     * @param address the address
     * @param deliveryAddress the deliveryAddress
     */
    public CustomerRepresentation(String id, String firstName, String lastName, String password, String address,
                                  String deliveryAddress) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.deliveryAddress = deliveryAddress;
        this.password = password;
    }

    /**
     * Get the id.
     * @return the id
     */
    @MatchingFactoryParameter(index = 0)
    @MatchingEntityId
    public String getId() {
        return id;
    }

    /**
	 * Gets password
	 * @return String
	 */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the id.
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
	/**
	 * Gets first name
	 * @return String
	 */
    @MatchingFactoryParameter(index = 1)
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
	/**
	 * Gets last name
	 * @return String
	 */

    @MatchingFactoryParameter(index = 2)
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
	/**
	 * Gets address
	 * @return String
	 */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address.
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
	/**
	 * Gets delivery address
	 * @return String
	 */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Sets the delivery address.
     * @param deliveryAddress the delivery address to set
     */
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
