/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.domain.model.category;

import org.seedstack.business.domain.BaseAggregateRoot;
import org.seedstack.business.domain.Identity;
import org.seedstack.business.test.identity.InMemorySequenceHandler;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Category extends BaseAggregateRoot<Long> {
    @Id
    @Identity(handler = InMemorySequenceHandler.class)
    private Long id;
    private String name;
    private String urlImg;

    private Category() {
        // for JPA
    }

    Category(long id) {
        // used by the default factory
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    @Override
    public Long getEntityId() {
        return id;
    }
}
