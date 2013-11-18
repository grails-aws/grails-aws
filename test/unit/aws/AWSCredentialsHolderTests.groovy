package aws

import grails.plugin.aws.AWSCredentialsHolder
import grails.plugin.aws.util.MockLogger
import grails.test.GrailsUnitTestCase

class AWSCredentialsHolderTests extends GrailsUnitTestCase {

    void test_BuildAwsSdkCredentialsWithPlainCredentials() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.log       = new MockLogger()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def credentialsReturned = credentialsHolder.buildAwsSdkCredentials()
		assertEquals "my-plain-access-key", credentialsReturned.getAWSAccessKeyId()
		assertEquals "my-plain-secret-key", credentialsReturned.getAWSSecretKey()
		assertEquals com.amazonaws.auth.BasicAWSCredentials, credentialsReturned.getClass()
    }

    void test_BuildAwsSdkCredentialsWithPropertiesCredentials() {

		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << """accessKey = my-properties-access-key
                      secretKey = my-properties-secret-key"""

		def credentialsHolder        = new AWSCredentialsHolder()
		credentialsHolder.log        = new MockLogger()
		credentialsHolder.properties = tmpFile.toString()

		def credentialsReturned = credentialsHolder.buildAwsSdkCredentials()
		assertEquals "my-properties-access-key", credentialsReturned.getAWSAccessKeyId()
		assertEquals "my-properties-secret-key", credentialsReturned.getAWSSecretKey()
		assertEquals com.amazonaws.auth.BasicAWSCredentials, credentialsReturned.getClass()
    }

    void test_BuildAwsSdkCredentialsWithBothCredentialsPropertiesReturned() {

		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << """accessKey = my-properties-access-key
                      secretKey = my-properties-secret-key"""

		def credentialsHolder        = new AWSCredentialsHolder()
		credentialsHolder.log        = new MockLogger()
		credentialsHolder.accessKey  = "my-plain-access-key"
		credentialsHolder.secretKey  = "my-plain-secret-key"
		credentialsHolder.properties = tmpFile.toString()

		def credentialsReturned = credentialsHolder.buildAwsSdkCredentials()
		assertEquals "my-properties-access-key", credentialsReturned.getAWSAccessKeyId()
		assertEquals "my-properties-secret-key", credentialsReturned.getAWSSecretKey()
		assertEquals com.amazonaws.auth.BasicAWSCredentials, credentialsReturned.getClass()
    }

    void test_FailToBuildAwsSdkCredentialsWithoutAccessOrSecretInPlainText() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.log       = new MockLogger()
		credentialsHolder.accessKey = "my-plain-access-key"

		shouldFail {
			def credentialsReturned = credentialsHolder.buildAwsSdkCredentials()
		}
    }

    void test_FailToBuildAwsSdkCredentialsWithoutAccessOrSecretInProperties() {

		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << "accessKey = my-properties-access-key"

		def credentialsHolder        = new AWSCredentialsHolder()
		credentialsHolder.log        = new MockLogger()
		credentialsHolder.properties = tmpFile.toString()

		shouldFail {
			def credentialsReturned = credentialsHolder.buildAwsSdkCredentials()
		}
    }

    void test_BuildJetS3tCredentialsWithPlainCredentials() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.log       = new MockLogger()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def credentialsReturned = credentialsHolder.buildJetS3tCredentials()
		assertEquals "my-plain-access-key", credentialsReturned.accessKey
		assertEquals "my-plain-secret-key", credentialsReturned.secretKey
		assertEquals org.jets3t.service.security.AWSCredentials, credentialsReturned.getClass()
    }

    void test_BuildJetS3tCredentialsWithPropertiesCredentials() {

		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << """accessKey = my-properties-access-key
                      secretKey = my-properties-secret-key"""

		def credentialsHolder        = new AWSCredentialsHolder()
		credentialsHolder.log        = new MockLogger()
		credentialsHolder.properties = tmpFile.toString()

		def credentialsReturned = credentialsHolder.buildJetS3tCredentials()
		assertEquals "my-properties-access-key", credentialsReturned.accessKey
		assertEquals "my-properties-secret-key", credentialsReturned.secretKey
 		assertEquals org.jets3t.service.security.AWSCredentials, credentialsReturned.getClass()
   }

    void test_BuildJetS3tCredentialsWithBothCredentialsPropertiesReturned() {

		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << """accessKey = my-properties-access-key
                      secretKey = my-properties-secret-key"""

		def credentialsHolder        = new AWSCredentialsHolder()
		credentialsHolder.log        = new MockLogger()
		credentialsHolder.accessKey  = "my-plain-access-key"
		credentialsHolder.secretKey  = "my-plain-secret-key"
		credentialsHolder.properties = tmpFile.toString()

		def credentialsReturned = credentialsHolder.buildJetS3tCredentials()
		assertEquals "my-properties-access-key", credentialsReturned.accessKey
		assertEquals "my-properties-secret-key", credentialsReturned.secretKey
		assertEquals org.jets3t.service.security.AWSCredentials, credentialsReturned.getClass()
    }

    void test_FailToBuildJetS3tCredentialsWithoutAccessOrSecretInPlainText() {

		def credentialsHolder       = new AWSCredentialsHolder()
		credentialsHolder.log       = new MockLogger()
		credentialsHolder.accessKey = "my-plain-access-key"

		shouldFail {
			def credentialsReturned = credentialsHolder.buildJetS3tCredentials()
		}
    }

    void test_FailToBuildJetS3tCredentialsWithoutAccessOrSecretInProperties() {

		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << "accessKey = my-properties-access-key"

		def credentialsHolder        = new AWSCredentialsHolder()
		credentialsHolder.log        = new MockLogger()
		credentialsHolder.properties = tmpFile.toString()

		shouldFail {
			def credentialsReturned = credentialsHolder.buildJetS3tCredentials()
		}
    }
}
