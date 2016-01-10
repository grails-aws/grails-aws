package aws

import grails.plugin.aws.s3.AWSS3Tools
import grails.test.mixin.TestMixin
import org.junit.Test
import grails.test.mixin.support.GrailsUnitTestMixin

import static org.junit.Assert.assertEquals

@TestMixin(GrailsUnitTestMixin)
class AWSS3ToolsTests {

   @Test
   void onMethod() {

      def s3Tools = new AWSS3Tools()
      assertEquals "http://my-bucket.s3.amazonaws.com/file.txt", s3Tools.on("my-bucket").url("file.txt")
      assertEquals "http://my-bucket.s3.amazonaws.com/path/to/my/file.txt", s3Tools.on("my-bucket").url("file.txt", "path/to/my")
      assertEquals "http://my-bucket.s3.amazonaws.com/path/to/my/file.txt", s3Tools.on("my-bucket").url("file.txt", "path/to/my/")
   }
}
