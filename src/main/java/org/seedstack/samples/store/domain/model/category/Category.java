/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 * <p>
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.samples.store.domain.model.category;

import javax.persistence.Entity;
import javax.persistence.Id;
import org.seedstack.business.domain.BaseAggregateRoot;
import org.seedstack.business.domain.Identity;
import org.seedstack.business.util.inmemory.InMemorySequenceGenerator;

@Entity
public class Category extends BaseAggregateRoot<Long> {
    @Id
    @Identity(generator = InMemorySequenceGenerator.class)
    private Long id;
    private String name;
    private String urlImg;

    private Category() {
        // A private constructor ensures that the category is created through its
        // factory so the identity generator is invoked just after creation
    }

    @Override
    public Long getId() {
        return id;
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
}
