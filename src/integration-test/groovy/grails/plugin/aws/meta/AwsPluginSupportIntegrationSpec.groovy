package grails.plugin.aws.meta

import grails.plugin.aws.ses.SendSesMail
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Ignore
import spock.lang.Specification

@Integration
@Rollback
@Ignore
class AwsPluginSupportIntegrationSpec extends Specification {

	@Autowired
	def s3FileUpload
	@Autowired
	def credentialsHolder
	@Autowired
	def sendSesMail

	def "injectS3FileUploadBeanDefaultsRRSPropertyToTrue"() {
		expect:
		s3FileUpload.rrs instanceof Boolean
		s3FileUpload.rrs == true
	}

	def "injectCredentialsHolderProperties"() {
		// These are read from the Config.groovy file
		// this file is only used for aws plugin development and testing
		expect:
		credentialsHolder.accessKey == "fake-access-key"
		credentialsHolder.secretKey == "fake-secret-key"
		credentialsHolder.properties == null
	}

	def "sesMailRegionDefaultsToUSEast1"() {
		expect
		sendSesMail.region == SendSesMail.US__EAST_1

	}
}
