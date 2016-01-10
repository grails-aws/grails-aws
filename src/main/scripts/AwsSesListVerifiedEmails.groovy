import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient

includeTargets << new File(awsPluginDir, "scripts/_ReadAwsCredentials.groovy")

target(awsSesListVerifiedEmails: "List all e-mails that this account is verified to use") {

	depends (readAwsCredentials)

	def credentials = new BasicAWSCredentials(accessKey, secretKey)
	def ses = new AmazonSimpleEmailServiceClient(credentials)

	def verifiedEmails = ses.listVerifiedEmailAddresses()
	if (verifiedEmails.getVerifiedEmailAddresses()) {
		verifiedEmails.getVerifiedEmailAddresses().eachWithIndex { email, index ->
			println "[AWS SES] ${index+1}) ${email}"
		}
	} else {
		println "[AWS SES] There is no verified e-mail adresses for this account"
	}
}

setDefaultTarget(awsSesListVerifiedEmails)
