/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.rest.product;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.seedstack.business.assembler.modelmapper.ModelMapperAssembler;
import org.seedstack.samples.store.domain.product.Product;

/**
 * Overrides the configuration of the the DefaultModelMapper for assembling Product into ProductRepresentation.
 *
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class ProductModelMapperAssembler extends ModelMapperAssembler<Product, ProductRepresentation> {

    @Override
    protected void configureAssembly(ModelMapper modelMapper) {
        modelMapper.addMappings(new PropertyMap<Product, ProductRepresentation>() {
            @Override
            protected void configure() {
                map().setId(source.getEntityId()); // Required due to the ambiguity with the categoryId field
            }
        });
    }

    @Override
    protected void configureMerge(ModelMapper modelMapper) {
    }
}
