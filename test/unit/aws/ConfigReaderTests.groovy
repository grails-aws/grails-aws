package aws

import grails.plugin.aws.util.ConfigReader
import grails.test.GrailsUnitTestCase
import org.junit.Test

class ConfigReaderTests extends GrailsUnitTestCase {


   private String testConfig = """
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

    @Test
   void configuration() {

      def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
      tmpFile.deleteOnExit()
      tmpFile << testConfig
      def cr = new ConfigReader(new ConfigSlurper().parse(tmpFile.toURI().toURL()))

      assertEquals "my-access-key",   cr.read("grails.plugin.aws.credentials.accessKey", "different-key-for-default-value")
      assertEquals "key-not-defined", cr.read("grails.plugin.aws.credentials.properties", "key-not-defined")
      assertEquals "true",            cr.read("grails.plugin.aws.ses.enabled", "true")
      assertEquals "false",           cr.read("grails.plugin.aws.s3.rrs", "true")
      assertNull                      cr.read("grails.plugin.aws.this.entry.does.not.exist")
   }
}
