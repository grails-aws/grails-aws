package grails.plugin.aws

import grails.plugin.aws.s3.AWSS3Tools
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Ignore
import spock.lang.Specification

@Integration
@Rollback
@Ignore
class AWSGenericToolsIntegrationSpec extends Specification {

	@Autowired
	def aws

	def "s3MethodReturnsHelperToTheS3Service"() {
		expect:
		aws.s3().class == AWSS3Tools
	}
}
