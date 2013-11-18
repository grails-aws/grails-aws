package aws

import grails.plugin.aws.s3.AWSS3Tools
import grails.test.GrailsUnitTestCase

class AWSS3ToolsTests extends GrailsUnitTestCase {

	void testUrl() {

		def s3Tools = new AWSS3Tools()
		assertEquals "http://my-bucket.s3.amazonaws.com/file.txt", s3Tools.on("my-bucket").url("file.txt")
		assertEquals "http://my-bucket.s3.amazonaws.com/path/to/my/file.txt", s3Tools.on("my-bucket").url("file.txt", "path/to/my")
		assertEquals "http://my-bucket.s3.amazonaws.com/path/to/my/file.txt", s3Tools.on("my-bucket").url("file.txt", "path/to/my/")
	}
}
