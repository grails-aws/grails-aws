package grails.aws

import org.jets3t.service.S3ServiceException
import static org.junit.Assert.*

class S3IntegrationTests extends GroovyTestCase {

    def aws

	void testSomething() {
		assert true
	}

	// TODO: get this working in grails 2.3.4
    // void testGetOnAMissingFileThrowsS3ServiceException() {
    // 
    //     def message = shouldFail(S3ServiceException) {
    //         aws.s3().on('missing-bucket').get('missing-file.txt')
    //     }
    //     assert message == "S3 Error Message."
    // }
}
