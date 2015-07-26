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


import org.seedstack.business.api.interfaces.finder.Finder;
import org.seedstack.business.api.interfaces.finder.Range;
import org.seedstack.business.api.interfaces.finder.Result;
import org.seedstack.seed.persistence.jpa.api.JpaUnit;
import org.seedstack.seed.transaction.api.Transactional;

import java.util.List;
import java.util.Map;
/**
 * ProductFinder interface
 */
@Finder
@Transactional
@JpaUnit("ecommerce-domain")
public interface ProductRepresentationFinder {
	/**
	 * Find all products
	 * @return List<ProductRepresentation>
	 */
    List<ProductRepresentation> findAllProducts();
    /**
     * Find a Product by id
     * @param value the product id
     * @return ProductRepresentation
     */
    ProductRepresentation findProductById(long value);
    /**
     * Find Products by category id
     * @param id the product id
     * @return List<ProductRepresentation>
     */
    List<ProductRepresentation> findProductsByCategory(long id);
    /**
	 * Find all products with pagination
     * @param range the range
     * @param criteria the criteria
     * @return the result
     */
    Result<ProductRepresentation> findAllProducts(Range range, Map<String, Object> criteria );

}
