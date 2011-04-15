package aws.s3

class S3TestController {

	def aws
	
	def index = {
		
	}
	
    def uploadWithDefaultProperties = {
	
		def fileToUpload = "/Users/blanq01/Desktop/grails-aws/cool-1.jpg"
		def uploadedFile = new File(fileToUpload).s3upload { }
		
		render """${uploadedFile.source.toString()} <br /><br />${uploadedFile.url()}"""
		
	}

    def uploadWithDefaultPropertiesAndNoClosure = {
	
		def fileToUpload = "/Users/blanq01/Desktop/grails-aws/cool-2.jpg"
		def uploadedFile = new File(fileToUpload).s3upload()
		
		render uploadedFile.source.toString()
	}

    def uploadWithOtherBucket = {
	
		def fileToUpload = "/Users/blanq01/Desktop/grails-aws/cool-3.jpg"
		def uploadedFile = new File(fileToUpload).s3upload {
			bucket "new-bucket-in-default-region"
		}
		
		render uploadedFile.source.toString()
	}
	
	def uploadWithOtherBucketInOtherRegion = {
	
		def fileToUpload = "/Users/blanq01/Desktop/grails-aws/cool-4.jpg"
		def uploadedFile = new File(fileToUpload).s3upload {
			bucket "bucket-in-eu-region", "EU"
		}
				
		render uploadedFile.source.toString()
	}
	
	def uploadWithPath = {
	
		def fileToUpload = "/Users/blanq01/Desktop/grails-aws/cool-5.jpg"
		def uploadedFile = new File(fileToUpload).s3upload { 
			path "path/created"
		}
		
		render uploadedFile.source.toString()
	}

	def uploadWithMetadata = {
	
		def fileToUpload = "/Users/blanq01/Desktop/grails-aws/cool-6.jpg"
		def uploadedFile = new File(fileToUpload).s3upload { 
			metadata param1: 'value1', param2: 'value2'
		}
		
		render uploadedFile.source.toString()
	}    

	def uploadWithoutRRS = {
	
		def fileToUpload = "/Users/blanq01/Desktop/grails-aws/cool-7.jpg"
		def uploadedFile = new File(fileToUpload).s3upload { 
			rrs false
		}
		
		render uploadedFile.source.toString()
	}    
	
	def uploadPrivate = {
	
		def fileToUpload = "/Users/blanq01/Desktop/grails-aws/cool-8.jpg"
		def uploadedFile = new File(fileToUpload).s3upload { 
			acl "private"
		}
		
		render uploadedFile.source.toString()
	}    

	def uploadPrivateWithPublicUrl = {
	
		def bucketName = "bucket-${System.currentTimeMillis()}"
		def fileToUpload = "/Users/blanq01/Desktop/grails-aws/cool-9.jpg"
		
		def uploadedFile = new File(fileToUpload).s3upload { 
			bucket bucketName
			acl "private"
		}
		
		def publicUrl = uploadedFile.publicUrlFor(5.minutes)
		
		render """
		          ${uploadedFile.source.toString()}<br />
		          Private URL: http://${bucketName}.s3.amazonaws.com/${uploadedFile.key}<br/>
		          Public URL: ${publicUrl}"""
	}  
	
	def uploadAndGetTorrent = {
	
		def fileToUpload = "/Users/blanq01/Desktop/grails-aws/cool-10.jpg"
		def bucketName = "bucket-${System.currentTimeMillis()}"
		def uploadedFile = new File(fileToUpload).s3upload { 
			bucket bucketName
		}
		
		render """
		          ${uploadedFile.source.toString()}<br />
		          URL: http://${bucketName}.s3.amazonaws.com/${uploadedFile.key}<br/>
		          Torrent URL: ${uploadedFile.torrent()}"""
	}      
	
	def uploadFromInputStream = {
		
		def file = request.getFile('photo')
		def uploadedFile = file.inputStream.s3upload("file-name-${System.currentTimeMillis()}.jpg") {
			bucket "file-upload-from-inputstream"
		}
		
		render uploadedFile.source.toString()
	}

	def uploadFromByteArrayInputStream = {
		
		def fileToUpload = "/Users/blanq01/Desktop/grails-aws/cool-11.jpg"
		def file = new File(fileToUpload)
		def uploadedFile = new ByteArrayInputStream(file.bytes).s3upload("file-name-${System.currentTimeMillis()}.jpg") {
			bucket "file-upload-from-inputstream"
		}
		
		render uploadedFile.source.toString()
	}
	
	def deleteUploadedFile = {
		
		def bucket = params.bucket
		def file = params.file
		def path = params.path
		
		aws.s3().on(bucket).delete(file, path)
		
		render "Deleted file ${file} (path '${path}') of bucket ${bucket}"
		
	}
}
