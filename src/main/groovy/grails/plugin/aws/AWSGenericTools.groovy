package grails.plugin.aws

class AWSGenericTools {

	def awsS3
	def awsSWF
	
	def s3 = {
		return awsS3
	}
	
	def swf = {
		return awsSWF
	}
}
