package aws.ses

class SesTestController {

    def sendPlainTextMail = { 
	
		def mailId = sesMail {
			from "lucastex@gmail.com"
			to "lucastex@gmail.com"
			subject "test plain text mail"
			body "this is a plain text (${new Date().format('dd/MM/yyyy HH:mm')})"
		}
		
		render "E-mail sent: ${mailId}"
	}
}
