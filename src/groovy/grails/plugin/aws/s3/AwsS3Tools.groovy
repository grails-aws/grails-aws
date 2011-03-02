package grails.plugin.aws.s3

import org.jets3t.service.S3Service
import com.amazonaws.auth.AWSCredentials
import org.jets3t.service.model.S3Bucket
import grails.plugin.aws.GrailsAWSException
import org.jets3t.service.impl.rest.httpclient.RestS3Service

class AwsS3Tools {
	
	def onTarget
	private AWSCredentials awsCredentials
	
	public AwsS3Tools(credentials) {
		this.awsCredentials = credentials
	}
	
	public AwsS3Tools on(String _onTarget) {
		this.onTarget = _onTarget
		return this
	}
	
	public void delete(String file, String path = null) {
				
		//bucket validation
		if (!onTarget) {
			throw new GrailsAWSException("Ops, you can't delete one file without setting its bucket")
		}
		
		def objectKey = file
		if (path) {
			if (!path.endsWith("/")) {
				path = "${path}/"
			}
			objectKey = "${path}${file}"
		}
		
		def s3Service = new RestS3Service(getJetCredentials())		
		def s3Bucket = new S3Bucket(onTarget)
		s3Service.deleteObject(s3Bucket, objectKey)
	}
	
	
	private getJetCredentials() {
		return new org.jets3t.service.security.AWSCredentials(awsCredentials.getAWSAccessKeyId(), awsCredentials.getAWSSecretKey())
	}
}