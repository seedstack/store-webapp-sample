/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.data;

import org.seedstack.business.assembler.AssemblerTypes;
import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.business.domain.Repository;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.store.domain.model.category.Category;
import org.seedstack.samples.store.domain.model.product.Product;
import org.seedstack.seed.DataImporter;
import org.seedstack.seed.DataSet;
import org.seedstack.seed.Logging;
import org.seedstack.seed.transaction.Transactional;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@DataSet(group = "store", name = "categories")
public class CategoryDataImporter implements DataImporter<CategoryDTO> {
    private final Set<CategoryDTO> staging = new HashSet<>();
    private final Repository<Product, Long> productRepository;
    private final Repository<Category, Long> categoryRepository;
    private final FluentAssembler fluentAssembler;
    @Logging
    Logger logger;

    @Inject
    public CategoryDataImporter(Repository<Product, Long> productRepository, Repository<Category, Long> categoryRepository, FluentAssembler fluentAssembler) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fluentAssembler = fluentAssembler;
    }

    @Transactional
    @JpaUnit("store")
    @Override
    public boolean isInitialized() {
        return categoryRepository.count() != 0;
    }

    @Override
    public void importData(CategoryDTO data) {
        staging.add(data);
    }

    @Transactional
    @JpaUnit("store")
    @Override
    public void commit(boolean clear) {
        if (clear) {
            logger.info("Clearing categories");
            categoryRepository.clear();
        }

        logger.info("staging size: " + staging.size());

        for (CategoryDTO categoryDTO : staging) {
            Category category = fluentAssembler.merge(categoryDTO).with(AssemblerTypes.MODEL_MAPPER).into(Category.class).fromFactory();
            categoryRepository.persist(category);
            for (ProductDTO productDTO : categoryDTO.getProducts()) {
                Product product = fluentAssembler.merge(productDTO).with(AssemblerTypes.MODEL_MAPPER).into(Product.class).fromFactory();
                product.setCategoryId(category.getId());
                productRepository.persist(product);
            }
        }

        logger.info("Import of categories completed");
        staging.clear();
    }

    @Override
    public void rollback() {
        staging.clear();
        logger.warn("Rollback occurred during category import");
    }
}
