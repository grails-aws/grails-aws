package grails.plugin.aws.s3

import org.jets3t.service.model.S3Object
import org.jets3t.service.utils.Mimetypes
import org.jets3t.service.acl.AccessControlList
import org.jets3t.service.impl.rest.httpclient.RestS3Service

import grails.plugin.aws.GrailsAWSException

class S3FileUpload {
	
	//injected
	def acl
	def rrs
	def bucket
	def bucketLocation
	def credentialsHolder
	
	//configured by user
	String path
	Map    metadata = [:]
		
	//set bucket
	void bucket(_bucketName) {
		this.bucket = _bucketName
	}
	
	//set bucket with location
	void bucket(_bucketName, _bucketLocation) {
		this.bucket = _bucketName
		this.bucketLocation = _bucketLocation
	}

	//set file path
	void path(_path) {
		this.path = _path
	}

	//file metadata
	void metadata(_metadata) {
		this.metadata = _metadata
	}

	//file's acl
	void acl(_acl) {
		this.acl = _acl
	}

	//if will or not use rrs
	void rrs(_rrs) {
		this.rrs = _rrs
	}
	
	//upload method for inputstreams
	def inputStreamUpload(InputStream inputStream, String name, Closure cls) {
		
		if (cls) {
			cls.delegate = this
			cls()
		}
		
		//bucket validation
		validateBucket()
		
		//s3 service
		def s3Service = new RestS3Service(credentialsHolder.buildJetS3tCredentials())
		
		//s3 object
		def s3Object = buildS3Object(new S3Object(), name)
		s3Object.setDataInputStream(inputStream)
		s3Object.setContentType(Mimetypes.getInstance().getMimetype(name))
		
		//bucket
		def bucketObject = s3Service.getOrCreateBucket(bucket, bucketLocation)
		
		//upload
		def uploadedObject = s3Service.putObject(bucketObject, s3Object)
		uploadedObject.bucketName = bucketObject.name
		return new S3File(uploadedObject)
	}

	//upload method for file
	def fileUpload(File _file, Closure cls) {
			
		if (cls) {
			cls.delegate = this
			cls()
		}
		
		//bucket validation
		validateBucket()
		
		//s3 service
		def s3Service = new RestS3Service(jetCredentials)		
		
		//s3 object
		def s3Object = buildS3Object(new S3Object(file))
		
		//bucket
		def bucketObject = s3Service.getOrCreateBucket(bucketName, bucketLocation)
		
		//upload
		def uploadedObject = s3Service.putObject(bucketObject, s3Object)
		uploadedObject.bucketName = bucketObject.name
		return new S3File(uploadedObject)
	}
	
	//bucket validation
	def validateBucket() {

		if (!bucketName) {
			throw new GrailsAWSException("Invalid upload attemp, do not forget to set your bucket")
		}
	}
	
	//build s3 object
	def buildS3Object(S3Object s3Object, String name = null) {
		
		//acl
		if (acl == "public")
			s3Object.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ)
		if (acl == "private")
			s3Object.setAcl(AccessControlList.REST_CANNED_PRIVATE)
		if (acl == "public_read_write")
			s3Object.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ_WRITE)
		if (acl == "authenticated_read")
			s3Object.setAcl(AccessControlList.REST_CANNED_AUTHENTICATED_READ)
		
		//path
		//if (this.path) {
		//	
		//	def _path = this.path
		//	if (!_path.endsWith("/")) {
		//		_path = "${_path}/"
		//	}
		//	_path = "${_path}${(name ? name : file.name)}"
		//	s3Object.setKey(_path)
		//	
		//} else {
		//	s3Object.setKey(name ? name : file.name)
		//}
		
		//metadata
		metadata.each { metaKey, metaValue ->
			s3Object.addMetadata(metaKey, metaValue.toString())	
		}
				
		//rrs
		if (rrs) {
			s3Object.setStorageClass(S3Object.STORAGE_CLASS_REDUCED_REDUNDANCY)
		}
		
		return s3Object
	}
}