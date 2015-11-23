# Store Web application sample 
[![Build status](https://travis-ci.org/seedstack/store-webapp-sample.svg?branch=master)](https://travis-ci.org/seedstack/store-webapp-sample)

A project demonstrating the following features of SeedStack:

* REST resources,
* JPA persistence,
* JavaMail,
* Pagination,
* Static resources serving,
* Simple business code.

# Running it

## Locally

If you have [Maven 3](http://maven.apache.org/) installed, you can clone the repository and run it locally:

    git clone https://github.com/seedstack/store-webapp-sample.git
    cd store-webapp-sample
    mvn clean install && (cd web && mvn seedstack:run)

## On Heroku

Or you can just deploy it on your own Heroku account by clicking this button:

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

# Usage

Just point your favorite browser to the base URL.
**Note that the application is secured with basic authentication. Use demo/demo credentials to log in.**
    
# Copyright and license

This source code is copyrighted by [The SeedStack Authors](https://github.com/seedstack/seedstack/blob/master/AUTHORS) and
released under the terms of the [Mozilla Public License 2.0](https://www.mozilla.org/MPL/2.0/).

