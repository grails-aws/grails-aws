package grails.plugin.aws.s3

//jets3t
import org.jets3t.service.model.S3Object
import org.jets3t.service.acl.AccessControlList
import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.jets3t.service.security.AWSCredentials

//aws sdk
import com.amazonaws.auth.AWSCredentials

//plugin
import grails.plugin.aws.GrailsAWSException

class S3FileUpload {
	
	String path
	String access
	String secret
	String bucketName
	String bucketLocation
	String acl = "public"
	org.jets3t.service.security.AWSCredentials jetCredentials
	
	File file
	boolean rrs = false
	Map metadata = [:]

	public S3FileUpload() {}
	public S3FileUpload(com.amazonaws.auth.AWSCredentials defaultCredentials, String _bucketName, String _acl, Boolean _rrs) {
		credentials(defaultCredentials.getAWSAccessKeyId(), defaultCredentials.getAWSSecretKey())
		bucket(_bucketName)
		acl(_acl)
		rrs(_rrs)
	}
	
	
	//set credentials
	void credentials(_access, _secret) {
		jetCredentials = new org.jets3t.service.security.AWSCredentials(_access, _secret)
	}
	
	//set bucket
	void bucket(_bucketName) {
		this.bucketName = _bucketName
	}
	
	//set bucket with location
	void bucket(_bucketName, _bucketLocation) {
		this.bucketName = _bucketName
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

	//upload method
	def upload(File _file, Closure cls) {
		
		this.file = _file
		
		if (cls) {
			cls.delegate = this
			cls()
		}
		
		//bucket validation
		if (!bucketName) {
			throw new GrailsAWSException("Invalid upload attemp, do not forget to set your bucket")
		}

		def s3Service = new RestS3Service(jetCredentials)		
		
		//acl
		def s3Object = new S3Object(file)
		if (acl == "public")
			s3Object.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ)
		if (acl == "private")
			s3Object.setAcl(AccessControlList.REST_CANNED_PRIVATE)
		if (acl == "public_read_write")
			s3Object.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ_WRITE)
		if (acl == "authenticated_read")
			s3Object.setAcl(AccessControlList.REST_CANNED_AUTHENTICATED_READ)
		
		//path
		if (this.path) {
			
			def _path = this.path
			if (!_path.endsWith("/")) {
				_path = "${_path}/"
			}
			_path = "${_path}${file.getName()}"
			s3Object.setKey(_path)
		}
		
		//metadata
		s3Object.addAllMetadata(metadata)
				
		//rrs
		if (rrs) {
			s3Object.setStorageClass(S3Object.STORAGE_CLASS_REDUCED_REDUNDANCY)
		}
		
		def bucketObject = s3Service.getOrCreateBucket(bucketName, bucketLocation)
		def uploadedObject = s3Service.putObject(bucketObject, s3Object)
		return uploadedObject
	}
}