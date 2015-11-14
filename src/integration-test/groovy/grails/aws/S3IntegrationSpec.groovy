package grails.aws

import grails.test.mixin.integration.Integration
import org.jets3t.service.S3ServiceException
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Ignore
import spock.lang.Specification

@Integration
@Ignore
class S3IntegrationSpec extends Specification {

	@Autowired
	def aws

	def "getOnAMissingFileThrowsS3ServiceException"() {
		when:
		aws.s3().on('missing-bucket').get('missing-file.txt')

		then:
		thrown(S3ServiceException)
	}

	def "fileS3UploadThrowsS3ServiceException"() {
		given:
		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile.deleteOnExit()

		when:
		def uploadedFile = tmpFile.s3upload {
			bucket "missing-bucket"
		}

		then:
		thrown(S3ServiceException)
//		message == "S3 Error Message."
	}

	void "inputStreamS3UploadThrowsS3ServiceException"() {
		given:
		def mockInputStream = new ByteArrayInputStream('mockInputStream'.getBytes())
		def mockFilename = 'destinationS3Key.txt'

		when:
		def uploadedFile = mockInputStream.s3upload(mockFilename) {
			bucket "missing-bucket"
		}

		then:
		thrown(S3ServiceException)
//		message == "S3 Error Message."
	}

}
