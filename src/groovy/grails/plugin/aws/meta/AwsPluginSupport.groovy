package grails.plugin.aws.meta

import grails.plugin.aws.AWSCredentialsHolder
import grails.plugin.aws.AWSGenericTools
import grails.plugin.aws.s3.AWSS3Tools
import grails.plugin.aws.s3.S3FileUpload
import grails.plugin.aws.ses.SendSesMail
import grails.plugin.aws.swf.AWSSWFTools
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

        String accessKeyVal, secretKeyVal, propertiesVal
        ( accessKeyVal, secretKeyVal, propertiesVal ) =
            ["credentials.accessKey", "credentials.secretKey", "credentials.properties"].collect {
                readString( it ) ?: null
            }

        credentialsHolder(AWSCredentialsHolder) {
            accessKey  = accessKeyVal
            secretKey  = secretKeyVal
            properties = propertiesVal
        }

        def fromVal, catchallVal, regionVal
        ( fromVal, catchallVal, regionVal ) =
            [ "ses.from", "ses.catchall", "ses.region" ].collect { read( it ) ?: null }

        sendSesMail(SendSesMail) { bean ->
            bean.singleton    = false
            credentialsHolder = ref('credentialsHolder')
            from              = fromVal
            catchall          = catchallVal
            region            = regionVal
        }

        def aclVal, bucketVal, bucketLocationVal
        ( aclVal, bucketVal, bucketLocationVal ) =
            ["s3.acl", "s3.bucket", "s3.bucketLocation"].collect {
                (it == "s3.acl" ? read( it, "public" ) : read( it )) ?: null
            }

        s3FileUpload(S3FileUpload) { bean ->
            bean.singleton    = false
            credentialsHolder = ref('credentialsHolder')
            acl               = aclVal
            bucket            = bucketVal
            bucketLocation    = bucketLocationVal
            rrs               = Boolean.valueOf(read("s3.rrs", "true") as boolean)
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
		credentialsHolderBean.accessKey    = readString("credentials.accessKey")
		credentialsHolderBean.secretKey    = readString("credentials.secretKey")
		credentialsHolderBean.properties   = readString("credentials.properties")

		def sendSesMailBean                = event.ctx.sendSesMail
		sendSesMailBean.from               = read("ses.from")
		sendSesMailBean.catchall           = read("ses.catchall")

		def s3FileUploadBean               = event.ctx.s3FileUpload
		s3FileUploadBean.acl               = read("s3.acl", "public")
		s3FileUploadBean.bucket            = read("s3.bucket")
		s3FileUploadBean.bucketLocation    = read("s3.bucketLocation")
		s3FileUploadBean.rrs               = Boolean.valueOf(read("s3.rrs", "true") as boolean)
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

	private static String readString(String name) {
		read(name)
	}
	private static read(String name, defaultValue = null) {
		configurationReader.read("grails.plugin.aws." + name, defaultValue)
	}
}
