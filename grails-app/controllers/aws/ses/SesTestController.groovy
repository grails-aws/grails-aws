package aws.ses

class SesTestController {
	
	def sesTestService

    def sendPlainTextMail = { 
	
		def mailId = sesMail {
			to "lucastex@gmail.com"
			subject "test plain text mail"
			body "sendPlainTextMail (${new Date().format('dd/MM/yyyy HH:mm')})"
		}
		
		render "E-mail sent: ${mailId}"
	}

    def sendPlainTextMailWithOneReplyTo = { 
	
		def mailId = sesMail {
			to "lucastex@gmail.com"
			subject "test plain text mail with one reply-to address"
			replyTo "test-aws-plugin-1@gmail.com"
			body "sendPlainTextMailWithOneReplyTo (${new Date().format('dd/MM/yyyy HH:mm')})"
		}
		
		render "E-mail sent: ${mailId}"
	}

    def sendPlainTextMailWithReplyToList = { 
	
		def mailId = sesMail {
			to "lucastex@gmail.com"
			subject "test plain text mail with one list of reply-to addresses"
			replyTo "test-aws-plugin-1@gmail.com", "test-aws-plugin-2@gmail.com"
			body "sendPlainTextMailWithReplyToList (${new Date().format('dd/MM/yyyy HH:mm')})"
		}
		
		render "E-mail sent: ${mailId}"
	}

    def sendPlainTextMailFromService = { 
	
		def mailId = sesTestService.sendTestMail()
		
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
	
	def sendHtmlMail = { 
	
		def mailId = sesMail {
			to "lucastex@gmail.com"
			subject "test html mail"
			html "<html><body><h3>title</h3><strong>strong text</strong></body></html>"
		}
		
		render "E-mail sent: ${mailId}"
	}

	def sendHtmlMailFromTemplate = { 
			
		def mailId = sesMail {
			to "lucastex@gmail.com"
			subject "test html mail"
			html g.render(template: "/email-templates/template", model: [name: "Lucas", now: new Date()])
		}
		
		render "E-mail sent: ${mailId}"
	}    
}
