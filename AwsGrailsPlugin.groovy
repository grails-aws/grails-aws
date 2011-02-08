import grails.plugin.aws.s3.S3FileUpload
import grails.plugin.aws.ses.SendSesMail
import grails.plugin.aws.util.MetaClassInjector
import grails.plugin.aws.GrailsAWSCredentialsWrapper
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class AwsGrailsPlugin {
	
    // the plugin version
    def version = "1.1.5.1"

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.0 > *"

    // the other plugins this plugin depends on
    def dependsOn = [:]

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/**/*.gsp",
            "grails-app/controllers/**/*Controller.groovy",
            "grails-app/services/**/*Service.groovy",
            "grails-app/conf/Config.groovy",
            "grails-app/conf/DataSource.groovy",
            "grails-app/conf/UrlMappings.groovy"
    ]

    def author = "Lucas Teixeira"
    def authorEmail = "lucastex@gmail.com"
    def title = "Grails AWS Plugin"
    def description = "Amazon Web Services (AWS) grails plugin will provide easy access to simpler functions of AWS"

    // URL to the plugin's documentation
    def documentation = "http://opensource.blanq.com.br/grails-aws"

	//hash for plugin config closure, will use it to validate when config changes
	//will reload plugin config only if it changes not other changes
    def awsConfigHash = null

    def doWithWebDescriptor = { xml ->

    }

    def doWithSpring = {

		//initializes the config hash
		awsConfigHash = ConfigurationHolder.getConfig().grails?.plugin?.aws?.hashCode()
		
    }

    def doWithDynamicMethods = { ctx ->		
		
		//inject helper methods on classes
		def injector = new MetaClassInjector()
		injector.injectIntegerMethods()
			
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
			s3FileUpload.upload(delegate, s3Config)
		}
    }

    def doWithApplicationContext = { applicationContext ->

    }

    def onChange = { event ->
		
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

}
