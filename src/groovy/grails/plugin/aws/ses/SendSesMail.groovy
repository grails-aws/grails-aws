package grails.plugin.aws.ses

import org.apache.log4j.Logger

import java.nio.ByteBuffer

import javax.mail.Message
import javax.mail.Session
import javax.mail.Address
import javax.mail.internet.MimeMessage
import javax.mail.Message.RecipientType
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource
import javax.mail.internet.InternetAddress

import javax.activation.DataHandler
import javax.activation.MimetypesFileTypeMap

import grails.plugin.aws.GrailsAWSException

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.simpleemail.model.Body
import com.amazonaws.services.simpleemail.model.Content
import com.amazonaws.services.simpleemail.model.Message
import com.amazonaws.services.simpleemail.model.RawMessage
import com.amazonaws.services.simpleemail.model.Destination
import com.amazonaws.services.simpleemail.model.SendEmailResult
import com.amazonaws.services.simpleemail.model.SendEmailRequest
import com.amazonaws.services.simpleemail.model.SendRawEmailResult
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient

import org.codehaus.groovy.grails.web.util.StreamCharBuffer

class SendSesMail {
	
	def to  = []
	def cc  = []
	def bcc = []
	def replyTo = []
	def attachments = []
    def charset = "UTF-8"
	
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

	//reply to
	void replyTo(String ... _replyTo) {
		this.replyTo?.addAll(_replyTo)
		log.debug "Setting 'replyTo' addresses to ${this.replyTo}"
	}
	
	//reply to
	void attach(String ... _attachFile) {
		this.attachments?.addAll(_attachFile)
		log.debug "Setting 'attachments' files to ${this.attachments}"
	}

    // charset
    void charset(String charset) {
        this.charset = charset
        log.debug "Setting 'charset' files to ${this.charset}"
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
		
		if (attachments && attachments?.size() > 0) {
			sendRawMail(from)
		} else {
			def destination = buildSimpleDestination()
			def message = buildSimpleMessage()
			def messageId = sendSimpleMail(from, destination, message)
			log.debug "Mail message sent with id ${messageId}"
			return messageId
		}
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
	def buildSimpleDestination() {
		
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
	def buildSimpleMessage() {
		
		def mailBody = new Body()
		mailBody.html = html ? new Content(html).withCharset(charset) : null
		mailBody.text = body ? new Content(body).withCharset(charset) : null
		
		def message = new Message()
		message.subject = subject ? new Content(subject).withCharset(charset) : null
		message.body = mailBody
		
		return message
	}
	
	//method to send the message to this destination, using this from
	def sendSimpleMail(_from, _destination, _message) {
		def credentials  = credentialsHolder.buildAwsSdkCredentials()
		def sesService   = new AmazonSimpleEmailServiceClient(credentials)
		def emailRequest = new SendEmailRequest(_from, _destination, _message)		
		
		if (replyTo && replyTo?.size() > 0) {
			emailRequest.setReplyToAddresses(replyTo) 
		}
		
		def emailResult  = sesService.sendEmail(emailRequest)
		return emailResult.messageId
	}
	
	def sendRawMail(_from) {
		
		def s = Session.getInstance(new Properties(), null)
	    def msg = new MimeMessage(s)

		//from
	    msg.setFrom(new InternetAddress(_from))
	
		if (catchall) {	
			msg.addRecipients(javax.mail.Message.RecipientType.TO, new InternetAddress(catchall))
		} else {
			to.each  { msg.addRecipients(javax.mail.Message.RecipientType.TO,  new InternetAddress(it)) }
			cc.each  { msg.addRecipients(javax.mail.Message.RecipientType.CC,  new InternetAddress(it)) }
			bcc.each { msg.addRecipients(javax.mail.Message.RecipientType.BCC, new InternetAddress(it)) }
		}
		
		//reply-to
		if (replyTo && replyTo?.size() > 0) {
			
			InternetAddress[] replyToArray = new InternetAddress[replyTo.size()]
			replyTo.eachWithIndex { email, index ->
				replyToArray[index] = new InternetAddress(email)
			}
			
			msg.setReplyTo(replyToArray) 
		}
		
		//subject
	    msg.setSubject(subject, charset)

		//multipart message
	    MimeMultipart mp = new MimeMultipart()

		//body text part
		if (body) {
			def part = new MimeBodyPart()
		    part.setText(body, charset, "plain")
		    mp.addBodyPart(part)
		}
		
		//body html part
		if (html) {
			def part = new MimeBodyPart()
            part.setText(html, charset, "html")
		    mp.addBodyPart(part)
		}
		
		attachments.each { fileNameToAttach ->

			def fileToAttach = new File(fileNameToAttach)
			if (!fileToAttach.exists()) {
				throw new GrailsAWSException("[SES] Attachment [${fileToAttach.name}] could not be found in your local storage.")
			}
			
			def attBodyPart = new MimeBodyPart()
		    def dataSource = new ByteArrayDataSource(fileToAttach.bytes, new MimetypesFileTypeMap().getContentType(fileToAttach))
		    attBodyPart.setDataHandler(new DataHandler(dataSource))
		    attBodyPart.setFileName(fileToAttach.name)
			mp.addBodyPart(attBodyPart)
		}

	    msg.setContent(mp)

	    def out = new ByteArrayOutputStream()
	    msg.writeTo(out)

	    def rm = new RawMessage()
	    rm.setData(ByteBuffer.wrap(out.toString().getBytes()))

		//sending e-mail
		def credentials  = credentialsHolder.buildAwsSdkCredentials()
		def sesService   = new AmazonSimpleEmailServiceClient(credentials)
		def rawEmailRequest = new SendRawEmailRequest()
		rawEmailRequest.source = _from
		rawEmailRequest.rawMessage = rm

		def emailResult  = sesService.sendRawEmail(rawEmailRequest)		
		return emailResult.messageId	
	}
}