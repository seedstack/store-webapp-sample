/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.interfaces.rest.product;

import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.business.assembler.dsl.AggregateNotFoundException;
import org.seedstack.business.domain.LegacyRepository;
import org.seedstack.business.finder.Range;
import org.seedstack.business.view.PaginatedView;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.store.domain.model.product.Product;
import org.seedstack.samples.store.interfaces.rest.Paging;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

import static org.seedstack.business.assembler.AssemblerTypes.MODEL_MAPPER;

@Path("/products")
@Transactional
@JpaUnit("store")
public class ProductsResource {
    private final ProductRepresentationFinder productRepresentationFinder;
    private final FluentAssembler fluentAssembler;
    private final LegacyRepository<Product, Long> productRepository;
    @Context
    private UriInfo uriInfo;

    @Inject
    public ProductsResource(ProductRepresentationFinder productRepresentationFinder, FluentAssembler fluentAssembler, LegacyRepository<Product, Long> productRepository) {
        this.productRepresentationFinder = productRepresentationFinder;
        this.fluentAssembler = fluentAssembler;
        this.productRepository = productRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listProducts(@QueryParam("searchString") String searchString, @BeanParam Paging paging) {
        return Response.ok(new PaginatedView<>(
                productRepresentationFinder.findProducts(Range.rangeFromPageInfo(paging.getPageIndex(), paging.getPageSize()), searchString),
                paging.getPageSize(),
                paging.getPageIndex())
        ).build();
    }

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("productId") long productId) {
        Product product = productRepository.load(productId);
        if (product == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(fluentAssembler.assemble(product).with(MODEL_MAPPER).to(ProductRepresentation.class)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(ProductRepresentation productRepresentation) throws URISyntaxException {
        Product product = fluentAssembler.merge(productRepresentation).with(MODEL_MAPPER).into(Product.class).fromFactory();

        productRepository.persist(product);

        return Response.created(URI.create(uriInfo.getRequestUri().toString() + "/" + product.getEntityId()))
                .entity(fluentAssembler.assemble(product).with(MODEL_MAPPER).to(ProductRepresentation.class))
                .build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{productId}")
    public Response updateProduct(ProductRepresentation productRepresentation, @PathParam("productId") long productId) {
        if (productRepresentation.getId() != productId) {
            throw new BadRequestException("Product identifiers from body and URL don't match");
        }

        Product product;
        try {
            product = fluentAssembler.merge(productRepresentation).with(MODEL_MAPPER).into(Product.class).fromRepository().orFail();
        } catch (AggregateNotFoundException e) {
            throw new NotFoundException("Product " + productId + " not found");
        }
        product = productRepository.save(product);

        return Response.ok(fluentAssembler.assemble(product).with(MODEL_MAPPER).to(ProductRepresentation.class))
                .build();
    }

    @DELETE
    @Path("/{productId}")
    public Response deleteProduct(@PathParam("productId") long productId) {
        Product product = productRepository.load(productId);
        if (product == null) {
            throw new NotFoundException("Product " + productId + " not found");
        }

        productRepository.delete(product);

        return Response.status(Status.OK).build();
    }
}
