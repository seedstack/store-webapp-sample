/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.data.category;

import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.samples.store.domain.category.Category;
import org.seedstack.samples.store.domain.category.CategoryRepository;
import org.seedstack.seed.DataImporter;
import org.seedstack.seed.DataSet;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.seed.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * @author adrien.lauer@mpsa.com
 */
@DataSet(group = "ecommerce-domain", name = "categories")
public class CategoryDataImporter implements DataImporter<CategoryDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDataImporter.class);

    @Inject
    private CategoryRepository categoryRepository;

    private Set<CategoryDTO> staging = new HashSet<CategoryDTO>();

    @Inject
    private FluentAssembler fluentAssembler;

    @Transactional
    @JpaUnit("ecommerce-domain")
    @Override
    public boolean isInitialized() {
        return categoryRepository.count() != 0;
    }

    @Override
    public void importData(CategoryDTO data) {
        staging.add(data);
    }

    @Transactional
    @JpaUnit("ecommerce-domain")
    @Override
    public void commit(boolean clear) {
        if (clear) {
            LOGGER.info("Clearing categories");
            categoryRepository.deleteAll();
        }

        LOGGER.info("staging size: " + staging.size());

        for (CategoryDTO categoryDTO : staging) {
            categoryRepository.persist(fluentAssembler.merge(categoryDTO).into(Category.class).fromFactory());
        }

        LOGGER.info("Import of categories completed");
        staging.clear();
    }

    @Override
    public void rollback() {
        staging.clear();
        LOGGER.warn("Rollback occurred during category import");
    }
}
