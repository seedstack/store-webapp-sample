/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.data.customer;

import org.seedstack.business.assembler.BaseAssembler;
import org.seedstack.samples.store.domain.customer.Customer;
import org.seedstack.samples.store.domain.customer.CustomerFactory;

import javax.inject.Inject;

/**
 * @author adrien.lauer@mpsa.com
 */
public class CustomerDTOAssembler extends BaseAssembler<Customer, CustomerDTO> {
    @Inject
    CustomerFactory customerFactory;

    @Override
    protected void doAssembleDtoFromAggregate(CustomerDTO targetDto, Customer sourceAggregate) {
        targetDto.setId(sourceAggregate.getEntityId().getValue());
        targetDto.setAddress(sourceAggregate.getAddress());
        targetDto.setDeliveryAddress(sourceAggregate.getDeliveryAddress());
        targetDto.setFirstName(sourceAggregate.getFirstName());
        targetDto.setLastName(sourceAggregate.getLastName());
        targetDto.setPassword(sourceAggregate.getPassword());
    }

    @Override
    protected void doMergeAggregateWithDto(Customer targetAggregate, CustomerDTO sourceDto) {
        targetAggregate.setAddress(sourceDto.getAddress());
        targetAggregate.setDeliveryAddress(sourceDto.getDeliveryAddress());
        targetAggregate.setFirstName(sourceDto.getFirstName());
        targetAggregate.setLastName(sourceDto.getLastName());
        targetAggregate.setPassword(sourceDto.getPassword());
    }
}
