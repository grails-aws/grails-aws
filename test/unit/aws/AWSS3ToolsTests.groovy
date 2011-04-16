package aws

import grails.plugin.aws.s3.AWSS3Tools
import grails.test.GrailsUnitTestCase
import org.jets3t.service.model.S3Object

class AWSS3ToolsTests extends GrailsUnitTestCase {
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testUrl() {
	
		def s3Tools = new AWSS3Tools()
		assertEquals "http://my-bucket.s3.amazonaws.com/file.txt", s3Tools.on("my-bucket").url("file.txt")
		assertEquals "http://my-bucket.s3.amazonaws.com/path/to/my/file.txt", s3Tools.on("my-bucket").url("file.txt", "path/to/my")
		assertEquals "http://my-bucket.s3.amazonaws.com/path/to/my/file.txt", s3Tools.on("my-bucket").url("file.txt", "path/to/my/")
    }
}
