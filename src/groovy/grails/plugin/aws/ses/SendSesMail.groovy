package grails.plugin.aws.ses

import grails.plugin.aws.GrailsAWSException
import grails.plugin.aws.GrailsAWSCredentialsWrapper

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.simpleemail.model.Body
import com.amazonaws.services.simpleemail.model.Content
import com.amazonaws.services.simpleemail.model.Message
import com.amazonaws.services.simpleemail.model.Destination
import com.amazonaws.services.simpleemail.model.SendEmailResult
import com.amazonaws.services.simpleemail.model.SendEmailRequest
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient

import org.codehaus.groovy.grails.web.util.StreamCharBuffer

class SendSesMail {
	
	def from
	def to  = []
	def cc  = []
	def bcc = []
	
	def body     = ""
	def html     = ""
	def subject  = ""
	def catchall
	
	def credentials
	
	public SendSesMail(AWSCredentials defaultCredentials, String defaultFrom, String catchall) {
		this.from = defaultFrom
		this.credentials = defaultCredentials
		this.catchall = catchall
	}
	
	//from
	void from(String _from) { this.from = _from }
	
	//to
	void to(String ... _to) { this.to?.addAll(_to) }
	
	//cc
	void cc(String ... _cc) { this.cc?.addAll(_cc) }

	//bcc
	void bcc(String ... _bcc) { this.bcc?.addAll(_bcc) }
	
	//body
	void body(String _body) { this.body = _body }

	//html
	void html(String _html) { this.html = _html }
	void html(StreamCharBuffer _html) { this.html = _html.toString() }
	
	//subject
	void subject(String _subject) { this.subject = _subject }

	//credentials
	void credentials(String _credentials) { this.credentials = _credentials }
	
	def send(Closure cls) {
		
		if (cls) {
			cls.delegate = this
			cls()
		}
		
		def sesService = new AmazonSimpleEmailServiceClient(credentials)
		
		if (!from) {
			throw new GrailsAWSException("[SES] You cannot send e-mail without specifing 'from'.")
		}
		
		def destination = new Destination()
		if (catchall) {
			
			destination.withToAddresses(catchall)
			
		} else {
			
			destination.toAddresses  = to  ?: null
			destination.ccAddresses  = cc  ?: null
			destination.bccAddresses = bcc ?: null		
		}
								
		def mailBody = new Body()
		mailBody.html = html ? new Content(html) : null
		mailBody.text = body ? new Content(body) : null
		
		def message = new Message()
		message.subject = subject ? new Content(subject) : null
		message.body = mailBody
		
		def emailRequest = new SendEmailRequest(from, destination, message)		
		def emailResult = sesService.sendEmail(emailRequest)
		return emailResult.messageId
	}
}