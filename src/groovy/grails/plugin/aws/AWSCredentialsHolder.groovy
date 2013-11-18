package grails.plugin.aws

import org.jets3t.service.security.AWSCredentials as JetS3tCredentials
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.PropertiesCredentials

class AWSCredentialsHolder {

	def accessKey
	def secretKey
	def properties

	private static Logger log = LoggerFactory.getLogger(this)

	BasicAWSCredentials buildAwsSdkCredentials() {

		if (properties) {
			def propertiesCredentials = new PropertiesCredentials(new File(properties))
			accessKey = propertiesCredentials.getAWSAccessKeyId()
			secretKey = propertiesCredentials.getAWSSecretKey()
			log.debug "building AWS SDK credentials (from a property file)"
		} else {
			log.debug "building AWS SDK credentials from plain credentials"
		}

		if (!accessKey || !secretKey) {
			throw new GrailsAWSException("Please check user guide to see how you should configure AWS credentials")
		}

		return new BasicAWSCredentials(accessKey, secretKey)
	}

	JetS3tCredentials buildJetS3tCredentials() {

		if (properties) {
			def propertiesCredentials = new PropertiesCredentials(new File(properties))
			accessKey = propertiesCredentials.getAWSAccessKeyId()
			secretKey = propertiesCredentials.getAWSSecretKey()
			log.debug "building JetS3t AWS credentials (from a property file)"
		} else {
			log.debug "building JetS3t AWS credentials from plain credentials"
		}

		if (!accessKey || !secretKey) {
			throw new GrailsAWSException("Please check user guide to see how you should configure AWS credentials")
		}

		return new JetS3tCredentials(accessKey, secretKey)
	}
}
