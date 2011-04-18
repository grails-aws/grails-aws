package grails.plugin.aws.s3

import org.jets3t.service.model.S3Object
import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class S3File {
	
	@Delegate
	S3Object source
	
	public S3File(S3Object _source) {
		this.source = _source
	}
	
	public String publicUrlFor(expiryDate = 1.hour) {
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		return s3Service.createSignedGetUrl(source.bucketName, source.key, expiryDate)
	}
				
}