import org.apache.log4j.Logger
import grails.plugin.aws.s3.S3FileUpload
import grails.plugin.aws.ses.SendSesMail
import grails.plugin.aws.util.MetaClassInjector
import grails.plugin.aws.GrailsAWSCredentialsWrapper
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

    def awsConfigHash = null

	private static Logger log = Logger.getLogger(AWSCredentialsHolder.class)

    def doWithWebDescriptor = { xml ->

    }

    def doWithSpring = {

		//initializes the config hash
		awsConfigHash = ConfigurationHolder.getConfig().grails?.plugin?.aws?.hashCode()
		
		//awsTool bean
		aws(grails.plugin.aws.AwsGenericTools)
    }

    def doWithDynamicMethods = { ctx ->		
	
		injectMetaclassMethods(application)
    }

    def doWithApplicationContext = { applicationContext ->

    }

    def onChange = { event ->
	
		if (event.source) {
			
			injectMetaclassMethods(application)

		}
			
    }

    def onConfigChange = { event ->
	
		def newConfigHash = event.source.grails?.plugin?.aws?.hashCode()
		if (awsConfigHash != newConfigHash) {

			//reload credentials
			GrailsAWSCredentialsWrapper.reload()

			//store the current hash
			awsConfigHash = newConfigHash
		}
    }

	//SES handling
	def sesMail = { Closure config ->
		
		def enabled = ConfigurationHolder.config.grails.plugin.aws.ses.enabled == true
		if (enabled) {
			
			def defaultCredentials = GrailsAWSCredentialsWrapper.defaultCredentials()
			def defaultFrom = ConfigurationHolder.config.grails.plugin.aws.ses.from ?: null
			def catchall = ConfigurationHolder.config.grails.plugin.aws.ses.catchall ?: null

			def ses = new SendSesMail(defaultCredentials, defaultFrom, catchall)
			ses.send(config)
		} else {
			println "[AWS SES] E-mail sending disabled on this environment"
		}
	}
	
	def injectMetaclassMethods = { application ->
		
		//inject helper methods on classes
		def injector = new MetaClassInjector()
		injector.injectIntegerMethods()
		
		//controllers
		for (controller in application.controllerClasses) {
			controller.metaClass.sesMail = sesMail
        }

		//services
		for (service in application.serviceClasses) {
			service.metaClass.sesMail = sesMail
        }
		
		//S3 handling
		File.metaClass.s3upload = { Closure s3Config ->
			
			def defaultCredentials = GrailsAWSCredentialsWrapper.defaultCredentials()
			
			def defaultBucket = ConfigurationHolder.config.grails.plugin.aws.s3.bucket ?: null
			def defaultAcl = ConfigurationHolder.config.grails.plugin.aws.s3.acl ?: null
			def defaultRrs = ConfigurationHolder.config.grails.plugin.aws.s3.rrs ?: null
									
			def s3FileUpload = new S3FileUpload(defaultCredentials, defaultBucket, defaultAcl, defaultRrs)
			s3FileUpload.fileUpload(delegate, s3Config)
		}
		
		//S3 handling on Inputstream objects
		InputStream.metaClass.s3upload = { String name, Closure s3Config ->
			
			def defaultCredentials = GrailsAWSCredentialsWrapper.defaultCredentials()
			
			def defaultBucket = ConfigurationHolder.config.grails.plugin.aws.s3.bucket ?: null
			def defaultAcl = ConfigurationHolder.config.grails.plugin.aws.s3.acl ?: null
			def defaultRrs = ConfigurationHolder.config.grails.plugin.aws.s3.rrs ?: null
									
			def s3FileUpload = new S3FileUpload(defaultCredentials, defaultBucket, defaultAcl, defaultRrs)
			s3FileUpload.inputStreamUpload(delegate, name, s3Config)
		}
		
	}

}
