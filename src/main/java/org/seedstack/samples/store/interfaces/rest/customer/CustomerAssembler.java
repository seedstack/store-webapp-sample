/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.samples.store.interfaces.rest.customer;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.seedstack.business.modelmapper.ModelMapperAssembler;
import org.seedstack.samples.store.domain.model.customer.Customer;
import org.seedstack.samples.store.domain.model.customer.CustomerId;

public class CustomerAssembler extends ModelMapperAssembler<Customer, CustomerRepresentation> {
    @Override
    protected void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(
                new AbstractConverter<CustomerId, String>() {
                    @Override
                    protected String convert(CustomerId source) {
                        return source.getValue();
                    }
                }
        );
        modelMapper.addConverter(
                new AbstractConverter<String, CustomerId>() {
                    @Override
                    protected CustomerId convert(String source) {
                        return new CustomerId(source);
                    }
                }
        );
    }
}
