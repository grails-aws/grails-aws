package grails.plugin.aws.s3

import grails.plugin.aws.GrailsAWSException

import org.jets3t.service.acl.AccessControlList
import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.jets3t.service.model.S3Object
import org.jets3t.service.utils.Mimetypes
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class S3FileUpload {

	//injected
	def acl
	def rrs
	def bucket
	def bucketLocation
	def credentialsHolder
	def useEncryption

	//configured by user
	String path
	Map    metadata = [:]

	//log
	private static Logger log = LoggerFactory.getLogger(this)

	//set bucket
	void bucket(_bucketName) {
		bucket = _bucketName
		log.debug "setting bucket name to '${bucket}'"
	}

	//set bucket with location
	void bucket(_bucketName, _bucketLocation) {
		bucket = _bucketName
		bucketLocation = _bucketLocation
		log.debug "setting bucket name to '${bucket}' and its location to '${bucketLocation}'"
	}

	//set file path
	void path(_path) {
		path = _path
		log.debug "setting path name to '${path}'"
	}

	//file metadata
	void metadata(_metadata) {
		metadata = _metadata
		log.debug "setting metadata name to '${metadata}'"
	}

	//file's acl
	void acl(_acl) {
		acl = _acl
		log.debug "setting acl name to '${acl}'"
	}

	//if will or not use rrs
	void rrs(_rrs) {
		rrs = _rrs
		log.debug "setting rrs name to '${rrs}'"
	}

	//whether to use server side encryption
	void useEncryption(_useEncryption) {
		useEncryption = _useEncryption
		log.debug "setting useEncryption preference to '${useEncryption}'"
	}

	//upload method for inputstreams
	S3File inputStreamUpload(InputStream inputStream, String name, Closure cls) {

		log.debug "attempting to upload file from inputStream"
		setClosureData(cls)
		validateBucketName()

		//s3 object
		def s3Object = buildS3Object(new S3Object(), name)
		s3Object.setDataInputStream(inputStream)
		s3Object.setContentType(Mimetypes.getInstance().getMimetype(name))

		//s3 service
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())

		//bucket
		def bucketObject = s3Service.getOrCreateBucket(bucket, bucketLocation)

		//upload
		return new S3File(s3Service.putObject(bucketObject, s3Object))
	}

	S3File fileUpload(File file, Closure cls) {

		log.debug "attempting to upload file from plain file object"
		setClosureData(cls)
		validateBucketName()

		//s3 object
		def s3Object = buildS3Object(new S3Object(file), file.name)

		//s3 service
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())

		//bucket
		def bucketObject = s3Service.getOrCreateBucket(bucket, bucketLocation)

		//upload
		return new S3File(s3Service.putObject(bucketObject, s3Object))
	}

	//build s3 object
	S3Object buildS3Object(S3Object s3Object, String name) {

		//acl
		if (acl == "public")
			s3Object.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ)
		if (acl == "private")
			s3Object.setAcl(AccessControlList.REST_CANNED_PRIVATE)
		if (acl == "public_read_write")
			s3Object.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ_WRITE)
		if (acl == "authenticated_read")
			s3Object.setAcl(AccessControlList.REST_CANNED_AUTHENTICATED_READ)

		s3Object.setKey(buildObjectKey(path, name))
		s3Object.bucketName = bucket

		metadata.each { metaKey, metaValue ->
			s3Object.addMetadata(metaKey, metaValue.toString())
		}

		if (rrs) {
			s3Object.setStorageClass(S3Object.STORAGE_CLASS_REDUCED_REDUNDANCY)
		}

		if (useEncryption) {
			s3Object.setServerSideEncryptionAlgorithm(S3Object.SERVER_SIDE_ENCRYPTION__AES256)
		}

		return s3Object
	}

	String buildObjectKey(path, name) {
		if (path) {
			if (!path.endsWith("/")) {
				path = path.concat("/")
			}
			path = path.concat(name)
			return path
		}
		return name
	}

	def setClosureData(Closure cls) {
		if (cls) {
			cls.delegate = this
			cls()
		}
	}

	void validateBucketName() {
		if (!bucket) {
			throw new GrailsAWSException("Invalid upload attemp, do not forget to set your bucket")
		}
	}
}
