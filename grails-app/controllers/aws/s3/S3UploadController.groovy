package aws.s3

class S3UploadController {
	
	def index = {
				
		def uploadedFile = new File("/Users/lucastex/Desktop/teste.txt").s3upload {
			bucket "bucket-that-does-not-exist"
			metadata nome: 'lucas', sobrenome: 'teixeira', data: new Date().format('dd/MM/yyyy HH:mm:ss')
			rrs true
		}
		
		render "uploaded file -> ${uploadedFile.properties}"
	}
	
}