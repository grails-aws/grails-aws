import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient
import com.amazonaws.services.simpleemail.model.GetSendQuotaResult

includeTargets << grailsScript("Init")
includeTargets << new File("scripts/ReadAwsCredentials.groovy")

target(main: "Queries about the sending statistics in AWS SES") {

	depends (readAwsCredentials)
	
	def credentials = new BasicAWSCredentials(accessKey, secretKey)
	def ses = new AmazonSimpleEmailServiceClient(credentials)	

	def sendStatisticsResult = ses.getSendStatistics()
	if (sendStatisticsResult.getSendDataPoints().size() > 0) {
		
		sendStatisticsResult.getSendDataPoints().each { dataPoints ->
		
			println "[AWS SES] ${dataPoints.toString()}"
		}
		
	} else {
		println "[AWS SES] No statistics available for this account"
	}
	
}

setDefaultTarget(main)
