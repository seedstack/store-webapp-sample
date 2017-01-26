/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.rest.product;


import org.seedstack.business.assembler.MatchingEntityId;
import org.seedstack.business.assembler.MatchingFactoryParameter;

/**
 * Representation of a product.
 */
public class ProductRepresentation {
    private Long id;

    private String designation;
    private String summary;
    private String details;
    private String picture;
    private Double price;
    private Long categoryId;
    private String categoryName;

    /**
     * Constructor.
     */
    public ProductRepresentation() {
    }

    /**
     * ProductRepresentation Constructor.
     *
     * @param id          the id
     * @param designation the designation
     * @param summary     the summary
     * @param details     the details
     * @param picture     the picture
     * @param price       the price
     * @param categoryId  the categoryId
     */
    public ProductRepresentation(Long id, String designation, String summary, String details, String picture,
                                 Double price, Long categoryId) {
        super();
        this.id = id;
        this.designation = designation;
        this.summary = summary;
        this.details = details;
        this.picture = picture;
        this.price = price;
        this.categoryId = categoryId;
    }

    /**
     * ProductRepresentation Constructor.
     *
     * @param id           the id
     * @param designation  the designation
     * @param summary      the summary
     * @param details      the details
     * @param picture      the picture
     * @param price        the price
     * @param categoryId   the categoryId
     * @param categoryName the categoryName
     */
    @SuppressWarnings("unchecked")
    public ProductRepresentation(Long id, String designation, String summary, String details, String picture,
                                 Double price, Long categoryId, String categoryName) {
        super();
        this.id = id;
        this.designation = designation;
        this.summary = summary;
        this.details = details;
        this.picture = picture;
        this.price = price;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    /**
     * get id
     *
     * @return Long
     */
    @MatchingFactoryParameter(index = 0)
    @MatchingEntityId
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the Id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * get designation
     *
     * @return String
     */
    @MatchingFactoryParameter(index = 1)
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets the designation.
     *
     * @param designation the Designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * get summary
     *
     * @return String
     */
    @MatchingFactoryParameter(index = 2)
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the summary.
     *
     * @param summary the Summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * get details
     *
     * @return String
     */
    @MatchingFactoryParameter(index = 3)
    public String getDetails() {
        return details;
    }

    /**
     * Sets the details.
     *
     * @param details the Details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * get picture
     *
     * @return String
     */
    @MatchingFactoryParameter(index = 4)
    public String getPicture() {
        return picture;
    }

    /**
     * Sets the picture.
     *
     * @param picture the Picture to set
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * get price
     *
     * @return Double
     */
    @MatchingFactoryParameter(index = 5)
    public Double getPrice() {
        return price;
    }

    /**
     * Sets the price.
     *
     * @param price the Price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * get Category id
     *
     * @return Long
     */
    @MatchingFactoryParameter(index = 6)
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the categoryId.
     *
     * @param categoryId the CategoryId to set
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * get Category name
     *
     * @return String
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the categoryName.
     *
     * @param categoryName the CategoryName to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
