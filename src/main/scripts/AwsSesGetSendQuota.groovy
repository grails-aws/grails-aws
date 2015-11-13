import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient

includeTargets << new File(awsPluginDir, "scripts/_ReadAwsCredentials.groovy")

target(awsSesGetSendQuota: "Queries about the user quota in AWS SES") {

	depends (readAwsCredentials)

	def credentials = new BasicAWSCredentials(accessKey, secretKey)
	def ses = new AmazonSimpleEmailServiceClient(credentials)

	def sendQuotaResult = ses.getSendQuota()
	println "[AWS SES] The maximum number of emails the user is allowed to send in a 24-hour interval: ${sendQuotaResult.getMax24HourSend()}"
	println "[AWS SES] The maximum number of emails the user is allowed to send per second: ${sendQuotaResult.getMaxSendRate()}"
	println "[AWS SES] The number of emails sent during the previous 24 hours: ${sendQuotaResult.getSentLast24Hours()}"
}

setDefaultTarget(awsSesGetSendQuota)
