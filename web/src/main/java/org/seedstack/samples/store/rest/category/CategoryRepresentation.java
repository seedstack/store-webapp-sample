/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.rest.category;


import org.seedstack.business.assembler.DtoOf;
import org.seedstack.business.assembler.MatchingEntityId;
import org.seedstack.business.assembler.MatchingFactoryParameter;
import org.seedstack.samples.store.domain.category.Category;

/**
 * CategoryRepresentation.
 */
@DtoOf(Category.class) // Required as there is no assembler implementation
public class CategoryRepresentation {

    private Long id;
    private String name;
    private String urlImg;
	/**
	 * constructor
	 */
    public CategoryRepresentation() {
    }
	/**
	 * constructor
	 * @param id the customer id
	 * @param name the customer name
	 * @param urlImg the customer URL image
	 */
    public CategoryRepresentation(Long id, String name, String urlImg) {
        this.id = id;
        this.name = name;
        this.urlImg = urlImg;
    }
	/**
	 * get id
	 * @return Long
	 */
    @MatchingFactoryParameter(index = 0)
    @MatchingEntityId
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
	/**
	 * get name
	 * @return String
	 */
    @MatchingFactoryParameter(index = 1)
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
	/**
	 * get image url
	 * @return String
	 */
    @MatchingFactoryParameter(index = 2)
    public String getUrlImg() {
        return urlImg;
    }

    /**
     * Sets the urlImg.
     * @param urlImg the urlImg to set
     */
    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

}
