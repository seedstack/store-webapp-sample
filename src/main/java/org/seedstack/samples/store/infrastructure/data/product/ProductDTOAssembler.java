/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.data.product;

import org.seedstack.business.assembler.BaseAssembler;
import org.seedstack.samples.store.domain.product.Product;

/**
 * @author adrien.lauer@mpsa.com
 */
public class ProductDTOAssembler extends BaseAssembler<Product, ProductDTO> {
    @Override
    protected void doAssembleDtoFromAggregate(ProductDTO targetDto, Product sourceAggregate) {
        targetDto.setId(sourceAggregate.getEntityId());
        targetDto.setDesignation(sourceAggregate.getDesignation());
        targetDto.setDetails(sourceAggregate.getDetails());
        targetDto.setPicture(sourceAggregate.getPicture());
        targetDto.setPrice(sourceAggregate.getPrice());
        targetDto.setSummary(sourceAggregate.getSummary());
        targetDto.setCategoryId(sourceAggregate.getCategoryId());
    }

    @Override
    protected void doMergeAggregateWithDto(Product targetAggregate, ProductDTO sourceDto) {
        targetAggregate.setEntityId(sourceDto.getId());
        targetAggregate.setDesignation(sourceDto.getDesignation());
        targetAggregate.setDetails(sourceDto.getDetails());
        targetAggregate.setPicture(sourceDto.getPicture());
        targetAggregate.setPrice(sourceDto.getPrice());
        targetAggregate.setSummary(sourceDto.getSummary());
        targetAggregate.setCategoryId(sourceDto.getCategoryId());
    }
}
