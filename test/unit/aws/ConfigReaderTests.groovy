package aws

import groovy.util.ConfigObject
import groovy.util.ConfigSlurper
import grails.test.GrailsUnitTestCase
import grails.plugin.aws.util.ConfigReader

class ConfigReaderTests extends GrailsUnitTestCase {
	
    protected void setUp() {
        super.setUp()
		ConfigReader.metaClass.getLog = { -> new grails.plugin.aws.util.MockLogger() }
    }

    protected void tearDown() {
        super.tearDown()
    }

	def testConfig = """
	grails {
		plugin {
			aws {
				credentials {
					accessKey = "my-access-key"
					secretKey = "my-secret-key"
				}
				s3 {
					bucket = "grails-aws-plugin-bucket"
					rrs    = false
					acl    = "public"
				}
				ses {
					from     = "default-from@server.com"
				}
			}
		}
	}
	"""

    void testConfiguration() {
	
		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << testConfig
		
		def cr = new ConfigReader(new ConfigSlurper().parse(tmpFile.toURL()))
		
		assertEquals "my-access-key",   cr.read("grails.plugin.aws.credentials.accessKey", "different-key-for-default-value")
		assertEquals "key-not-defined", cr.read("grails.plugin.aws.credentials.properties", "key-not-defined")
		assertEquals "true",            cr.read("grails.plugin.aws.ses.enabled", "true")
		assertEquals "false",           cr.read("grails.plugin.aws.s3.rrs", "true")
		assertEquals null,              cr.read("grails.plugin.aws.this.entry.does.not.exist")
    }
}
