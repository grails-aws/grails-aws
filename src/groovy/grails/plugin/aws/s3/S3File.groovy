package grails.plugin.aws.s3

import org.jets3t.service.model.S3Object
import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class S3File {
	
	@Delegate
	S3Object source
	
	def credentialsHolder
	
	public S3File(S3Object _source) {
		this.source = _source
		credentialsHolder = AH.application.mainContext.credentialsHolder
	}
	
	public String publicUrlFor(expiryDate = 1.hour) {
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		return s3Service.createSignedGetUrl(source.bucketName, source.key, expiryDate)
	}
	
	public String torrent() {
		
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())			
		return s3Service.createTorrentUrl(source.bucketName, source.key)
	}
		
}