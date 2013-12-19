package grails.plugin.aws.s3

import grails.plugin.aws.GrailsAWSException

import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.jets3t.service.model.MultipleDeleteResult
import org.jets3t.service.model.S3Bucket

class AWSS3Tools {

	def onTarget
	def credentialsHolder

	AWSS3Tools on(String _onTarget) {
		onTarget = _onTarget
		return this
	}

	//delete the file
	void delete(String name, String path = null) {
		validateTarget()
		def objectKey = buildObjectKey(name, path)
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		s3Service.deleteObject(new S3Bucket(onTarget), objectKey)
	}

	//delete multiple files in one request
	void deleteMultiple(String[] objectKeys) {
		validateTarget()
		def s3Bucket = new S3Bucket(onTarget)
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		MultipleDeleteResult result = s3Service.deleteMultipleObjects(new S3Bucket(onTarget), objectKeys)
		if (result.hasErrors()) {
			log.error('deleteMultipleObjects had errors: ' + result.getErrorResults())
		}
	}

	// list all files
	S3File[] list() {
		validateTarget()
		def s3Bucket = new S3Bucket(onTarget)
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		return s3Service.listObjects(s3Bucket).collect { new S3File(it) }
	}

	//delete all files in specified path
	void deleteAll() {
		validateTarget()
		def s3Bucket = new S3Bucket(onTarget)
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		def s3ObjectList = s3Service.listObjects(onTarget)
		s3ObjectList?.each { s3Object ->
			s3Service.deleteObject(s3Bucket, s3Object.getKey())
		}
	}

	//get the file
	S3File get(String name, String path = null) {
		validateTarget()
		def objectKey = buildObjectKey(name, path)
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		def s3Object = s3Service.getObject(onTarget, objectKey)
		return new S3File(s3Object)
	}

	//get the file details. metadata without the file content
	S3File getDetails(String name, String path = null) {
		validateTarget()
		def objectKey = buildObjectKey(name, path)
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		def s3Object = s3Service.getObjectDetails(onTarget, objectKey)
		return new S3File(s3Object)
	}

	//build the URL to retrieve the file
	String url(String name, String path = null) {
		validateTarget()
		def objectKey = buildObjectKey(name, path)
		return "http://${onTarget}.s3.amazonaws.com/${objectKey}"
	}

	//creates a torrent file for seeding S3 hosted files
	String torrent(String name, String path = null) {
		validateTarget()
		def objectKey = buildObjectKey(name, path)
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		return s3Service.createTorrentUrl(onTarget, objectKey)
	}

	//creates a signed URL for retrieving private files
	String publicUrlFor(Date expiryDate, String name, String path = null) {
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
