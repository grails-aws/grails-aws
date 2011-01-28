import grails.plugin.aws.s3.S3FileUpload
import grails.plugin.aws.ses.SendSesMail
import grails.plugin.aws.util.MetaClassInjector
import grails.plugin.aws.GrailsAWSCredentialsWrapper
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class AwsGrailsPlugin {
	
    // the plugin version
    def version = "1.1.4.1"

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.0 > *"

    // the other plugins this plugin depends on
    def dependsOn = [:]

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/controller/aws/s3/S3TestController.groovy",
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

	//grailsApplication
	def grailsApplication

	//hash for plugin config closure, will use it to validate when config changes
	//will reload plugin config only if it changes not other changes
    def awsConfigHash = null

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
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
		for (controller in application.controllerClasses) {
            controller.metaClass.sesMail = { Closure config ->
                
				def defaultCredentials = GrailsAWSCredentialsWrapper.defaultCredentials()
				def defaultFrom = ConfigurationHolder.config?.grails?.plugin?.aws?.ses?.from

				def ses = new SendSesMail(defaultCredentials, defaultFrom)
				ses.send(config)
            }
        }
		
		
        //S3 handling
		File.metaClass.s3upload = { Closure s3Config ->
			
			def defaultCredentials = GrailsAWSCredentialsWrapper.defaultCredentials()
			
			def defaultBucket = ConfigurationHolder.config.grails.plugin.aws.s3.bucket
			def defaultAcl    = ConfigurationHolder.config.grails.plugin.aws.s3.acl
			def defaultRrs    = ConfigurationHolder.config.grails.plugin.aws.s3.rrs			
			
			def s3FileUpload = new S3FileUpload(defaultCredentials, defaultBucket, defaultAcl, defaultRrs)
			s3FileUpload.upload(delegate, s3Config)
		}
		
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
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
