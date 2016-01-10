import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest

includeTargets << new File(awsPluginDir, "scripts/_ReadAwsCredentials.groovy")

target(awsSesVerifyEmail: "Verify this e-mail address to use with AWS SES") {

	depends (readAwsCredentials)

	ant.input(message: "Enter the e-mail address to verify: ", addproperty: "emailToVerify")

	def credentials = new BasicAWSCredentials(accessKey, secretKey)
	def ses = new AmazonSimpleEmailServiceClient(credentials)

	ses.verifyEmailAddress(new VerifyEmailAddressRequest().withEmailAddress(emailToVerify))
	println "[AWS SES] Please check the email address ${emailToVerify} to verify it"
}

setDefaultTarget(awsSesVerifyEmail)
