package grails.plugin.aws

import grails.plugin.aws.s3.AwsS3Tools
import com.amazonaws.auth.AWSCredentials

class AwsGenericTools {
	
	def s3 = {
		return new AwsS3Tools(GrailsAWSCredentialsWrapper.defaultCredentials())
	}
	
}