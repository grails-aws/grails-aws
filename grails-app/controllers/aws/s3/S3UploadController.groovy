package aws.s3

class S3UploadController {
	
	def index = {
		
		def awsCred = new org.jets3t.service.security.AWSCredentials("access-key", "secret-key")
		
		def uploadedFile = new File("/Users/lucastex/Desktop/teste.txt").s3upload {
			
			credentials awsCred
			metadata nome: 'lucas', sobrenome: 'teixeira', data: new Date().format('dd/MM/yyyy HH:mm:ss')
			rrs true
		}
		
		render "uploaded file -> ${uploadedFile.properties}"
	}
	
}