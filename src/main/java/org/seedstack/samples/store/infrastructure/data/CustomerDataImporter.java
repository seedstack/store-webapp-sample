/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.infrastructure.data;

import org.seedstack.business.assembler.AssemblerTypes;
import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.business.domain.Repository;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.store.domain.model.customer.Customer;
import org.seedstack.samples.store.domain.model.customer.CustomerId;
import org.seedstack.seed.DataImporter;
import org.seedstack.seed.DataSet;
import org.seedstack.seed.Logging;
import org.seedstack.seed.transaction.Transactional;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@DataSet(group = "store", name = "customers")
public class CustomerDataImporter implements DataImporter<CustomerDTO> {
    private final Set<CustomerDTO> staging = new HashSet<>();
    private final Repository<Customer, CustomerId> customerRepository;
    private final FluentAssembler fluentAssembler;
    @Logging
    private Logger logger;

    @Inject
    public CustomerDataImporter(Repository<Customer, CustomerId> customerRepository, FluentAssembler fluentAssembler) {
        this.customerRepository = customerRepository;
        this.fluentAssembler = fluentAssembler;
    }

    @Transactional
    @JpaUnit("store")
    @Override
    public boolean isInitialized() {
        return customerRepository.count() != 0;
    }

    @Override
    public void importData(CustomerDTO data) {
        staging.add(data);
    }

    @Transactional
    @JpaUnit("store")
    @Override
    public void commit(boolean clear) {
        if (clear) {
            logger.info("Clearing customers");
            customerRepository.clear();
        }

        logger.info("staging size: " + staging.size());

        for (CustomerDTO customerDTO : staging) {
            customerRepository.persist(fluentAssembler.merge(customerDTO).with(AssemblerTypes.MODEL_MAPPER).into(Customer.class).fromFactory());
        }

        logger.info("Import of customers completed");
        staging.clear();
    }

    @Override
    public void rollback() {
        staging.clear();
        logger.warn("Rollback occurred during customer import");
    }
}
