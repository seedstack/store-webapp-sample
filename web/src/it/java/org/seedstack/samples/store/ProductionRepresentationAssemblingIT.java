/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.samples.store.domain.product.Product;
import org.seedstack.samples.store.domain.product.ProductFactory;
import org.seedstack.samples.store.rest.product.ProductRepresentation;
import org.seedstack.seed.it.SeedITRunner;

import javax.inject.Inject;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
@RunWith(SeedITRunner.class)
public class ProductionRepresentationAssemblingIT {

    @Inject
    private FluentAssembler fluently;

    @Inject
    private ProductFactory factory;

    @Test
    public void testAssemblingWithModelMapper() {
        Product product = factory.createProduct(1L, "destination", "summary", "details", "http://img.png", 14.90, 3L);
        ProductRepresentation representation = fluently.assemble(product).to(ProductRepresentation.class);

        Assertions.assertThat(representation.getId()).isEqualTo(1L);
        Assertions.assertThat(representation.getDesignation()).isEqualTo("destination");
        Assertions.assertThat(representation.getSummary()).isEqualTo("summary");
        Assertions.assertThat(representation.getCategoryId()).isEqualTo(3L);
    }

    @Test
    public void testMergingWithModelMapper() {
        ProductRepresentation representation = new ProductRepresentation(2L, "destination2", "summary2", "details2", "http://img2.png", 24.90, 3L);

        Product product = factory.createProduct(1L, "destination", "summary", "details", "http://img.png", 14.90, 3L);
        fluently.merge(representation).into(product);

        Assertions.assertThat(product.getEntityId()).isEqualTo(1L); // should not have changed (works because setEntityId() is in visibility package)
        Assertions.assertThat(product.getDesignation()).isEqualTo("destination2");
        Assertions.assertThat(product.getSummary()).isEqualTo("summary2");
        Assertions.assertThat(product.getCategoryId()).isEqualTo(3L);
    }
}
