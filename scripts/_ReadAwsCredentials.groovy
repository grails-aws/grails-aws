import groovy.util.ConfigSlurper
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.PropertiesCredentials

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsPackage")

target (readAwsCredentials: "Reads user AWS Credentials from Config.groovy") {

	depends(compile, createConfig)

	def accessKey  = config.grails?.plugin?.aws?.credentials?.accessKey
	def secretKey  = config.grails?.plugin?.aws?.credentials?.secretKey
	def properties = config.grails?.plugin?.aws?.credentials?.properties

	//load from properties, prefered
	if (properties) {
		def awsCredentials = new PropertiesCredentials(new File(properties))
		accessKey = awsCredentials.getAWSAccessKeyId()
		secretKey = awsCredentials.getAWSSecretKey()
	}

	if (!accessKey || !secretKey) {
		println "[AWS ERROR] Please check user guide to see how you should configure AWS credentials"
		System.exit(1)
	}

	ant.property ( name: "accessKey", value: accessKey )
	ant.property ( name: "secretKey", value: secretKey )
}