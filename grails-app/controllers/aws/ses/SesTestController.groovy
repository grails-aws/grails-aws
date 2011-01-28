package aws.ses

class SesTestController {

    def sendPlainTextMail = { 
	
		def mailId = sesMail {
			to "lucastex@gmail.com"
			subject "test plain text mail"
			body "sendPlainTextMail (${new Date().format('dd/MM/yyyy HH:mm')})"
		}
		
		render "E-mail sent: ${mailId}"
	}

    def sendPlainTextMultipleTo = { 
	
		def mailId = sesMail {
			to "lucastex@gmail.com", "l.ucastex@gmail.com", "lu.castex@gmail.com"
			subject "test plain text mail"
			body "sendPlainTextMultipleTo (${new Date().format('dd/MM/yyyy HH:mm')})"
		}
		
		render "E-mail sent: ${mailId}"
	}
	
	def sendPlainTextMailCC = { 
	
		def mailId = sesMail {
			to "lucastex@gmail.com"
			cc "l.ucastex@gmail.com"
			subject "test plain text mail"
			body "sendPlainTextMailCC (${new Date().format('dd/MM/yyyy HH:mm')})"
		}
		
		render "E-mail sent: ${mailId}"
	}

	def sendPlainTextMailMultipleCC = { 
	
		def mailId = sesMail {
			to "lucastex@gmail.com"
			cc "l.ucastex@gmail.com", "lu.castex@gmail.com"			
			subject "test plain text mail"
			body "sendPlainTextMailMultipleCC (${new Date().format('dd/MM/yyyy HH:mm')})"
		}
		
		render "E-mail sent: ${mailId}"
	}

	def sendPlainTextMailBCC = { 
	
		def mailId = sesMail {
			to "lucastex@gmail.com"
			bcc "l.ucastex@gmail.com"
			subject "test plain text mail"
			body "sendPlainTextMailBCC (${new Date().format('dd/MM/yyyy HH:mm')})"
		}
		
		render "E-mail sent: ${mailId}"
	}

	def sendPlainTextMailMultipleBCC = { 
	
		def mailId = sesMail {
			to "lucastex@gmail.com"
			bcc "l.ucastex@gmail.com", "lu.castex@gmail.com"			
			subject "test plain text mail"
			body "sendPlainTextMailMultipleBCC (${new Date().format('dd/MM/yyyy HH:mm')})"
		}
		
		render "E-mail sent: ${mailId}"
	}
    
}
