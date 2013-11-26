log4j = {
	error 'org.codehaus.groovy.grails',
	      'org.springframework',
	      'org.hibernate',
	      'net.sf.ehcache.hibernate'
}

grails.doc.authors = 'Lucas Teixeira, Jay Prall'
grails.doc.license = 'Apache License'
grails.doc.title = 'Grails AWS Plugin'

grails {
	plugin {
		aws {
			credentials {
				//accessKey = "access-key"
				//secretKey = "secret-key"
				properties = "/Users/blanq01/Desktop/AwsCredentials.properties"
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
