/*
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.samples.store.interfaces.rest.category;

import com.google.common.base.Strings;
import java.net.URI;
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
import org.seedstack.business.assembler.dsl.FluentAssembler;
import org.seedstack.business.domain.AggregateNotFoundException;
import org.seedstack.business.domain.Repository;
import org.seedstack.business.modelmapper.ModelMapper;
import org.seedstack.business.pagination.Page;
import org.seedstack.business.pagination.dsl.Paginator;
import org.seedstack.business.specification.Specification;
import org.seedstack.business.specification.dsl.SpecificationBuilder;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.store.domain.model.category.Category;
import org.seedstack.samples.store.domain.model.product.Product;
import org.seedstack.samples.store.interfaces.rest.Paging;
import org.seedstack.samples.store.interfaces.rest.product.ProductRepresentation;
import org.seedstack.seed.transaction.Transactional;

@Path("/categories")
@Transactional
@JpaUnit("store")
public class CategoryResource {

    @Inject
    private Repository<Category, Long> categoryRepository;
    @Inject
    private Repository<Product, Long> productLongRepository;
    @Inject
    private FluentAssembler fluentAssembler;
    @Inject
    private SpecificationBuilder specificationBuilder;
    @Inject
    private Paginator paginator;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Page<CategoryRepresentation> listCategories(@QueryParam("searchString") String searchString,
            @BeanParam Paging paging) {
        return fluentAssembler.assemble(paginator.paginate(categoryRepository)
                .byPage(paging.getPageIndex())
                .ofSize(paging.getPageSize())
                .matching(buildFilteringSpecification(searchString)))
                .toPageOf(CategoryRepresentation.class);
    }

    private Specification<Category> buildFilteringSpecification(String searchString) {
        if (!Strings.isNullOrEmpty(searchString)) {
            return specificationBuilder.of(Category.class)
                    .property("name").matching("*" + searchString + "*")
                    .build();
        } else {
            return specificationBuilder.of(Category.class)
                    .all()
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{categoryId}/products")
    public Page<ProductRepresentation> listProductByCategory(
            @PathParam("categoryId") long categoryId,
            @BeanParam Paging paging) {
        return fluentAssembler.assemble(paginator.paginate(productLongRepository)
                .byPage(paging.getPageIndex())
                .ofSize(paging.getPageSize())
                .matching(specificationBuilder.ofAggregate(Product.class)
                        .property("categoryId").equalTo(categoryId)
                        .build()))
                .toPageOf(ProductRepresentation.class);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCategory(CategoryRepresentation categoryRepresentation) {
        Category category = fluentAssembler.merge(categoryRepresentation)
                .into(Category.class)
                .fromFactory();

        categoryRepository.add(category);

        return Response.created(URI.create(uriInfo.getRequestUri() + "/" + category.getId()))
                .entity(fluentAssembler.assemble(category).to(CategoryRepresentation.class))
                .build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{categoryId}")
    public CategoryRepresentation updateCategory(CategoryRepresentation categoryRepresentation,
            @PathParam("categoryId") long categoryId) {
        if (categoryRepresentation.getId() != categoryId) {
            throw new BadRequestException("Category identifiers from body and URL don't match");
        }

        Category category;
        try {
            category = fluentAssembler.merge(categoryRepresentation)
                    .into(Category.class)
                    .fromRepository()
                    .orFail();
        } catch (AggregateNotFoundException e) {
            throw new NotFoundException("Category " + categoryId + " not found");
        }
        categoryRepository.add(category);

        return fluentAssembler.assemble(category).to(CategoryRepresentation.class);
    }

    @DELETE
    @Path("/{categoryId}")
    public void deleteCategory(@PathParam("categoryId") long categoryId) {
        try {
            categoryRepository.remove(categoryId);
        } catch (AggregateNotFoundException e) {
            throw new NotFoundException("Category " + categoryId + " not found", e);
        }
    }
}
