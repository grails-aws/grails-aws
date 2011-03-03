package grails.plugin.aws.s3

//jets3t
import org.jets3t.service.model.S3Object
import org.jets3t.service.utils.Mimetypes
import org.jets3t.service.acl.AccessControlList
import org.jets3t.service.security.AWSCredentials
import org.jets3t.service.impl.rest.httpclient.RestS3Service

//aws sdk
import com.amazonaws.auth.AWSCredentials

//plugin
import grails.plugin.aws.GrailsAWSException

class S3FileUpload {
	
	String path
	String bucketName
	String bucketLocation
	String acl = "public"
	org.jets3t.service.security.AWSCredentials jetCredentials
	
	File file
	boolean rrs = false
	Map metadata = [:]
	InputStream inputStream

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
	
	//upload method for inputstreams
	def inputStreamUpload(InputStream is, String name, Closure cls) {
		
		this.inputStream = is
		
		if (cls) {
			cls.delegate = this
			cls()
		}
		
		//bucket validation
		validateBucket()
		
		//s3 service
		def s3Service = new RestS3Service(jetCredentials)
		
		//s3 object
		def s3Object = buildS3Object(new S3Object(), name)
		s3Object.setDataInputStream(this.inputStream)
		s3Object.setContentType(Mimetypes.getInstance().getMimetype(name))
		
		//bucket
		def bucketObject = s3Service.getOrCreateBucket(bucketName, bucketLocation)
		
		//upload
		def uploadedObject = s3Service.putObject(bucketObject, s3Object)
		uploadedObject.bucketName = bucketObject.name
		return new S3File(uploadedObject)
	}

	//upload method for file
	def fileUpload(File _file, Closure cls) {
		
		this.file = _file
		
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
		if (this.path) {
			
			def _path = this.path
			if (!_path.endsWith("/")) {
				_path = "${_path}/"
			}
			_path = "${_path}${(name ? name : file.name)}"
			s3Object.setKey(_path)
			
		} else {
			s3Object.setKey(name ? name : file.name)
		}
		
		//metadata
		s3Object.addAllMetadata(metadata)
				
		//rrs
		if (rrs) {
			s3Object.setStorageClass(S3Object.STORAGE_CLASS_REDUCED_REDUNDANCY)
		}
		
		return s3Object
	}
}