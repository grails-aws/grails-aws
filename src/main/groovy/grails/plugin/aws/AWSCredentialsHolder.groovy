package grails.plugin.aws

import org.jets3t.service.security.AWSCredentials as JetS3tCredentials
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.PropertiesCredentials

class AWSCredentialsHolder {

	/**
	 * AWS access key
	 */
	String accessKey
	
	/**
	 * AWS secret key
	 */
	String secretKey
	
	/**
	 * Location of a properties file
	 */
	String properties

	private static Logger log = LoggerFactory.getLogger(this)

	/**
	 * @return BasicAWSCredentials which are compatible with the AWS SDK
	 */
	BasicAWSCredentials buildAwsSdkCredentials() {

		if (properties instanceof String) {
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

	/**
	 * @return JetS3tCredentials which are compatible with JetS3t
	 */
	JetS3tCredentials buildJetS3tCredentials() {

		if (properties instanceof String) {
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
