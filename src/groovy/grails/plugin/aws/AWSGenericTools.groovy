package grails.plugin.aws

import grails.plugin.aws.s3.AWSS3Tools
import com.amazonaws.auth.AWSCredentials

class AWSGenericTools {
	
	def awsS3
	
	def s3 = {
		return awsS3
	}
	
}