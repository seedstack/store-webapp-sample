/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.data.product;

import org.seedstack.business.api.interfaces.assembler.FluentAssembler;
import org.seedstack.samples.store.domain.product.Product;
import org.seedstack.samples.store.domain.product.ProductRepository;
import org.seedstack.seed.core.spi.data.DataImporter;
import org.seedstack.seed.core.spi.data.DataSet;
import org.seedstack.seed.persistence.jpa.api.JpaUnit;
import org.seedstack.seed.transaction.api.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * @author adrien.lauer@mpsa.com
 */
@DataSet(group = "ecommerce-domain", name = "products")
public class ProductDataImporter implements DataImporter<ProductDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDataImporter.class);

    @Inject
    private ProductRepository productRepository;

    private Set<ProductDTO> staging = new HashSet<ProductDTO>();

    @Inject
    private FluentAssembler fluentAssembler;

    @Transactional
    @JpaUnit("ecommerce-domain")
    @Override
    public boolean isInitialized() {
        return productRepository.count() != 0;
    }

    @Override
    public void importData(ProductDTO data) {
        staging.add(data);
    }

    @Transactional
    @JpaUnit("ecommerce-domain")
    @Override
    public void commit(boolean clear) {
        if (clear) {
            LOGGER.info("Clearing products");
            productRepository.deleteAll();
        }

        LOGGER.info("staging size: " + staging.size());

        for (ProductDTO productDTO : staging) {
            productRepository.persist(fluentAssembler.merge(productDTO).into(Product.class).fromFactory());
        }

        LOGGER.info("Import of products completed");
        staging.clear();
    }

    @Override
    public void rollback() {
        staging.clear();
        LOGGER.warn("Rollback occurred during product import");
    }
}
