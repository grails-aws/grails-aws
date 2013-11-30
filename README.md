Grails AWS Plugin
===========
[![Build Status](https://travis-ci.org/grails-aws/grails-aws.png?branch=master)](https://travis-ci.org/grails-aws/grails-aws)

## Getting Started

Include the plugin in the `BuildConfig.groovy`:

        plugins {
            compile ':aws:1.2.12.3'
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

##Contributing
# Fork it.
# Create a branch (`git checkout -b my_markup`)
# Commit your changes (`git commit -am "Added Snarkdown"`)
# Push to the branch (`git push origin my_markup`)
# Create an [Issue][1] with a link to your branch
# Enjoy a refreshing Diet Coke and wait

## TODO

# Amazon Cloudfront
* Cloudfront Distribution creation
# Amazon SNS

* Basic integration with Amazon SNS

# Amazon SQS
* Basic integration with Amazon SQS with Typica (http://code.google.com/p/typica/wiki/TypicaSampleCode)

# Amazon Beanstalk
* Scripts for allowing deploy directly to Amazon Beanstalk infrastructure
   

## Found a bug?

# Open an issue on Github

