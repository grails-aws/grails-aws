package grails.plugin.aws.meta

import static org.junit.Assert.*
import org.junit.*

class AwsPluginSupportIntegrationTests {

	def s3FileUpload
	
    @Test
    void injectS3FileUploadBeanDefaultsRRSPropertyToTrue() {
		assert s3FileUpload.rrs instanceof Boolean
		assert s3FileUpload.rrs == true
    }
}
