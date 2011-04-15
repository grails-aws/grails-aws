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

class AwsGrailsPlugin {

    def version = "1.1.7.3"
    def grailsVersion = "1.3.0 > *"
    def dependsOn = [:]
    def loadAfter = ['services', 'controllers']
    def observe = ['services', 'controllers']
    def pluginExcludes = [
            "grails-app/views/**/*.gsp",
            "grails-app/controllers/**/*Controller.groovy",
            "grails-app/services/**/*Service.groovy",
            "grails-app/conf/Config.groovy",
            "grails-app/conf/DataSource.groovy",
            "grails-app/conf/UrlMappings.groovy"
    ]
    def watchedResources = [
    	"grails-app/services/**/*Service.groovy",
    	"grails-app/controllers/**/*Controller.groovy"
	]
    def author = "Lucas Teixeira"
    def authorEmail = "lucastex@gmail.com"
    def title = "Grails AWS Plugin"
    def description = "Amazon Web Services (AWS) grails plugin will provide easy access to simpler functions of AWS"
    def documentation = "http://blanq.github.com/grails-aws"

	def license = "APACHE"
	def organization = [ name: "Blanq", url: "http://www.blanq.com.br/" ]
	def developers = [[ name: "Lucas Teixeira", email: "lucastex@gmail.com" ]]
	def issueManagement = [ system: "JIRA", url: "http://jira.codehaus.org/browse/GRAILSPLUGINS" ]
	def scm = [ url: "https://github.com/blanq/grails-aws" ]
	
	def configurationReader = new ConfigReader(ConfigurationHolder.config)

	private static def log = Logger.getLogger(AwsGrailsPlugin.class)

    def doWithWebDescriptor = { xml ->
    }

    def doWithApplicationContext = { applicationContext ->
    }

	def doWithSpring = { 

		credentialsHolder(AWSCredentialsHolder) {
			accessKey  = configurationReader.read("grails.plugin.aws.credentials.accessKey")
			secretKey  = configurationReader.read("grails.plugin.aws.credentials.secretKey")
			properties = configurationReader.read("grails.plugin.aws.credentials.properties")
		}
	
		sendSesMail(SendSesMail) {
			credentialsHolder = ref('credentialsHolder')
			from              = configurationReader.read("grails.plugin.aws.ses.from")
			catchall          = configurationReader.read("grails.plugin.aws.ses.catchall")
		}
		
		s3FileUpload(S3FileUpload) { bean ->
			bean.singleton = false
			credentialsHolder = ref('credentialsHolder')
			acl               = configurationReader.read("grails.plugin.aws.s3.acl", "public")
			bucket            = configurationReader.read("grails.plugin.aws.s3.bucket")
			bucketLocation    = configurationReader.read("grails.plugin.aws.s3.bucketLocation")
			rrs               = Boolean.valueOf(configurationReader.read("grails.plugin.aws.s3.rrs", "true"))
		}
		
		awsS3(AWSS3Tools) {
			credentialsHolder = ref('credentialsHolder')
		}
		
		aws(AWSGenericTools) {
			awsS3 = ref('awsS3')
		}
	}


	def onConfigChange = { event ->
		
		configurationReader  = new ConfigReader(ConfigurationHolder.config)

		def credentialsHolderBean          = event.ctx.getBean('credentialsHolder')
		credentialsHolderBean.accessKey    = configurationReader.read("grails.plugin.aws.credentials.accessKey")
		credentialsHolderBean.secretKey    = configurationReader.read("grails.plugin.aws.credentials.secretKey")
		credentialsHolderBean.properties   = configurationReader.read("grails.plugin.aws.credentials.properties")	
		
		def sendSesMailBean                = event.ctx.getBean('sendSesMail')
		sendSesMailBean.from               = configurationReader.read("grails.plugin.aws.ses.from")
		sendSesMailBean.catchall           = configurationReader.read("grails.plugin.aws.ses.catchall")
		
		def s3FileUploadBean               = event.ctx.getBean('s3FileUpload')	
		s3FileUploadBean.acl               = configurationReader.read("grails.plugin.aws.s3.acl", "public")
		s3FileUploadBean.bucket            = configurationReader.read("grails.plugin.aws.s3.bucket")
		s3FileUploadBean.bucketLocation    = configurationReader.read("grails.plugin.aws.s3.bucketLocation")
		s3FileUploadBean.rrs               = Boolean.valueOf(configurationReader.read("grails.plugin.aws.s3.rrs", "true"))
		
	}

	def onChange = { event ->
		if (event.source) {
			injectMetaclassMethods(application, event.ctx)
		}
	}

	def doWithDynamicMethods = { applicationContext ->		
		injectMetaclassMethods(application, applicationContext)
	}
	
	def injectMetaclassMethods = { grailsApplication, applicationContext ->
		
		//inject helper methods on classes
		def injector = new MetaClassInjector()
		injector.injectIntegerMethods()
				
		//S3
		File.metaClass.s3upload = { Closure s3Config ->
			def s3FileUploadBean = applicationContext.getBean('s3FileUpload')
			s3FileUploadBean.fileUpload(delegate, s3Config)
		}
		
		//S3 handling on Inputstream objects
		InputStream.metaClass.s3upload = { String name, Closure s3Config ->
			def s3FileUploadBean = applicationContext.getBean('s3FileUpload')
			s3FileUploadBean.inputStreamUpload(delegate, name, s3Config)
		}

		//sesmail
		def targetClasses = []
		targetClasses.addAll(grailsApplication.controllerClasses)
		targetClasses.addAll(grailsApplication.serviceClasses)
		
		targetClasses.each { clazz ->
			
			clazz.metaClass.sesMail = { Closure sendConfigClosure ->
				
				def enabled = Boolean.valueOf(configurationReader.read("grails.plugin.aws.ses.enabled", "true"))
				if (!enabled) {
					log.info "[AWS SES] Aborting attemp to send e-mail. E-mail sending disabled on this environment"
					return
				}
				
				def sesBean = applicationContext.getBean('sendSesMail')
				sesBean.send(sendConfigClosure)
			}
		}
	}
}
