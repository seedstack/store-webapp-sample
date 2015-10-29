/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.rest.customer;


import org.seedstack.business.api.interfaces.assembler.BaseAssembler;
import org.seedstack.samples.store.domain.customer.Customer;

/**
 * CustomerAssembler
 */
public class CustomerRepresentationAssembler extends BaseAssembler<Customer, CustomerRepresentation> {

    @Override
    protected void doAssembleDtoFromAggregate(CustomerRepresentation targetDto, Customer sourceEntity) {
        targetDto.setId(sourceEntity.getEntityId().getValue());
        targetDto.setLastName(sourceEntity.getLastName());
        targetDto.setFirstName(sourceEntity.getFirstName());
        targetDto.setPassword(sourceEntity.getPassword());
        targetDto.setAddress(sourceEntity.getAddress());
        targetDto.setDeliveryAddress(sourceEntity.getDeliveryAddress());
    }


    @Override
    protected void doMergeAggregateWithDto(Customer targetEntity, CustomerRepresentation sourceDto) {
        targetEntity.setLastName(sourceDto.getLastName());
        targetEntity.setPassword(sourceDto.getPassword());
        targetEntity.setFirstName(sourceDto.getFirstName());
        targetEntity.setDeliveryAddress(sourceDto.getDeliveryAddress());
        targetEntity.setAddress(sourceDto.getAddress());
    }
}
