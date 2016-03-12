/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.category;

import org.seedstack.business.domain.GenericFactory;

/**
 * Category Factory interface.
 */
public interface CategoryFactory extends GenericFactory<Category> {
    /**
     * Factory create method.
     *
     * @param id     the category id
     * @param name   the category name
     * @param urlImg the image URL
     * @return the category
     */
    Category createCategory(Long id, String name, String urlImg);

}
