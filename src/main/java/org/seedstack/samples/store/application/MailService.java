/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.samples.store.application;

import org.seedstack.business.Service;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * Mail application service.
 */
@Service
public interface MailService {

    /**
     * Sends an email.
     *
     * @param subject the subject of the mail
     * @param to      the recipient
     * @param values  the template values
     * @throws MessagingException if an exception occurs during the sending
     */
    public void sendMail(String subject, String to, Map<String, Object> values) throws MessagingException;
}