/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.category;

import org.seedstack.business.domain.BaseFactory;

/**
 * Category Factory implementation.
 */
public class CategoryFactoryDefault extends BaseFactory<Category> implements CategoryFactory {

	@Override
	public Category createCategory(Long id, String name, String urlImg) {
		return new Category(id, name, urlImg);
	}
}
