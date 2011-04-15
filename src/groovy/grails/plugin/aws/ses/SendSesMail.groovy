package grails.plugin.aws.ses

import org.apache.log4j.Logger

import grails.plugin.aws.GrailsAWSException

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
	def credentialsHolder
	
	private static def log = Logger.getLogger(SendSesMail.class)
		
	//from
	void from(String _from) { 
		this.from = _from
		log.debug "Setting from address to ${this.from}"
	}
	
	//to
	void to(String ... _to) { 
		this.to?.addAll(_to)
		log.debug "Setting 'to' addresses to ${this.to}"
	}
	
	//cc
	void cc(String ... _cc) { 
		this.cc?.addAll(_cc)
		log.debug "Setting 'cc' addresses to ${this.cc}"
	}

	//bcc
	void bcc(String ... _bcc) {
		this.bcc?.addAll(_bcc)
		log.debug "Setting 'bcc' addresses to ${this.bcc}"
	}
	
	//body
	void body(String _body) { 
		this.body = _body
		log.debug "Setting body message"
	}

	//html
	void html(String _html) { 
		this.html = _html
		log.debug "Setting html message"
	}
	void html(StreamCharBuffer _html) { 
		this.html = _html.toString()
		log.debug "Setting html message from StreamCharBuffer"
	}
	
	//subject
	void subject(String _subject) { 
		this.subject = _subject
		log.debug "Setting message subject to ${this.subject}"
	}
	
	//send the mail message
	def send(Closure cls) {
		log.debug "attemping to send mail..."
		setClosureData(cls)
		checkValidFromAddress()
		def destination = buildDestination()
		def message = buildMessage()
		def messageId = sendMail(from, destination, message)
		log.debug "Mail message sent with id ${messageId}"
		return messageId 
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
		def credentials  = credentialsHolder.buildAwsSdkCredentials()
		def sesService   = new AmazonSimpleEmailServiceClient(credentials)
		def emailRequest = new SendEmailRequest(_from, _destination, _message)		
		def emailResult  = sesService.sendEmail(emailRequest)
		return emailResult.messageId
	}
}