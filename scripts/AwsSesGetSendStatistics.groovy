import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient

includeTargets << new File(awsPluginDir, "scripts/_ReadAwsCredentials.groovy")

target(awsSesGetSendStatistics: "Queries about the sending statistics in AWS SES") {

	depends (readAwsCredentials)

	def credentials = new BasicAWSCredentials(accessKey, secretKey)
	def ses = new AmazonSimpleEmailServiceClient(credentials)

	def sendStatisticsResult = ses.getSendStatistics()
	if (sendStatisticsResult.getSendDataPoints().size() > 0) {

		def intervals = sendStatisticsResult.getSendDataPoints().sort { it.timestamp }

		println "[AWS SES]  -------------------------------------------------------------------------------"
		println "[AWS SES] |    time range    | attempts | rejects (SES) | complaints (recipient) | bounces |"
		println "[AWS SES] |-------------------------------------------------------------------------------|"

		intervals.each { dp ->

			def _timestamp = dp.timestamp?.format('yyyy/MM/dd HH:mm')
			def _attempts = dp.deliveryAttempts?.toString()?.center(7, " ")
			def _rejects = dp.rejects?.toString()?.center(13, " ")
			def _complaints = dp.complaints?.toString()?.center(22, " ")
			def _bounces = dp.bounces?.toString()?.center(7, " ")

			println "[AWS SES] | ${_timestamp} | ${_attempts} | ${_rejects} | ${_complaints} | ${_bounces} |"
		}

		println "[AWS SES]  -------------------------------------------------------------------------------"

	} else {
		println "[AWS SES] No statistics available for this account"
	}

}

setDefaultTarget(awsSesGetSendStatistics)
