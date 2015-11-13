package aws

import grails.plugin.aws.AWSCredentialsHolder
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.junit.Test

import static org.junit.Assert.assertEquals

@TestMixin(GrailsUnitTestMixin)
class AWSCredentialsHolderTests {

	@Test
	void buildAwsSdkCredentialsWithPlainCredentials() {

		def credentialsHolder = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def credentialsReturned = credentialsHolder.buildAwsSdkCredentials()
		assertEquals "my-plain-access-key", credentialsReturned.getAWSAccessKeyId()
		assertEquals "my-plain-secret-key", credentialsReturned.getAWSSecretKey()
		assertEquals com.amazonaws.auth.BasicAWSCredentials, credentialsReturned.getClass()
	}

	@Test
	void buildAwsSdkCredentialsWithPropertiesCredentials() {

		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << """accessKey = my-properties-access-key
                      secretKey = my-properties-secret-key"""

		def credentialsHolder = new AWSCredentialsHolder()
		credentialsHolder.properties = tmpFile.toString()

		def credentialsReturned = credentialsHolder.buildAwsSdkCredentials()
		assertEquals "my-properties-access-key", credentialsReturned.getAWSAccessKeyId()
		assertEquals "my-properties-secret-key", credentialsReturned.getAWSSecretKey()
		assertEquals com.amazonaws.auth.BasicAWSCredentials, credentialsReturned.getClass()
	}

	@Test
	void buildAwsSdkCredentialsWithBothCredentialsPropertiesReturned() {

		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << """accessKey = my-properties-access-key
                      secretKey = my-properties-secret-key"""

		def credentialsHolder = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"
		credentialsHolder.properties = tmpFile.toString()

		def credentialsReturned = credentialsHolder.buildAwsSdkCredentials()
		assertEquals "my-properties-access-key", credentialsReturned.getAWSAccessKeyId()
		assertEquals "my-properties-secret-key", credentialsReturned.getAWSSecretKey()
		assertEquals com.amazonaws.auth.BasicAWSCredentials, credentialsReturned.getClass()
	}

	@Test
	void failToBuildAwsSdkCredentialsWithoutAccessOrSecretInPlainText() {

		def credentialsHolder = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"

		shouldFail {
			def credentialsReturned = credentialsHolder.buildAwsSdkCredentials()
		}
	}

	@Test
	void failToBuildAwsSdkCredentialsWithoutAccessOrSecretInProperties() {

		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << "accessKey = my-properties-access-key"

		def credentialsHolder = new AWSCredentialsHolder()
		credentialsHolder.properties = tmpFile.toString()

		shouldFail {
			def credentialsReturned = credentialsHolder.buildAwsSdkCredentials()
		}
	}

	@Test
	void buildJetS3tCredentialsWithPlainCredentials() {

		def credentialsHolder = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"

		def credentialsReturned = credentialsHolder.buildJetS3tCredentials()
		assertEquals "my-plain-access-key", credentialsReturned.accessKey
		assertEquals "my-plain-secret-key", credentialsReturned.secretKey
		assertEquals org.jets3t.service.security.AWSCredentials, credentialsReturned.getClass()
	}

	@Test
	void buildJetS3tCredentialsWithPropertiesCredentials() {

		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << """accessKey = my-properties-access-key
                      secretKey = my-properties-secret-key"""

		def credentialsHolder = new AWSCredentialsHolder()
		credentialsHolder.properties = tmpFile.toString()

		def credentialsReturned = credentialsHolder.buildJetS3tCredentials()
		assertEquals "my-properties-access-key", credentialsReturned.accessKey
		assertEquals "my-properties-secret-key", credentialsReturned.secretKey
		assertEquals org.jets3t.service.security.AWSCredentials, credentialsReturned.getClass()
	}

	@Test
	void buildJetS3tCredentialsWithBothCredentialsPropertiesReturned() {

		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << """accessKey = my-properties-access-key
                      secretKey = my-properties-secret-key"""

		def credentialsHolder = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"
		credentialsHolder.secretKey = "my-plain-secret-key"
		credentialsHolder.properties = tmpFile.toString()

		def credentialsReturned = credentialsHolder.buildJetS3tCredentials()
		assertEquals "my-properties-access-key", credentialsReturned.accessKey
		assertEquals "my-properties-secret-key", credentialsReturned.secretKey
		assertEquals org.jets3t.service.security.AWSCredentials, credentialsReturned.getClass()
	}

	@Test
	void failToBuildJetS3tCredentialsWithoutAccessOrSecretInPlainText() {

		def credentialsHolder = new AWSCredentialsHolder()
		credentialsHolder.accessKey = "my-plain-access-key"

		shouldFail {
			def credentialsReturned = credentialsHolder.buildJetS3tCredentials()
		}
	}

	@Test
	void failToBuildJetS3tCredentialsWithoutAccessOrSecretInProperties() {

		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile << "accessKey = my-properties-access-key"

		def credentialsHolder = new AWSCredentialsHolder()
		credentialsHolder.properties = tmpFile.toString()

		shouldFail {
			def credentialsReturned = credentialsHolder.buildJetS3tCredentials()
		}
	}
}
