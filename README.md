Grails AWS Plugin
===========
[![Build Status](https://travis-ci.org/grails-aws/grails-aws.png?branch=master)](https://travis-ci.org/grails-aws/grails-aws)

## Introduction

A Grails plugin for interacting with Amazon Web Services (AWS)

## Getting Started

Include the plugin in the `BuildConfig.groovy`:

        plugins {
            compile ':aws:1.2.12.4'
        }
        
        
Use the Grails bean property override syntax to specify the AWS accessKey and secretKey.  You can do this by modifying `Config.groovy` or any file available in `grails.config.locations`.

        grails {
            plugin {
                aws {
                    credentials {
                        accessKey = "your-access-key-here"
                        secretKey = "your-secret-key-here"
                    }
                }
            }
        }


## Documentation

Docs: http://grails-aws.github.com/grails-aws

## Contributing

- Fork it.
- Create a branch (`git checkout -b my_markup`)
- Commit your changes (`git commit -am "Added Snarkdown"`)
- Push to the branch (`git push origin my_markup`)
- Create an [Issue](issues/new) with a link to your branch
- Enjoy a refreshing Diet Coke and wait


## Changelog

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
