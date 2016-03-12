/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.rest.category;


import org.seedstack.business.finder.Finder;
import org.seedstack.business.finder.Range;
import org.seedstack.business.finder.Result;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.seed.transaction.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Category finder interface.
 */
@Finder
@Transactional
@JpaUnit("ecommerce-domain")
public interface CategoryRepresentationFinder {

    /**
     * Finds a category by id.
     *
     * @param id the category id
     * @return the category representation
     */
    CategoryRepresentation findCategoryById(long id);

    /**
     * Finds all categories.
     *
     * @return the list of category
     */
    List<CategoryRepresentation> findAllCategory();

    /**
     * Finds all categories for pagination. {@literal <>}
     *
     * @param range    the range
     * @param criteria the criteria
     * @return the result list of category representation
     */
    Result<CategoryRepresentation> findAllCategory(Range range, Map<String, Object> criteria);
}
