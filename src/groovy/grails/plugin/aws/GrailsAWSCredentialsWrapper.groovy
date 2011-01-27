package grails.plugin.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.PropertiesCredentials

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class GrailsAWSCredentialsWrapper {
	
	static def accessKey
	static def secretKey
	static def properties
	
	static def loaded = false
	static AWSCredentials awsCredentials
	
	public static AWSCredentials defaultCredentials() {
		
		if (!loaded) {
			println "Loading AWS credentials from application config"
			def applicationConfig = ConfigurationHolder.getConfig()
			
			accessKey  = applicationConfig.grails?.plugin?.aws?.credentials?.accessKey
			secretKey  = applicationConfig.grails?.plugin?.aws?.credentials?.secretKey
			properties = applicationConfig.grails?.plugin?.aws?.credentials?.properties
			
			//load from properties, prefered
			if (properties) {
				awsCredentials = new PropertiesCredentials(new File(properties))
				loaded = true
			} else {
				
				//plain text config
				if (accessKey && secretKey) {
					awsCredentials = new BasicAWSCredentials(accessKey, secretKey)
					loaded = true
				} else {
					throw new GrailsAWSException("Please check user guide to see how you should configure AWS credentials")
				}
			}
		}
		
		return awsCredentials
	}
	
	public static reload() {
		loaded = false
	}
}