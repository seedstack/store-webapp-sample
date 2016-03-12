/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.data.customer;

import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.samples.store.domain.customer.Customer;
import org.seedstack.samples.store.domain.customer.CustomerRepository;
import org.seedstack.seed.DataImporter;
import org.seedstack.seed.DataSet;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.seed.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * @author adrien.lauer@mpsa.com
 */
@DataSet(group = "ecommerce-domain", name = "customers")
public class CustomerDataImporter implements DataImporter<CustomerDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDataImporter.class);

    @Inject
    private CustomerRepository customerRepository;

    private Set<CustomerDTO> staging = new HashSet<CustomerDTO>();

    @Inject
    private FluentAssembler fluentAssembler;

    @Transactional
    @JpaUnit("ecommerce-domain")
    @Override
    public boolean isInitialized() {
        return customerRepository.count() != 0;
    }

    @Override
    public void importData(CustomerDTO data) {
        staging.add(data);
    }

    @Transactional
    @JpaUnit("ecommerce-domain")
    @Override
    public void commit(boolean clear) {
        if (clear) {
            LOGGER.info("Clearing customers");
            customerRepository.deleteAll();
        }

        LOGGER.info("staging size: " + staging.size());

        for (CustomerDTO customerDTO : staging) {
            customerRepository.persist(fluentAssembler.merge(customerDTO).into(Customer.class).fromFactory());
        }

        LOGGER.info("Import of customers completed");
        staging.clear();
    }

    @Override
    public void rollback() {
        staging.clear();
        LOGGER.warn("Rollback occurred during customer import");
    }
}
