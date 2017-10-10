/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.interfaces.rest.customer;


import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.business.assembler.dsl.AggregateNotFoundException;
import org.seedstack.business.domain.LegacyRepository;
import org.seedstack.business.finder.Range;
import org.seedstack.business.finder.Result;
import org.seedstack.business.view.PaginatedView;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.store.domain.model.customer.Customer;
import org.seedstack.samples.store.domain.model.customer.CustomerId;
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


@Path("/customers")
@Transactional
@JpaUnit("store")
public class CustomersResource {
    private final CustomerRepresentationFinder customerRepresentationFinder;
    private final FluentAssembler fluentAssembler;
    private final LegacyRepository<Customer, CustomerId> customerRepository;
    @Context
    private UriInfo uriInfo;

    @Inject
    public CustomersResource(CustomerRepresentationFinder customerRepresentationFinder, FluentAssembler fluentAssembler, LegacyRepository<Customer, CustomerId> customerRepository) {
        this.customerRepresentationFinder = customerRepresentationFinder;
        this.fluentAssembler = fluentAssembler;
        this.customerRepository = customerRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCustomers(@QueryParam("searchString") String searchString, @BeanParam Paging paging) {
        Result<CustomerRepresentation> result = customerRepresentationFinder.find(Range.rangeFromPageInfo(paging.getPageIndex(), paging.getPageSize()), searchString);
        return Response.ok(new PaginatedView<>(result, paging.getPageSize(), paging.getPageIndex())).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{customerId}")
    public Response getCustomer(@PathParam("customerId") String customerId) {
        Customer customer = customerRepository.load(new CustomerId(customerId));
        if (customer != null) {
            return Response.ok(fluentAssembler.assemble(customer).to(CustomerRepresentation.class)).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(CustomerRepresentation customerRepresentation) throws URISyntaxException {
        Customer customer = fluentAssembler.merge(customerRepresentation).into(Customer.class).fromFactory();

        customerRepository.persist(customer);

        return Response.created(URI.create(this.uriInfo.getRequestUri().toString() + "/" + customerRepresentation.getId()))
                .entity(fluentAssembler.assemble(customer).to(CustomerRepresentation.class))
                .build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{customerId}")
    public Response updateCustomer(CustomerRepresentation customerRepresentation, @PathParam("customerId") String customerId) {
        if (!customerId.equals(customerRepresentation.getId())) {
            throw new BadRequestException("Customer identifiers from body and URL don't match");
        }

        Customer customer;
        try {
            customer = fluentAssembler.merge(customerRepresentation).into(Customer.class).fromRepository().orFail();
        } catch (AggregateNotFoundException e) {
            throw new NotFoundException("Customer " + customerId + " not found");
        }
        customerRepository.save(customer);

        return Response.ok(fluentAssembler.assemble(customer).to(CustomerRepresentation.class)).build();
    }

    @DELETE
    @Path("/{customerId}")
    public Response deleteCategory(@PathParam("customerId") String customerId) {
        Customer customer = customerRepository.load(new CustomerId(customerId));
        if (customer == null) {
            throw new NotFoundException("Customer " + customerId + " not found");
        }

        customerRepository.delete(customer);

        return Response.status(Status.OK).build();
    }
}
