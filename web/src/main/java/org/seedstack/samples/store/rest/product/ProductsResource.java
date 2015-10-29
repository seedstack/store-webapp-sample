/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.rest.product;

import org.apache.commons.lang.StringUtils;
import org.javatuples.Pair;
import org.seedstack.business.api.interfaces.assembler.AssemblerTypes;
import org.seedstack.business.api.interfaces.assembler.FluentAssembler;
import org.seedstack.business.api.interfaces.assembler.dsl.AggregateNotFoundException;
import org.seedstack.business.api.interfaces.finder.Range;
import org.seedstack.business.api.interfaces.finder.Result;
import org.seedstack.business.api.interfaces.view.PaginatedView;
import org.seedstack.samples.store.domain.product.Product;
import org.seedstack.samples.store.domain.product.ProductRepository;
import org.seedstack.seed.core.api.Logging;
import org.seedstack.seed.persistence.jpa.api.JpaUnit;
import org.seedstack.seed.transaction.api.Transactional;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Product resource.
 */
@Path("/products")
@Transactional
@JpaUnit("ecommerce-domain")
public class ProductsResource {
    @Logging
    private static Logger logger;
    @Inject
    private ProductRepresentationFinder productRepresentationFinder;
    @Inject
    private FluentAssembler fluentAssembler;
    @Inject
    ProductRepository productRepository;

    /**
     * Gets Products with pagination. and search parameters
     *
     * @param categoryId   the categoryId
     * @param searchString the searchString
     * @param pageIndex    the pageIndex
     * @param pageSize     the pageSize
     * @return Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response products(@QueryParam("categoryId") Long categoryId,
                             @QueryParam("searchString") String searchString,
                             @QueryParam("pageIndex") int pageIndex,
                             @QueryParam("pageSize") int pageSize) {
        Map<String, Object> criteria = new HashMap<String, Object>();

        // criteria map is used to build where clause
        // p.entityId, p.designation, p.summary, p.details
        String likeOperator = "like";
        String likeValue = "'%" + searchString + "%'";
        Pair<String, String> operatorValueCondition = new Pair<String, String>(
                likeOperator, likeValue);

        if (StringUtils.isNotEmpty(searchString)) {

            // criteria with operator and value
            criteria.put("entityId", operatorValueCondition);
            criteria.put("designation", operatorValueCondition);
            criteria.put("summary", operatorValueCondition);
            criteria.put("details", operatorValueCondition);
        }

        if (categoryId != null) {
            // criteria with value (defaulting to equal operator)
            criteria.put("categoryId", categoryId);
        }

        Result<ProductRepresentation> result = productRepresentationFinder.findAllProducts(
                Range.rangeFromPageInfo(pageIndex, pageSize), criteria);
        PaginatedView<ProductRepresentation> paginatedView = new PaginatedView<ProductRepresentation>(
                result, pageSize, pageIndex);
        return Response.ok(paginatedView).build();
    }

    /**
     * Gets a product by id.
     *
     * @param productId the product id
     * @return Response
     */
    @GET
    @Path("/{productId: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response product(@PathParam("productId") long productId) {
        ProductRepresentation productFinderProductById = productRepresentationFinder
                .findProductById(productId);
        productRepresentationFinder.findAllProducts();
        if (productFinderProductById == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(productFinderProductById).build();
    }

    /**
     * Updates a product.
     *
     * @param productRepresentation the product representation
     * @param productId             the product id
     * @return Response
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{productId: [0-9]+}")
    public Response updateProduct(ProductRepresentation productRepresentation, @PathParam("productId") long productId) {
        if (productRepresentation.getId() != productId) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).entity("Product identifier cannot be updated").build();
        }

        Product product;
        try {
            product = fluentAssembler.merge(productRepresentation).into(Product.class).fromRepository().orFail();
        } catch (AggregateNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        product = productRepository.saveProduct(product);

        if (product == null) {
            logger.info("Product with id {} not updated",
                    productRepresentation.getId());
            return Response.status(Status.NOT_MODIFIED).build();
        }
        ProductRepresentation productRepresentation1 = fluentAssembler.assemble(product).with(AssemblerTypes.MODEL_MAPPER).to(ProductRepresentation.class);
        return Response.ok(productRepresentation1).build();
    }

    /**
     * Adds a product.
     *
     * @param productRepresentation the product representation
     * @param uriInfo               the uri info
     * @return Response
     * @throws URISyntaxException if an URI error occurs
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(ProductRepresentation productRepresentation,
                                  @Context UriInfo uriInfo) throws URISyntaxException {

        Product product = fluentAssembler.merge(productRepresentation).into(Product.class).fromFactory();
        productRepository.persistProduct(product);

        if (product == null) {
            logger.info("Product with id {} not updated",
                    productRepresentation.getId());
            return Response.status(Status.NOT_ACCEPTABLE).build();
        }

        ProductRepresentation productRepresentation1 = fluentAssembler.assemble(product).to(ProductRepresentation.class);
        return Response.created(URI.create(uriInfo.getRequestUri().toString() + "/" + productRepresentation1.getId())).entity(productRepresentation1).build();
    }

    /**
     * Deletes a product.
     *
     * @param productId the product id
     * @return Response
     */
    @DELETE
    @Path("/{productId: [0-9]+}")
    public Response deleteProduct(@PathParam("productId") long productId) {
        Product product = productRepository.load(productId);

        if (product != null) {
            productRepository.delete(product);
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.status(Status.OK).build();

    }

}
