/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.order;


import org.seedstack.business.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * OrderItem entity.
 */
@Entity
public class OrderItem extends BaseEntity<Long> {
	
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderItemId;

    private int quantity;
    private long productId;
    private double price;

    /**
     * Constructor are in visibility package because only Factories can create
     * aggregates and entities.
     * <p/>
     * Factories are in the same package so he can access package visibility.
     */
    OrderItem() {
    }

    @Override
    public Long getEntityId() {
        return orderItemId;
    }

    /**
	 * Gets the orderItemId.
	 * 
	 * @return the orderItemId
	 */
	public Long getOrderItemId() {
		return orderItemId;
	}

    /**
	 * Sets the orderItemId.
	 * 
	 * @param orderItemId the orderItemId to set
	 */
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

    /**
	 * Gets the quantity.
	 * 
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

    /**
	 * Sets the quantity.
	 * 
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

    /**
	 * Gets the productId.
	 * 
	 * @return the productId
	 */
	public long getProductId() {
		return productId;
	}

    /**
	 * Sets the productId.
	 * 
	 * @param productId the productId to set
	 */
	public void setProductId(long productId) {
		this.productId = productId;
	}

    /**
	 * Gets the price.
	 * 
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

    /**
	 * Sets the price.
	 * 
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

}
