/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.order;

import org.javatuples.Triplet;
import org.seedstack.business.api.domain.BaseFactory;
import org.seedstack.samples.store.domain.customer.CustomerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Order Factory implementation.
 */
public class OrderFactoryDefault extends BaseFactory<Order> implements OrderFactory {

    @Inject
    private CustomerFactory customerFactory;

    @Override
    public Order createOrder(String customerId, Date checkoutDate, double price, List<Triplet<Integer, Double, Long>> orderItemTriplets) {
        Order o = new Order();
        o.setPrice(price);
        o.setCustomerId(customerFactory.createCustomerId(customerId));
        o.setCheckoutDate(checkoutDate);

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for (Triplet<Integer, Double, Long> orderItemTriplet : orderItemTriplets) {
            OrderItem oi = new OrderItem();
            oi.setQuantity(orderItemTriplet.getValue0());
            oi.setPrice(orderItemTriplet.getValue1());
            oi.setProductId(orderItemTriplet.getValue2());
            orderItems.add(oi);
        }

        o.setItems(orderItems);

        return o;
    }
}
