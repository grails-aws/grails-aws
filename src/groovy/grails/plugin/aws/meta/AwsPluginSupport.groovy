package grails.plugin.aws.meta

import grails.plugin.aws.AWSCredentialsHolder
import grails.plugin.aws.AWSGenericTools
import grails.plugin.aws.swf.AWSSWFTools
import grails.plugin.aws.s3.AWSS3Tools
import grails.plugin.aws.s3.S3FileUpload
import grails.plugin.aws.ses.SendSesMail
import grails.plugin.aws.util.ConfigReader
import grails.plugin.aws.util.MetaClassInjector

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AwsPluginSupport {

	private static Logger log = LoggerFactory.getLogger(this)
	private static ConfigReader configurationReader

	static doWithSpring = {
		
		MetaClassInjector.application = application
		
		configurationReader = new ConfigReader(application.config)

		credentialsHolder(AWSCredentialsHolder) {
			accessKey  = read("credentials.accessKey")
			secretKey  = read("credentials.secretKey")
			properties = read("credentials.properties")
		}

		sendSesMail(SendSesMail) { bean ->
			bean.singleton    = false
			credentialsHolder = ref('credentialsHolder')
			from              = read("ses.from")
			catchall          = read("ses.catchall")
		}

		s3FileUpload(S3FileUpload) { bean ->
			bean.singleton    = false
			credentialsHolder = ref('credentialsHolder')
			acl               = read("s3.acl", "public")
			bucket            = read("s3.bucket")
			bucketLocation    = read("s3.bucketLocation")
			rrs               = Boolean.valueOf(read("s3.rrs", "true").toString())
		}

		awsS3(AWSS3Tools) {
			credentialsHolder = ref('credentialsHolder')
		}

		awsSWF(AWSSWFTools) {
			credentialsHolder = ref('credentialsHolder')
		}

		aws(AWSGenericTools) {
			awsS3 = ref('awsS3')
			awsSWF = ref('awsSWF')
		}
	}

	static onConfigChange = { event ->

		configurationReader = new ConfigReader(application.config)

		def credentialsHolderBean          = event.ctx.credentialsHolder
		credentialsHolderBean.accessKey    = read("credentials.accessKey")
		credentialsHolderBean.secretKey    = read("credentials.secretKey")
		credentialsHolderBean.properties   = read("credentials.properties")

		def sendSesMailBean                = event.ctx.sendSesMail
		sendSesMailBean.from               = read("ses.from")
		sendSesMailBean.catchall           = read("ses.catchall")

		def s3FileUploadBean               = event.ctx.s3FileUpload
		s3FileUploadBean.acl               = read("s3.acl", "public")
		s3FileUploadBean.bucket            = read("s3.bucket")
		s3FileUploadBean.bucketLocation    = read("s3.bucketLocation")
		s3FileUploadBean.rrs               = Boolean.valueOf(read("s3.rrs", "true").toString())
	}

	static onChange = { event ->
		if (event.source) {
			MetaClassInjector.injectIntegerMethods()
			MetaClassInjector.injectS3UploadMethods(event.ctx)
			MetaClassInjector.injectSesMethods(application, event.ctx)
		}
	}

	static doWithDynamicMethods = { applicationContext ->
		MetaClassInjector.injectIntegerMethods()
		MetaClassInjector.injectS3UploadMethods(applicationContext)
		MetaClassInjector.injectSesMethods(application, applicationContext)
	}

	private static read(String name, defaultValue = null) {
		configurationReader.read("grails.plugin.aws." + name, defaultValue)
	}
}
