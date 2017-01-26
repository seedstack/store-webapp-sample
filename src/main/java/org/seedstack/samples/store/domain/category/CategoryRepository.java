/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.category;


import org.seedstack.business.domain.GenericRepository;

/**
 * Repository interface of category.
 */
public interface CategoryRepository extends GenericRepository<Category, Long> {

    /**
     * Saves the category.
     *
     * @param category the category to save
     * @return the category
     */
    Category saveCategory(Category category);

    /**
     * Persists the category.
     *
     * @param category the category to persist
     */
    void persistCategory(Category category);

    /**
     * Delete all categories
     *
     * @return number of categories deleted
     */
    long deleteAll();

    /**
     * Count the number of categories in the repository.
     *
     * @return category count
     */
    long count();
}
