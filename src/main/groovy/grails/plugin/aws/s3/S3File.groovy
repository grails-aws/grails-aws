package grails.plugin.aws.s3

import org.jets3t.service.model.S3Object

class S3File {

	@Delegate
	S3Object source

	S3File(S3Object _source) {
		source = _source
	}
}
