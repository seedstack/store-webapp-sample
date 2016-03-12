/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.rest.customer;


import org.apache.commons.lang.StringUtils;
import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.business.assembler.dsl.AggregateNotFoundException;
import org.seedstack.business.finder.Range;
import org.seedstack.business.finder.Result;
import org.seedstack.business.view.PaginatedView;
import org.seedstack.samples.store.domain.customer.Customer;
import org.seedstack.samples.store.domain.customer.CustomerFactory;
import org.seedstack.samples.store.domain.customer.CustomerRepository;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.seed.transaction.Transactional;

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
 * CustomersResource
 */
@Path("/customers")
@Transactional
@JpaUnit("ecommerce-domain")
public class CustomersResource {

    @Inject
    private CustomerRepresentationFinder customerRepresentationFinder;

    @Inject
    private FluentAssembler fluentAssembler;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerFactory customerFactory;

    @Context
    private UriInfo uriInfo;

    /**
     * Gets Customers with pagination and search parameters.
     *
     * @param searchString the string to search
     * @param pageIndex    the page index
     * @param pageSize     the page size
     * @return a paginated view of customer representations
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response customers(@QueryParam("searchString") String searchString, @QueryParam("pageIndex") int pageIndex, @QueryParam("pageSize") int pageSize) {
        Map<String, Object> criteria = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(searchString)) {
            criteria.put("searchString", searchString);
        }

        Result<CustomerRepresentation> result = customerRepresentationFinder.findAllCustomers(Range.rangeFromPageInfo(pageIndex, pageSize), criteria);
        PaginatedView<CustomerRepresentation> paginatedView = new PaginatedView<CustomerRepresentation>(result, pageSize, pageIndex);

        return Response.ok(paginatedView).build();
    }

    /**
     * Gets a customer by id.
     *
     * @param customerId the customer id
     * @return a customer representation or 404 if not found
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{customerId}")
    public Response customers(@PathParam("customerId") String customerId) {
        CustomerRepresentation customer = customerRepresentationFinder.findCustomerById(customerId);
        if (customer != null) {
            return Response.ok(customer).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    /**
     * Updates a Customer.
     *
     * @param customerRepresentation the customer representation
     * @param customerId             the customer id
     * @return the updated customer
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{customerId}")
    public Response updateCustomer(CustomerRepresentation customerRepresentation, @PathParam("customerId") String customerId) {
        if (!customerId.equals(customerRepresentation.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).entity("Customer email cannot be updated").build();
        }

        Customer customer;
        try {
            customer = fluentAssembler.merge(customerRepresentation).into(Customer.class).fromRepository().orFail();
        } catch (AggregateNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        customer = customerRepository.save(customer);

        if (customer == null) {
            return Response.status(Status.NOT_MODIFIED).build();
        }

        CustomerRepresentation customerRepresentation1 = fluentAssembler.assemble(customer).to(CustomerRepresentation.class);
        return Response.ok(customerRepresentation1).build();
    }

    /**
     * Adds a Customer.
     *
     * @param customerRepresentation the customer representation
     * @return the new customer
     * @throws URISyntaxException if an URI error occurs
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(CustomerRepresentation customerRepresentation) throws URISyntaxException {
        Customer customer = fluentAssembler.merge(customerRepresentation).into(Customer.class).fromFactory();

        customerRepository.persist(customer);

        CustomerRepresentation customerRepresentation1 = fluentAssembler.assemble(customer).to(CustomerRepresentation.class);
        return Response.created(URI.create(this.uriInfo.getRequestUri().toString() + "/" + customerRepresentation.getId())).entity(customerRepresentation1).build();
    }

    /**
     * Deletes a Customer.
     *
     * @param customerId the customer id
     * @return ok or 404 if the customer did exist
     */
    @DELETE
    @Path("/{customerId}")
    public Response deleteCategory(@PathParam("customerId") String customerId) {
        Customer customer = customerRepository.load(customerFactory.createCustomerId(customerId));

        if (customer != null) {
            customerRepository.delete(customer);
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.status(Status.OK).build();
    }

}
