package grails.aws

import org.jets3t.service.S3ServiceException
import static org.junit.Assert.*
import org.junit.Test

import grails.test.mixin.integration.IntegrationTestMixin
import grails.test.mixin.TestMixin

@TestMixin(IntegrationTestMixin)
class S3IntegrationTests {

    def aws

    @Test
    void getOnAMissingFileThrowsS3ServiceException() {
    
        def message = shouldFail(S3ServiceException) {
            aws.s3().on('missing-bucket').get('missing-file.txt')
        }
        assert message == "S3 Error Message."
    }

    @Test
    void s3UploadThrowsS3ServiceException() {
        def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
        tmpFile.deleteOnExit()
        
        def message = shouldFail(S3ServiceException) {

            def uploadedFile = tmpFile.s3upload {
                bucket "foo"
            }
        }
        assert message == "S3 Error Message."
    }
}
