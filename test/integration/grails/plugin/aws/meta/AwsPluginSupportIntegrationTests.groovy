package grails.plugin.aws.meta

import grails.plugin.aws.ses.SendSesMail
import static org.junit.Assert.*
import org.junit.*

class AwsPluginSupportIntegrationTests {

	def s3FileUpload
	def credentialsHolder
	def sendSesMail
	
    @Test
    void injectS3FileUploadBeanDefaultsRRSPropertyToTrue() {
		assert s3FileUpload.rrs instanceof Boolean
		assert s3FileUpload.rrs == true
    }
	
	@Test 
	void injectCredentialsHolderProperties() {
		// These are read from the Config.groovy file
		// this file is only used for aws plugin development and testing

		assert credentialsHolder.accessKey == "fake-access-key"
		assert credentialsHolder.secretKey == "fake-secret-key"
		assert credentialsHolder.properties == null
	}
	
	@Test
	void sesMailRegionDefaultsToUSEast1() {

	    assert sendSesMail.region == SendSesMail.US__EAST_1

	}
}
