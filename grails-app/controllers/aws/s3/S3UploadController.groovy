package aws.s3

class S3UploadController {
	
	def index = {
		
		new File("/Users/lucastex/Desktop/teste.txt").s3upload {
			metadata nome: 'lucas', sobrenome: 'teixeira', data: new Date().format('dd/MM/yyyy HH:mm:ss')
			rrs true
		}
		
	}
	
}