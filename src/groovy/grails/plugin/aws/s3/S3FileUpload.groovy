package grails.plugin.aws.s3

import org.jets3t.service.model.S3Object
import org.jets3t.service.acl.AccessControlList
import org.jets3t.service.security.AWSCredentials
import org.jets3t.service.impl.rest.httpclient.RestS3Service

class S3FileUpload {
	
	String path
	String access
	String secret
	String bucket
	String acl = "public"
	boolean rrs = false
	Map metadata = [:]
	File file

	public S3FileUpload() {}
	public S3FileUpload(String _access, String _secret, String _bucket, String _acl, Boolean _rrs) {
		credentials(_access, _secret)
		bucket(_bucket)
		acl(_acl)
		rrs(_rrs)
	}
	
	void credentials(_access, _secret) {
		this.access = _access
		this.secret = _secret
	}

	void bucket(_bucket) {
		this.bucket = _bucket
	}

	void path(_path) {
		this.path = _path
	}

	void metadata(_metadata) {
		this.metadata = _metadata
	}

	void acl(_acl) {
		this.acl = _acl
	}

	void rrs(_rrs) {
		this.rrs = _rrs
	}

	def upload(File _file, Closure cls) {
		
		this.file = _file
		
		cls.delegate = this
		cls()
		
		if (!access || !secret || !bucket) {
			throw new Exception("[grails-aws-plugin][s3] invalid configuration, do not forget to set credentials and bucket")
		}
		
		def s3Service = new RestS3Service(new AWSCredentials(access, secret));		
		
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
		
		def uploadedObject = s3Service.putObject(bucket, s3Object)
		return uploadedObject
	}
}