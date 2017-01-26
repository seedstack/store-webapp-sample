/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.order;

import org.seedstack.business.domain.BaseAggregateRoot;
import org.seedstack.samples.store.domain.customer.CustomerId;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Order aggregate root.
 */
@Entity
@Table(name = "THE_ORDER")
public class Order extends BaseAggregateRoot<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    private CustomerId customerId;
    private Date checkoutDate;
    @OneToMany(cascade = {CascadeType.ALL, CascadeType.PERSIST})
    private List<OrderItem> items;
    private double price;

    /**
     * Constructor are in visibility package because only Factories can create
     * aggregates and entities.
     * <p/>
     * Factories are in the same package so he can access package visibility.
     */
    Order() {
    }

    @Override
    public Long getEntityId() {
        return orderId;
    }

    /**
     * Adds an order item.
     *
     * @param quantity  the order quantity
     * @param productId the order product Id
     * @param price     the order price
     */
    public void addOrderItem(int quantity, long productId, double price) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(quantity);
        orderItem.setProductId(productId);
        orderItem.setPrice(price);
        items.add(orderItem);
    }

    /**
     * Clears order items.
     */
    public void clearOrderItems() {
        items.clear();
    }

    /**
     * Gets the order Id.
     *
     * @return the orderId
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * Sets the order Id.
     *
     * @param orderId the order Id to set
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets the customer Id.
     *
     * @return the customerId
     */
    public CustomerId getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer Id.
     *
     * @param customerId the customerId to set
     */
    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the checkout date.
     *
     * @return the checkoutDate
     */
    public Date getCheckoutDate() {
        return checkoutDate;
    }

    /**
     * Sets the checkout date.
     *
     * @param checkoutDate the checkoutDate to set
     */
    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    /**
     * Gets the items.
     *
     * @return the items
     */
    public List<OrderItem> getItems() {
        return items;
    }

    /**
     * Sets the items.
     *
     * @param items the items to set
     */
    public void setItems(List<OrderItem> items) {
        this.items = items;
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
