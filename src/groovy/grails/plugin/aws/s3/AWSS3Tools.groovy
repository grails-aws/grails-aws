package grails.plugin.aws.s3

import grails.plugin.aws.s3.S3File
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
	
	//delete the file
	public void delete(String name, String path = null) {				
		validateTarget()
		def objectKey = buildObjectKey(name, path)
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		s3Service.deleteObject(new S3Bucket(onTarget), objectKey)
	}
	
	//get the file
	public S3File get(String name, String path = null) {
		validateTarget()
		def objectKey = buildObjectKey(name, path)
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		def s3Object = s3Service.getObject(onTarget, objectKey)
		return new S3File(s3Object)
	}
	
	//build the URL to retrieve the file
	public String url(String name, String path = null) {
		validateTarget()
		def objectKey = buildObjectKey(name, path)
		return "http://${onTarget}.s3.amazonaws.com/${objectKey}"
	}
	
	//creates a torrent file for seeding S3 hosted files
	public String torrent(String name, String path = null) {
		validateTarget()
		def objectKey = buildObjectKey(name, path)
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())			
		return s3Service.createTorrentUrl(onTarget, objectKey)
	}
	
	//creates a signed URL for retrieving private files
	public String publicUrlFor(Date expiryDate, String name, String path = null) {
		validateTarget()
		def objectKey = buildObjectKey(name, path)
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		return s3Service.createSignedGetUrl(onTarget, objectKey, expiryDate)
	}
	
	//check if user defined the bucket
	def validateTarget() {
		if (!onTarget) throw new GrailsAWSException("You can't manipulate files without setting its bucket")
	}
	
	//method to build the correct key for file (composed with name and path)
	def buildObjectKey(name, path) {
		
		def objectKey = name
		if (path) {
			if (!path.endsWith("/"))
				path = "${path}/"
			objectKey = "${path}${name}"
		}
		return objectKey
	}
}