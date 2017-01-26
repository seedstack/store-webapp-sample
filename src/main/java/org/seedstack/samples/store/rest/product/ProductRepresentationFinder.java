/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.rest.product;


import org.seedstack.business.finder.Finder;
import org.seedstack.business.finder.Range;
import org.seedstack.business.finder.Result;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.seed.transaction.Transactional;

import java.util.Map;

/**
 * ProductFinder interface
 */
@Finder
@Transactional
@JpaUnit("ecommerce-domain")
public interface ProductRepresentationFinder {
    /**
     * Find a Product by id
     *
     * @param value the product id
     * @return ProductRepresentation
     */
    ProductRepresentation findProductById(long value);

    /**
     * Find all products with pagination
     *
     * @param range    the range
     * @param criteria the criteria
     * @return the result
     */
    Result<ProductRepresentation> findAllProducts(Range range, Map<String, Object> criteria);

}
