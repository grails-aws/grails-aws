package grails.plugin.aws.ses

import org.apache.log4j.Logger

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
	
	def to  = []
	def cc  = []
	def bcc = []
	
	def body     = ""
	def html     = ""
	def subject  = ""
	
	//injected
	def from
	def catchall
	def credentials
	
	private static Logger log = Logger.getLogger(SendSesMail.class)
		
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
	
	//send the mail message
	def send(Closure cls) {
		setClosureData(cls)
		checkValidFromAddress()
		def destination = buildDestination()
		def message = buildMessage()
		return sendMail(from, destination, message)
	}
	
	//check if the 'from' address supplied is valid
	def checkValidFromAddress() {
		if (!from) { 
			throw new GrailsAWSException("[SES] You cannot send e-mail without specifing 'from'.")
		}
	}
	
	//method that overwrite and set parameter
	//passed in the send method closure
	def setClosureData(cls) {
		if (cls) {
			cls.delegate = this
			cls()
		}
	}
	
	//method to build a AWS Destination object
	def buildDestination() {
		
		def destination = new Destination()
		if (catchall) {	
			destination.withToAddresses(catchall)
		} else {
			destination.toAddresses  = to  ?: []
			destination.ccAddresses  = cc  ?: []
			destination.bccAddresses = bcc ?: []		
		}
		
		return destination
	}
	
	//method to build a AWS Message object
	def buildMessage() {
		
		def mailBody = new Body()
		mailBody.html = html ? new Content(html) : null
		mailBody.text = body ? new Content(body) : null
		
		def message = new Message()
		message.subject = subject ? new Content(subject) : null
		message.body = mailBody
		
		return message
	}
	
	//method to send the message to this destination, using this from
	def sendMail(_from, _destination, _message) {
		def sesService   = new AmazonSimpleEmailServiceClient(credentials)
		def emailRequest = new SendEmailRequest(_from, _destination, _message)		
		def emailResult  = sesService.sendEmail(emailRequest)
		return emailResult.messageId
	}
}