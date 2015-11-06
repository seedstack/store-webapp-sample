/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 30 juil. 2013
 */
package org.seedstack.samples.store.infrastructure.jpa;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.samples.store.domain.category.Category;
import org.seedstack.samples.store.domain.category.CategoryFactory;
import org.seedstack.samples.store.domain.category.CategoryRepository;
import org.seedstack.seed.it.SeedITRunner;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.seed.transaction.Transactional;

import javax.inject.Inject;

@RunWith(SeedITRunner.class)
public class CategoryJpaRepositoryIT {
    @Inject
    private CategoryRepository categoryRepository;
    @Inject
    private CategoryFactory categoryFactory;

    private Category createCategory() {
        Category category = categoryFactory.createCategory(null, "Camera", "./path/to/images/file.jpg");
        Assertions.assertThat(category.getEntityId()).isNull();
        categoryRepository.persistCategory(category);
        Assertions.assertThat(category.getEntityId()).isNotNull();
        return category;
    }

    @Test
    @Transactional
    @JpaUnit("ecommerce-domain")
    public void deleteCategoryTest() {
        Category category = createCategory();
        categoryRepository.delete(category);
        category = categoryRepository.load(category.getEntityId());
        Assertions.assertThat(category).isNull();
    }
}
