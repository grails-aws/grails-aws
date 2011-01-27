package grails.plugin.aws.s3

import org.jets3t.service.model.S3Object
import org.jets3t.service.security.AWSCredentials
import grails.plugin.aws.GrailsAWSCredentialsWrapper
import org.jets3t.service.impl.rest.httpclient.RestS3Service

class S3File {
	
	@Delegate
	S3Object source
	
	public S3File(S3Object _source) {
		this.source = _source
	}
	
	public String publicUrlFor(expiryDate) {
		
		def sdkCredentials = GrailsAWSCredentialsWrapper.defaultCredentials()
		def jetCredentials = new AWSCredentials(sdkCredentials.getAWSAccessKeyId(), sdkCredentials.getAWSSecretKey())
		def s3Service = new RestS3Service(jetCredentials)		
			
		return s3Service.createSignedGetUrl(source.bucketName, source.key, expiryDate)
	}
	
	public String torrent() {
		
		def sdkCredentials = GrailsAWSCredentialsWrapper.defaultCredentials()
		def jetCredentials = new AWSCredentials(sdkCredentials.getAWSAccessKeyId(), sdkCredentials.getAWSSecretKey())
		def s3Service = new RestS3Service(jetCredentials)		
			
		return s3Service.createTorrentUrl(source.bucketName, source.key)
	}
		
}