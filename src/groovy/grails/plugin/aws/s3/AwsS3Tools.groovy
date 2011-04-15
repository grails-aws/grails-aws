package grails.plugin.aws.s3

import org.jets3t.service.S3Service
import com.amazonaws.auth.AWSCredentials
import org.jets3t.service.model.S3Bucket
import grails.plugin.aws.GrailsAWSException
import org.jets3t.service.impl.rest.httpclient.RestS3Service

class AWSS3Tools {
	
	def onTarget	
	def credentialsHolder
		
	public AWSS3Tools on(String _onTarget) {
		this.onTarget = _onTarget
		return this
	}
	
	public void delete(String file, String path = null) {				
		validateTarget()
		def objectKey = buildObjectKey(file, path)
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		s3Service.deleteObject(new S3Bucket(onTarget), objectKey)
	}	
	
	def validateTarget() {
		if (!onTarget) throw new GrailsAWSException("You can't delete one file without setting its bucket")
	}
	
	def buildObjectKey(file, path) {
		
		def objectKey = file
		if (path) {
			if (!path.endsWith("/"))
				path = "${path}/"
			objectKey = "${path}${file}"
		}
		return objectKey
	}
}