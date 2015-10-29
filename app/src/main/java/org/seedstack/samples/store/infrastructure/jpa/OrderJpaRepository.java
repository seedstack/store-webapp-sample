/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.jpa;


import org.seedstack.business.jpa.BaseJpaRepository;
import org.seedstack.samples.store.domain.order.Order;
import org.seedstack.samples.store.domain.order.OrderRepository;

/**
 * JPA Repository implementation.
 */
public class OrderJpaRepository extends BaseJpaRepository<Order, Long> implements OrderRepository {
}