package aws

import grails.plugin.aws.s3.S3File
import grails.test.GrailsUnitTestCase
import org.jets3t.service.model.S3Object

class S3FileTests extends GrailsUnitTestCase {
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testUrl() {
	
		def s3Object = new S3Object()
		s3Object.key = "path/to/my/file.txt"
		s3Object.bucketName = "my-bucket"
		
		def s3File = new S3File(s3Object)
		assertEquals "http://my-bucket.s3.amazonaws.com/path/to/my/file.txt", s3File.url()
    }
}
