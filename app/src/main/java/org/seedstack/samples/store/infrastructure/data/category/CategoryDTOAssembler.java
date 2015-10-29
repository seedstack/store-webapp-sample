/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.data.category;

import org.seedstack.business.api.interfaces.assembler.BaseAssembler;
import org.seedstack.samples.store.domain.category.Category;

/**
 * @author adrien.lauer@mpsa.com
 */
public class CategoryDTOAssembler extends BaseAssembler<Category, CategoryDTO> {
    @Override
    protected void doAssembleDtoFromAggregate(CategoryDTO targetDto, Category sourceAggregate) {
        targetDto.setId(sourceAggregate.getEntityId());
        targetDto.setName(sourceAggregate.getName());
        targetDto.setImg(sourceAggregate.getUrlImg());
    }

    @Override
    protected void doMergeAggregateWithDto(Category targetAggregate, CategoryDTO sourceDto) {
        targetAggregate.setEntityId(sourceDto.getId());
        targetAggregate.setName(sourceDto.getName());
        targetAggregate.setUrlImg(sourceDto.getImg());
    }
}
