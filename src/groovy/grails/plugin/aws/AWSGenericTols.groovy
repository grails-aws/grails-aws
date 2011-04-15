package grails.plugin.aws

import grails.plugin.aws.s3.AwsS3Tools
import com.amazonaws.auth.AWSCredentials

class AWSGenericTools {
	
	def s3 = {
		return new AWSGenericTools(GrailsAWSCredentialsWrapper.defaultCredentials())
	}
	
}