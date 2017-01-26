/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.product;


import org.seedstack.business.domain.GenericFactory;

/**
 * Product Factory interface of {@link Product}.
 */
public interface ProductFactory extends GenericFactory<Product> {
	/**
	 * Create a product.
	 *
	 * @param id the id
	 * @param designation the designation
	 * @param summary the summary
	 * @param details the details
	 * @param picture the picture
	 * @param price the price
	 * @param categoryId the categoryId
	 * @return product
	 */
	Product createProduct(Long id, String designation, String summary, String details, String picture, Double price, Long categoryId);

}
