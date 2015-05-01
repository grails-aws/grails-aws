# Amazon Web Services Grails Plugin
[![Build Status](https://travis-ci.org/grails-aws/grails-aws.png?branch=master)][travis]

[travis]: https://travis-ci.org/grails-aws/grails-aws

#### A Grails plugin for interacting with Amazon Web Services
Currently supports S3 and S3S

## Getting Started

Add the plugin to the plugins section of `grails-app/conf/BuildConfig.groovy`
```groovy
plugins {
    compile ':aws:1.9.13.3'
}
```

Configure the plugin in `grails-app/conf/Config.groovy`
```groovy
grails {
    plugin {
        aws {
            credentials {
                accessKey = "your-access-key"
                secretKey = "your-secret-key"
            }
            s3 {
                bucket = "default-bucket-name"
            }
        }
    }
}
```

## Documentation

* [Reference Documentation](http://grails-aws.github.io/grails-aws/1.7.5.0/)
* [Groovy Documentation](http://grails-aws.github.io/grails-aws/1.7.5.0/gapi/)


## Contributing

- Fork it.
- Create a branch (`git checkout -b my_markup`)
- Commit your changes (`git commit -am "Added Snarkdown"`)
- Push to the branch (`git push origin my_markup`)
- Create an [Issue](issues/new) with a link to your branch
- Enjoy a refreshing Diet Coke and wait


## Changelog

1.9.13.0 - January 5 2015
* Upgrade to AWS SDK version 1.9.13

1.7.5.0 - April 11 2014
* Upgrade to AWS SDK version 1.7.5
* Ability to configure SES AWS region

1.6.7.4 - January 15 2014
* Add the ability to start SWF executions
* Upgrade to httpclient 4.2

1.6.7.1 - December 10 2013
* Update to AWS SDK version 1.6.7

1.2.12.4 - November 30 2013
* Added support to access only file metadata on S3
* Remove usages of the deprecated ConfigurationHolder which added warnings in grails logs
* Added support to specify charset for SES

1.2.12.3 - September 30 2012
* Add support for S3 Server Side Encryption

1.1.9.2 - May 10 2011
* Release notes: Fixed bug that was always storing the past sent mail addresses in next mail messages.


## TODO

- Amazon Cloudfront * Cloudfront Distribution creation
- Amazon SNS * Basic integration with Amazon SNS
- Amazon SQS * Basic integration with Amazon SQS with Typica (http://code.google.com/p/typica/wiki/TypicaSampleCode)
- Amazon Beanstalk * Scripts for allowing deploy directly to Amazon Beanstalk infrastructure
