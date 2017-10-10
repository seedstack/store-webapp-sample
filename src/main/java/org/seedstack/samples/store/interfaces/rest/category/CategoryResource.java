/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.interfaces.rest.category;

import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.business.assembler.dsl.AggregateNotFoundException;
import org.seedstack.business.domain.LegacyRepository;
import org.seedstack.business.finder.Range;
import org.seedstack.business.finder.Result;
import org.seedstack.business.view.PaginatedView;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.store.domain.model.category.Category;
import org.seedstack.samples.store.interfaces.rest.Paging;
import org.seedstack.samples.store.interfaces.rest.product.ProductRepresentation;
import org.seedstack.samples.store.interfaces.rest.product.ProductRepresentationFinder;
import org.seedstack.seed.transaction.Transactional;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static org.seedstack.business.assembler.AssemblerTypes.MODEL_MAPPER;


@Path("/categories")
@Transactional
@JpaUnit("store")
public class CategoryResource {
    private final CategoryRepresentationFinder categoryRepresentationFinder;
    private final ProductRepresentationFinder productRepresentationFinder;
    private final LegacyRepository<Category, Long> categoryRepository;
    private final FluentAssembler fluentAssembler;
    @Context
    private UriInfo uriInfo;

    @Inject
    public CategoryResource(CategoryRepresentationFinder categoryRepresentationFinder, ProductRepresentationFinder productRepresentationFinder, LegacyRepository<Category, Long> categoryRepository, FluentAssembler fluentAssembler) {
        this.categoryRepresentationFinder = categoryRepresentationFinder;
        this.productRepresentationFinder = productRepresentationFinder;
        this.categoryRepository = categoryRepository;
        this.fluentAssembler = fluentAssembler;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCategories(@QueryParam("searchString") String searchString, @BeanParam Paging paging) {
        Result<CategoryRepresentation> result = categoryRepresentationFinder.find(
                Range.rangeFromPageInfo(paging.getPageIndex(), paging.getPageSize()),
                searchString
        );
        return Response.ok(new PaginatedView<>(result, paging.getPageSize(), paging.getPageIndex())).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{categoryId}/products")
    public Response listProductByCategory(@PathParam("categoryId") long categoryId, @BeanParam Paging paging) {
        Result<ProductRepresentation> result = productRepresentationFinder.findProductsFromCategory(Range.rangeFromPageInfo(paging.getPageIndex(), paging.getPageSize()), categoryId);
        return Response.ok(new PaginatedView<>(result, paging.getPageSize(), paging.getPageIndex())).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCategory(CategoryRepresentation categoryRepresentation) {
        Category category = fluentAssembler.merge(categoryRepresentation).with(MODEL_MAPPER).into(Category.class).fromFactory();

        categoryRepository.persist(category);

        return Response.created(URI.create(uriInfo.getRequestUri() + "/" + category.getEntityId()))
                .entity(fluentAssembler.assemble(category).with(MODEL_MAPPER).to(CategoryRepresentation.class))
                .build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{categoryId}")
    public Response updateCategory(CategoryRepresentation categoryRepresentation, @PathParam("categoryId") long categoryId) {
        if (categoryRepresentation.getId() != categoryId) {
            throw new BadRequestException("Category identifiers from body and URL don't match");
        }

        Category category;
        try {
            category = fluentAssembler.merge(categoryRepresentation).with(MODEL_MAPPER).into(Category.class).fromRepository().orFail();
        } catch (AggregateNotFoundException e) {
            throw new NotFoundException("Category " + categoryId + " not found");
        }
        categoryRepository.save(category);

        return Response.ok(fluentAssembler.assemble(category).with(MODEL_MAPPER).to(CategoryRepresentation.class)).build();
    }

    @DELETE
    @Path("/{categoryId}")
    public Response deleteCategory(@PathParam("categoryId") long categoryId) {
        Category category = categoryRepository.load(categoryId);
        if (category == null) {
            throw new NotFoundException("Category " + categoryId + " not found");
        }

        categoryRepository.delete(category);

        return Response.ok().build();
    }
}
