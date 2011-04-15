package grails.plugin.aws

import org.apache.log4j.Logger
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.PropertiesCredentials
import com.amazonaws.auth.AWSCredentials as AwsSdkCredentials
import org.jets3t.service.security.AWSCredentials as JetS3tCredentials

class AWSCredentialsHolder {
	
	def accessKey
	def secretKey
	def properties
	
	private static def log = Logger.getLogger(AWSCredentialsHolder.class)
	
	def buildAwsSdkCredentials() {
		
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

	def buildJetS3tCredentials() {
		
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