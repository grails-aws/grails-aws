import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient
import com.amazonaws.services.simpleemail.model.Body
import com.amazonaws.services.simpleemail.model.Content
import com.amazonaws.services.simpleemail.model.Destination
import com.amazonaws.services.simpleemail.model.Message
import com.amazonaws.services.simpleemail.model.SendEmailRequest

includeTargets << new File(awsPluginDir, "scripts/_ReadAwsCredentials.groovy")

target(awsSesSendPingMail: "Send test e-mail, so user can check if e-mail is verified and working") {

	println """************************************************************
* Attention: If you only subscribed to Amazon SES and      *
* still didn't get access for production using, you'll     *
* only be allowed to send e-mails 'from' AND 'to' emails    *
* that has been verified with amazon.                      *
* So, you'll have to use the 'grails aws-ses-verify-email' *
* for both sender and recipient adresses. After getting    *
* production access, this won't be needed.                 *
************************************************************"""

	depends (readAwsCredentials)

	def credentials = new BasicAWSCredentials(accessKey, secretKey)
	def ses = new AmazonSimpleEmailServiceClient(credentials)

	ant.input(message: "Enter the sender e-mail [has to be verified]: ", addproperty: "senderEmail")
	ant.input(message: "Enter the e-mail address to recieve ping message: ", addproperty: "recipientEmail")

	def message = new Message(new Content("[Grails AWS Plugin] Ping e-mail"), new Body(new Content("Test ok!")))
	def result = ses.sendEmail(new SendEmailRequest(senderEmail, new Destination().withToAddresses(recipientEmail), message))

	println "[AWS SES] E-mail queued. Id: ${result.getMessageId()}"
}

setDefaultTarget(awsSesSendPingMail)
