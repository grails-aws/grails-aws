package grails.plugin.aws.ses

import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient
import com.amazonaws.services.simpleemail.model.*
import grails.plugin.aws.GrailsAWSException
import org.codehaus.groovy.grails.web.util.StreamCharBuffer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Part
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource
import java.nio.ByteBuffer

class SendSesMail {

    public static final String US__EAST_1 = 'US_EAST_1'
    def to	= []
	def cc	= []
	def bcc = []
	def replyTo = []
	def attachments = []
	def byteAttachments = []
	def charset = "UTF-8"

	def body	 = ""
	def html	 = ""
	def subject	 = ""

	//injected
	def from
	def catchall
	def credentialsHolder
    String region

    //set from injected props
    Region awsRegion

    SendSesMail() {
        setRegion(US__EAST_1) //default to east
    }

    private static Logger log = LoggerFactory.getLogger(this)

    void setRegion(region) {
        this.region = region ?: US__EAST_1 //default to east if not set
        this.awsRegion = Region.getRegion(Regions.valueOf(this.region))
        log.debug("SES Region set to: " + awsRegion.name)
    }

    //from
	void from(String _from) {
		from = _from
		log.debug "Setting from address to ${from}"
	}

	//to
	void to(String ... _to) {
		to?.addAll(_to)
		log.debug "Setting 'to' addresses to ${to}"
	}

	//cc
	void cc(String ... _cc) {
		cc?.addAll(_cc)
		log.debug "Setting 'cc' addresses to ${cc}"
	}

	//bcc
	void bcc(String ... _bcc) {
		bcc?.addAll(_bcc)
		log.debug "Setting 'bcc' addresses to ${bcc}"
	}

	//reply to
	void replyTo(String ... _replyTo) {
		replyTo?.addAll(_replyTo)
		log.debug "Setting 'replyTo' addresses to ${replyTo}"
	}

	//reply to
	void attach(String ... _attachFile) {
		attachments?.addAll(_attachFile)
		log.debug "Setting 'attachments' files to ${attachments}"
	}

    void attach(String filename, String contentType, byte[] bytes) {
		def map = [filename: filename, contentType: contentType, bytes: bytes]
		this.byteAttachments.add map
		log.debug "Setting 'byteAttachment' to: ${byteAttachments.filename}"
    }

	// charset
	void charset(String charset) {
		this.charset = charset
		log.debug "Setting 'charset' files to ${this.charset}"
	}
	
	//body
	void body(String _body) {
		body = _body
		log.debug "Setting body message"
	}

	//html
	void html(String _html) {
		html = _html
		log.debug "Setting html message"
	}
	void html(StreamCharBuffer _html) {
		html = _html.toString()
		log.debug "Setting html message from StreamCharBuffer"
	}

	//subject
	void subject(String _subject) {
		subject = _subject
		log.debug "Setting message subject to ${subject}"
	}

	//send the mail message
	def send(Closure cls) {
		log.debug "attempting to send mail..."
		setClosureData(cls)
		checkValidFromAddress()

		if (attachments && attachments?.size() > 0 || byteAttachments && byteAttachments.size() > 0) {
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
			destination.toAddresses	 = to  ?: []
			destination.ccAddresses	 = cc  ?: []
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
		def credentials	 = credentialsHolder.buildAwsSdkCredentials()
		def sesService	 = new AmazonSimpleEmailServiceClient(credentials)
        sesService.region = awsRegion
		def emailRequest = new SendEmailRequest(_from, _destination, _message)

		if (replyTo) {
			emailRequest.setReplyToAddresses(replyTo)
		}

		return sesService.sendEmail(emailRequest).messageId
	}

	def sendRawMail(_from) {

		def s = Session.getInstance(new Properties(), null)
		def msg = new MimeMessage(s)

		//from
		msg.setFrom(new InternetAddress(_from))

		if (catchall) {
			msg.addRecipients(javax.mail.Message.RecipientType.TO, new InternetAddress(catchall))
		} else {
			to.each	 { msg.addRecipients(javax.mail.Message.RecipientType.TO,  new InternetAddress(it)) }
			cc.each	 { msg.addRecipients(javax.mail.Message.RecipientType.CC,  new InternetAddress(it)) }
			bcc.each { msg.addRecipients(javax.mail.Message.RecipientType.BCC, new InternetAddress(it)) }
		}

		//reply-to
		if (replyTo) {

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
			def dataSource = new FileDataSource(fileToAttach)

			attBodyPart.setDataHandler(new DataHandler(dataSource))
			attBodyPart.setFileName(fileToAttach.name)
			attBodyPart.setHeader("Content-Type", dataSource.getContentType())
			attBodyPart.setHeader("Content-ID", fileToAttach.getName())
			attBodyPart.setDisposition(Part.ATTACHMENT)

			mp.addBodyPart(attBodyPart)
		}

        byteAttachments.each { map ->

			def attBodyPart = new MimeBodyPart()
			def dataSource = new ByteArrayDataSource(map.bytes, map.contentType)

			attBodyPart.setDataHandler(new DataHandler(dataSource))
			attBodyPart.setFileName(map.filename)
			attBodyPart.setHeader("Content-Type", dataSource.getContentType())
			attBodyPart.setHeader("Content-ID", map.filename)
			attBodyPart.setDisposition(Part.ATTACHMENT)

			mp.addBodyPart(attBodyPart)
        }

		msg.setContent(mp)

		def out = new ByteArrayOutputStream()
		msg.writeTo(out)

		def rm = new RawMessage()
		rm.setData(ByteBuffer.wrap(out.toString().getBytes()))

		//sending e-mail
		def credentials	 = credentialsHolder.buildAwsSdkCredentials()
		def sesService	 = new AmazonSimpleEmailServiceClient(credentials)
        sesService.region = awsRegion
		def rawEmailRequest = new SendRawEmailRequest()
		rawEmailRequest.source = _from
		rawEmailRequest.rawMessage = rm

		return sesService.sendRawEmail(rawEmailRequest).messageId
	}
}
