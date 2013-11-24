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

[![http://blanq.github.com/grails-aws]](http://blanq.github.com/grails-aws)

Docs: http://blanq.github.com/grails-aws
