package aws.ses

class SesTestService {

    static transactional = false

    def sendTestMail() {

		def mailId = sesMail {
			to "lucastex@gmail.com"
			subject "test plain text mail"
			body "sendPlainTextMailFromService (${new Date().format('dd/MM/yyyy HH:mm')})"
		}

		return mailId
    }
}
