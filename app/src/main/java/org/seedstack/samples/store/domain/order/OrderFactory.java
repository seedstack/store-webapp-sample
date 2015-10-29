/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.order;

import org.javatuples.Triplet;
import org.seedstack.business.api.domain.GenericFactory;

import java.util.Date;
import java.util.List;

/**
 * Order Factory interface of order.
 */
public interface OrderFactory extends GenericFactory<Order> {

    /**
     * Create an order.
     *
     * @param customerId        the order customerId
     * @param checkoutDate      the order checkoutDate
     * @param price             the order price
     * @param orderItemTriplets the order orderItemTriplets
     * @return the order
     */
    Order createOrder(String customerId, Date checkoutDate, double price, List<Triplet<Integer, Double, Long>> orderItemTriplets);
}
