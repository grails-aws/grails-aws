package grails.plugin.aws.s3

import org.jets3t.service.model.S3Object

class S3File {
	
	@Delegate
	S3Object source
	
	public S3File(S3Object _source) {
		this.source = _source
	}			
}