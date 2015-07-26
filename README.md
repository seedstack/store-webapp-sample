# Store Web application sample 

A classic Java Web application based on the e-commerce domain model.

# Run it

If you have [Maven 3](http://maven.apache.org/) installed, you can clone the repository and run it locally with the maven Jetty plugin:

    git clone https://github.com/seedstack/store-webapp-sample.git
    cd store-webapp-sample
    mvn clean install && (cd web && mvn jetty:run)

Or you can just deploy it on your own Heroku account by clicking this button:

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

**Note that the application is secured with basic authentication. Use demo/demo crendentials to log in.**
    
# Copyright and license

This source code is copyrighted by [The SeedStack Authors](https://github.com/seedstack/seedstack/blob/master/AUTHORS) and
released under the terms of the [Mozilla Public License 2.0](https://www.mozilla.org/MPL/2.0/).

