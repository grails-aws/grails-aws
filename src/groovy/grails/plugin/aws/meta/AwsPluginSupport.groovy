package grails.plugin.aws.meta

import org.apache.log4j.Logger
import grails.spring.BeanBuilder
import grails.plugin.aws.AWSGenericTools
import grails.plugin.aws.s3.AWSS3Tools
import grails.plugin.aws.s3.S3FileUpload
import grails.plugin.aws.ses.SendSesMail
import grails.plugin.aws.util.MetaClassInjector
import grails.plugin.aws.util.ConfigReader
import grails.plugin.aws.AWSCredentialsHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class AwsPluginSupport {
	
	private static def log = Logger.getLogger(AwsPluginSupport.class)
	def static configurationReader = new ConfigReader(ConfigurationHolder.config)

	static doWithWebDescriptor = {}
	static doWithApplicationContext = {}
	    
	static doWithSpring = { 

		credentialsHolder(AWSCredentialsHolder) {
			accessKey  = AwsPluginSupport.configurationReader.read("grails.plugin.aws.credentials.accessKey")
			secretKey  = AwsPluginSupport.configurationReader.read("grails.plugin.aws.credentials.secretKey")
			properties = AwsPluginSupport.configurationReader.read("grails.plugin.aws.credentials.properties")
		}
	
		sendSesMail(SendSesMail) {
			credentialsHolder = ref('credentialsHolder')
			from              = AwsPluginSupport.configurationReader.read("grails.plugin.aws.ses.from")
			catchall          = AwsPluginSupport.configurationReader.read("grails.plugin.aws.ses.catchall")
		}
		
		s3FileUpload(S3FileUpload) { bean ->
			bean.singleton = false
			credentialsHolder = ref('credentialsHolder')
			acl               = AwsPluginSupport.configurationReader.read("grails.plugin.aws.s3.acl", "public")
			bucket            = AwsPluginSupport.configurationReader.read("grails.plugin.aws.s3.bucket")
			bucketLocation    = AwsPluginSupport.configurationReader.read("grails.plugin.aws.s3.bucketLocation")
			rrs               = Boolean.valueOf(AwsPluginSupport.configurationReader.read("grails.plugin.aws.s3.rrs", "true"))
		}
		
		awsS3(AWSS3Tools) {
			credentialsHolder = ref('credentialsHolder')
		}
		
		aws(AWSGenericTools) {
			awsS3 = ref('awsS3')
		}
	}
	
	static onConfigChange = { event ->
		
		configurationReader  = new ConfigReader(ConfigurationHolder.config)

		def credentialsHolderBean          = event.ctx.getBean('credentialsHolder')
		credentialsHolderBean.accessKey    = AwsPluginSupport.configurationReader.read("grails.plugin.aws.credentials.accessKey")
		credentialsHolderBean.secretKey    = AwsPluginSupport.configurationReader.read("grails.plugin.aws.credentials.secretKey")
		credentialsHolderBean.properties   = AwsPluginSupport.configurationReader.read("grails.plugin.aws.credentials.properties")	
		
		def sendSesMailBean                = event.ctx.getBean('sendSesMail')
		sendSesMailBean.from               = AwsPluginSupport.configurationReader.read("grails.plugin.aws.ses.from")
		sendSesMailBean.catchall           = AwsPluginSupport.configurationReader.read("grails.plugin.aws.ses.catchall")
		
		def s3FileUploadBean               = event.ctx.getBean('s3FileUpload')	
		s3FileUploadBean.acl               = AwsPluginSupport.configurationReader.read("grails.plugin.aws.s3.acl", "public")
		s3FileUploadBean.bucket            = AwsPluginSupport.configurationReader.read("grails.plugin.aws.s3.bucket")
		s3FileUploadBean.bucketLocation    = AwsPluginSupport.configurationReader.read("grails.plugin.aws.s3.bucketLocation")
		s3FileUploadBean.rrs               = Boolean.valueOf(AwsPluginSupport.configurationReader.read("grails.plugin.aws.s3.rrs", "true"))
	}

	static onChange = { event ->
		if (event.source) {
			MetaClassInjector.injectIntegerMethods()
			MetaClassInjector.injectS3UploadMethods()
			MetaClassInjector.injectSesMethods(application, event.ctx)
		}
	}

	static doWithDynamicMethods = { applicationContext ->
		MetaClassInjector.injectIntegerMethods()
		MetaClassInjector.injectS3UploadMethods()
		MetaClassInjector.injectSesMethods(application, applicationContext)
	}	
}