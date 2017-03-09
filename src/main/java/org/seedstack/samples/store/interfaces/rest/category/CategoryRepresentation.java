/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.interfaces.rest.category;


import org.seedstack.business.assembler.DtoOf;
import org.seedstack.business.assembler.MatchingEntityId;
import org.seedstack.samples.store.domain.model.category.Category;

@DtoOf(Category.class)
public class CategoryRepresentation {
    private Long id;
    private String name;
    private String urlImg;

    public CategoryRepresentation() {
    }

    public CategoryRepresentation(long id, String name, String urlImg) {
        this.id = id;
        this.name = name;
        this.urlImg = urlImg;
    }

    @MatchingEntityId
    public Long getId() {
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
}
