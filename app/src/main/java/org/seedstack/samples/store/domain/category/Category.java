/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.category;


import org.seedstack.business.api.domain.BaseAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Category Entity 
 */
@Entity
public class Category extends BaseAggregateRoot<Long> {

    @Id
    private Long categoryId;
    private String name;
    private String urlImg;

    /**
     * Constructor in visibility package because only Factories can create
     * aggregates and entities.
     * <p/>
     * Factories are in the same package so he can access package visibility.
     */
    Category() {
    }

    /**
     * Constructor.
     *
     * @param name the category name
     * @param urlImg the urlImg
     */
    Category(String name, String urlImg) {
        this.name = name;
        this.urlImg = urlImg;
    }

    /**
     * Constructor.
     *
     * @param categoryId the category id
     * @param name the category name
     * @param urlImg the urlImg
     */
    public Category(Long categoryId, String name, String urlImg) {
        super();
        this.categoryId = categoryId;
        this.name = name;
        this.urlImg = urlImg;
    }

	/**
	 * Getter name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter name.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter urlImg.
	 * 
	 * @return the urlImg
	 */
	public String getUrlImg() {
		return urlImg;
	}

	/**
	 * Setter urlImg.
	 * 
	 * @param urlImg the urlImg to set
	 */
	public void setUrlImg(String urlImg) {
		this.urlImg = urlImg;
	}

	/**
	 * Getter entityId.
	 * 
	 * @return the entityId
	 */
    @Override
    public Long getEntityId() {
        return categoryId;
    }

    /**
     * Setter entityId.
     *
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.categoryId = entityId;
    }
}
