import groovy.util.ConfigSlurper
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.PropertiesCredentials

includeTargets << grailsScript("_GrailsPackage")

target (readAwsCredentials: "Reads user AWS Credentials from Config.groovy") {

	depends(createConfig)
	
	println config
	
	//def config = new ConfigSlurper().parse(new File('grails-app/conf/Config.groovy').toURL())
	def accessKey  = config.grails?.plugin?.aws?.credentials?.accessKey
	def secretKey  = config.grails?.plugin?.aws?.credentials?.secretKey
	def properties = config.grails?.plugin?.aws?.credentials?.properties
	
	def awsCredentials 
	
	//load from properties, prefered
	if (properties) {
		awsCredentials = new PropertiesCredentials(new File(properties))
	} else {
		
		//plain text config
		if (accessKey && secretKey) {
			awsCredentials = new BasicAWSCredentials(accessKey, secretKey)
		} else {
			throw new Exception("Please check user guide to see how you should configure AWS credentials")
		}
	}	

	ant.property ( name: "accessKey", value: awsCredentials.getAWSAccessKeyId() )
	ant.property ( name: "secretKey", value: awsCredentials.getAWSSecretKey() )

}