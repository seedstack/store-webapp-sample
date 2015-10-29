/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.data.category;

import org.seedstack.business.api.interfaces.assembler.MatchingEntityId;
import org.seedstack.business.api.interfaces.assembler.MatchingFactoryParameter;

/**
 * @author adrien.lauer@mpsa.com
 */
public class CategoryDTO {
    private Long id;
    private String name;
    private String img;

    @MatchingEntityId
    @MatchingFactoryParameter(index = 0)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @MatchingFactoryParameter(index = 1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @MatchingFactoryParameter(index = 2)
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
