package grails.aws

import grails.plugin.aws.AWSCredentialsHolder
import grails.plugin.aws.AWSGenericTools
import grails.plugin.aws.s3.AWSS3Tools
import grails.plugin.aws.s3.S3FileUpload
import grails.plugin.aws.ses.SendSesMail
import grails.plugin.aws.swf.AWSSWFTools
import grails.plugin.aws.util.ConfigReader
import grails.plugin.aws.util.MetaClassInjector
import grails.plugins.Plugin

class GrailsAwsGrailsPlugin extends Plugin {

	def grailsVersion = "3.0.0 > *"
	def loadAfter = ['services', 'controllers']
	def observe = ['services', 'controllers']
	def pluginExcludes = [
			"grails-app/views/**",
			"grails-app/controllers/**/*Controller.groovy",
			"grails-app/services/**/*Service.groovy",
			"src/docs/**",
			"testScripts/*.groovy"
	]

	def title = "Grails AWS Plugin"
	def description = "Amazon Web Services (AWS) grails plugin provides easy access to simpler functions of AWS"

	def documentation = "https://grails-aws.github.io/grails-aws/$version/"
	def license = "APACHE"
	def organization = [name: "Grails AWS", url: "https://github.com/grails-aws"]
	def developers = [[name: "Lucas Teixeira", email: "lucastex@gmail.com"],
					  [name: "Jay Prall", email: "jay@prall.net"]]
	def scm = [url: "https://github.com/grails-aws/grails-aws"]

	def issueManagement = [system: "GITHUB", url: "https://github.com/grails-aws/grails-aws/issues"]

	Closure doWithSpring() {
		{ ->
			MetaClassInjector.application = grailsApplication

			String accessKeyVal = readString('credentials.accessKey')
			String secretKeyVal = readString('credentials.secretKey')
			String propertiesVal = readString('credentials.properties')

			credentialsHolder(AWSCredentialsHolder) {
				accessKey = accessKeyVal
				secretKey = secretKeyVal
				properties = propertiesVal
			}

			String fromVal = readString('ses.from')
			String catchallVal = readString('ses.catchall')
			String regionVal = readString('ses.region')

			sendSesMail(SendSesMail) { bean ->
				bean.singleton = false
				credentialsHolder = ref('credentialsHolder')
				from = fromVal
				catchall = catchallVal
				region = regionVal
			}

			def aclVal, bucketVal, bucketLocationVal
			(aclVal, bucketVal, bucketLocationVal) =
					["s3.acl", "s3.bucket", "s3.bucketLocation"].collect {
						(it == "s3.acl" ? read(it, "public") : read(it)) ?: null
					}

			s3FileUpload(S3FileUpload) { bean ->
				bean.singleton = false
				credentialsHolder = ref('credentialsHolder')
				acl = aclVal
				bucket = bucketVal
				bucketLocation = bucketLocationVal
				rrs = Boolean.valueOf(read("s3.rrs", "true") as boolean)
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
	}

	void doWithDynamicMethods() {
		MetaClassInjector.injectIntegerMethods()
		MetaClassInjector.injectS3UploadMethods(applicationContext)
		MetaClassInjector.injectSesMethods(grailsApplication, applicationContext)
	}

	void doWithApplicationContext() {
		// TODO Implement post initialization spring config (optional)
	}

	void onChange(Map<String, Object> event) {
		if (event.source) {
			MetaClassInjector.injectIntegerMethods()
			MetaClassInjector.injectS3UploadMethods(event.ctx)
			MetaClassInjector.injectSesMethods(grailsApplication, event.ctx)
		}
	}

	void onConfigChange(Map<String, Object> event) {
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

	void onShutdown(Map<String, Object> event) {
		// TODO Implement code that is executed when the application shuts down (optional)
	}

	private String readString(String name) {
		read(name)
	}

	private def read(String property, defaultValue = null){
		grailsApplication.config.get('grails.plugin.aws.' + property, defaultValue)
	}
}
