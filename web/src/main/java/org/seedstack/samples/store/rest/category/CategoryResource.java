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

import org.apache.commons.lang.StringUtils;
import org.javatuples.Pair;
import org.seedstack.business.api.interfaces.assembler.FluentAssembler;
import org.seedstack.business.api.interfaces.assembler.dsl.AggregateNotFoundException;
import org.seedstack.business.api.interfaces.finder.Range;
import org.seedstack.business.api.interfaces.finder.Result;
import org.seedstack.business.api.interfaces.view.ChunkedView;
import org.seedstack.business.api.interfaces.view.PaginatedView;
import org.seedstack.samples.store.domain.category.Category;
import org.seedstack.samples.store.domain.category.CategoryRepository;
import org.seedstack.samples.store.rest.product.ProductRepresentation;
import org.seedstack.samples.store.rest.product.ProductRepresentationFinder;
import org.seedstack.seed.persistence.jpa.api.JpaUnit;
import org.seedstack.seed.transaction.api.Transactional;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.seedstack.business.api.interfaces.assembler.AssemblerTypes.MODEL_MAPPER;

/**
 * A REST resource to manage categories.
 */
@Path("/categories")
@Transactional
@JpaUnit("ecommerce-domain")
public class CategoryResource {

    @Inject
    private CategoryRepresentationFinder categoryRepresentationFinder;

    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private ProductRepresentationFinder productRepresentationFinder;

    @Inject
    private FluentAssembler fluentAssembler;

    @Context
    private UriInfo uriInfo;

    /**
     * Gets the category list with pagination and search parameters.
     *
     * @param searchString the search string
     * @param pageIndex    the page index
     * @param pageSize     the page size
     * @param chunkOffset  the chunk offset
     * @param chunkSize    the chunk size
     * @return the paginated list of categories
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@QueryParam("searchString") String searchString,
                         @QueryParam("pageIndex") Long pageIndex,
                         @QueryParam("pageSize") Integer pageSize,
                         @QueryParam("chunkOffset") Long chunkOffset,
                         @QueryParam("chunkSize") Integer chunkSize) {

        Map<String, Object> criteria = new HashMap<String, Object>();

        // criteria map is used to build where clause
        if (StringUtils.isNotEmpty(searchString)) {
            // with field names and values for equal : ie. put(field,value)
            criteria.put("categoryId", new Pair<String, String>("like", "'%" + searchString + "%'"));
            // else a Pair is required to provide the expected operator (eg.
            // "like") : ie. put(field, Pair(operator,value))
            criteria.put("name", new Pair<String, String>("like", "'%" + searchString + "%'"));
        }

        // use pages
        if (pageIndex != null && pageSize != null) {
            Range range = Range.rangeFromPageInfo(pageIndex, pageSize);
            Result<CategoryRepresentation> result = categoryRepresentationFinder.findAllCategory(range, criteria);

            PaginatedView<CategoryRepresentation> paginatedView = new PaginatedView<CategoryRepresentation>(result, pageSize, pageIndex);
            return Response.ok(paginatedView).build();
        }

        // or use chunks
        if (chunkOffset != null && chunkSize != null) {
            Range range = Range.rangeFromChunkInfo(chunkOffset, chunkSize);
            Result<CategoryRepresentation> result = categoryRepresentationFinder.findAllCategory(range, criteria);

            ChunkedView<CategoryRepresentation> chunkedView = new ChunkedView<CategoryRepresentation>(result, chunkOffset, chunkSize);
            return Response.ok(chunkedView).build();
        }

        return Response.ok(categoryRepresentationFinder.findAllCategory()).build();
    }

    /**
     * Gets the products of a specific category
     *
     * @param categoryId the category id
     * @return the paginated list of products by category
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{categoryId}/products")
    public Response listProductByCategory(@PathParam("categoryId") Long categoryId,
                                          @DefaultValue("0") @QueryParam("pageIndex") int pageIndex,
                                          @DefaultValue("10") @QueryParam("pageSize") int pageSize) {

        Map<String, Object> criteria = new HashMap<String, Object>();

        if (categoryId != null) {
            criteria.put("categoryId", categoryId); // add a criteria to filter on categories
        }

        Result<ProductRepresentation> result = productRepresentationFinder.findAllProducts(Range.rangeFromPageInfo(pageIndex, pageSize), criteria);
        PaginatedView<ProductRepresentation> paginatedView = new PaginatedView<ProductRepresentation>(result, pageSize, pageIndex);

        return Response.ok(paginatedView).build();
    }

    /**
     * Creates a category
     *
     * @param categoryRepresentation the category representation
     * @return the created category
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(CategoryRepresentation categoryRepresentation) {
        Category category = fluentAssembler.merge(categoryRepresentation).with(MODEL_MAPPER).into(Category.class).fromFactory();
        categoryRepository.persistCategory(category);

        CategoryRepresentation categoryRepresentation1;
        categoryRepresentation1 = fluentAssembler.assemble(category).with(MODEL_MAPPER).to(CategoryRepresentation.class);

        return Response.created(URI.create(uriInfo.getRequestUri() + "/" + category.getEntityId())).entity(categoryRepresentation1).build();
    }

    /**
     * Updates a category.
     *
     * @param categoryRepresentation the category representation
     * @param categoryId             the category id
     * @return the updated category or 400 if the request was malformed or 404 if the category doesn't exist
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{categoryId}")
    public Response update(CategoryRepresentation categoryRepresentation, @PathParam("categoryId") long categoryId) {
        if (categoryRepresentation.getId() != categoryId) { // bad request: the ids don't match
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).entity("Category identifier cannot be updated").build();
        }

        Category category;
        try {
            category = fluentAssembler.merge(categoryRepresentation).with(MODEL_MAPPER).into(Category.class).fromRepository().orFail();
        } catch (AggregateNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        category = categoryRepository.saveCategory(category);

        if (category == null) {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }

        CategoryRepresentation categoryRepresentation1 = fluentAssembler.assemble(category).with(MODEL_MAPPER).to(CategoryRepresentation.class);
        return Response.ok(categoryRepresentation1).build();
    }

    /**
     * Deletes a category.
     *
     * @param categoryId the category id
     * @return ok or 404 if the category doesn't exist
     */
    @DELETE
    @Path("/{categoryId}")
    public Response deleteCategory(@PathParam("categoryId") long categoryId) {
        Category category = categoryRepository.load(categoryId);

        if (category != null) {
            categoryRepository.delete(category);
        } else { // can't delete a nonexistent category
            Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().build();
    }
}
