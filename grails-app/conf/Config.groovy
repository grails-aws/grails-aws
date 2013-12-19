log4j = {
    error 'org.codehaus.groovy.grails',
          'org.springframework',
          'org.hibernate',
          'net.sf.ehcache.hibernate'
}

grails.doc.authors = 'Lucas Teixeira, Jay Prall'
grails.doc.license = 'Apache License 2.0'
grails.doc.title = 'Grails AWS Plugin'

// These settings are only used for plugin development
grails {
    plugin {
        aws {
            credentials {
                accessKey = "fake-access-key"
                secretKey = "fake-secret-key"
            }
            s3 {
                bucket = "grails-aws-test-bucket-20110515"
                rrs    = true
                acl    = "public"
            }
            ses {
                from     = "lucastex@gmail.com"
                //enabled  = false
            }
        }
    }
}
