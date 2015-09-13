package grails.plugin.aws

import grails.plugin.aws.s3.AWSS3Tools
import static org.junit.Assert.*
import org.junit.Test

class AWSGenericToolsIntegrationTests {

    def aws

    @Test
    void s3MethodReturnsHelperToTheS3Service() {
        
        assert aws.s3().class == AWSS3Tools
    }
}
