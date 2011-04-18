import grails.plugin.aws.meta.AwsPluginSupport

class AwsGrailsPlugin {

    def version                  = "1.1.9.1"
    def grailsVersion            = "1.3.0 > *"
    def dependsOn                = [:]
    def loadAfter                = ['services', 'controllers']
    def observe                  = ['services', 'controllers']
    def watchedResources         = ["grails-app/services/**/*Service.groovy", "grails-app/controllers/**/*Controller.groovy"]
    def pluginExcludes           = [
            "grails-app/views/**/*.gsp",
            "grails-app/controllers/**/*Controller.groovy",
            "grails-app/services/**/*Service.groovy",
            "grails-app/conf/Config.groovy",
            "grails-app/conf/DataSource.groovy",
            "grails-app/conf/UrlMappings.groovy",
            "testScripts/*.groovy"
    ]

    def title                    = "Grails AWS Plugin"
    def description              = "Amazon Web Services (AWS) grails plugin will provide easy access to simpler functions of AWS"
    def documentation            = "http://blanq.github.com/grails-aws"
	def author                   = "Lucas Teixeira"
    def authorEmail              = "lucastex@gmail.com"    

	def license                  = "APACHE"
	def organization             = [  name: "Blanq", url: "http://github.com/blanq" ]
	def developers               = [[ name: "Lucas Teixeira", email: "lucastex@gmail.com" ]]
	def scm                      = [  url: "https://github.com/blanq/grails-aws" ]
	def issueManagement          = [  system: "JIRA", url: "http://jira.grails.org/browse/GPAWS" ]
	
    def onChange                 = AwsPluginSupport.onChange
	def doWithSpring             = AwsPluginSupport.doWithSpring
	def onConfigChange           = AwsPluginSupport.onConfigChange
	def doWithWebDescriptor      = AwsPluginSupport.doWithWebDescriptor
    def doWithDynamicMethods     = AwsPluginSupport.doWithDynamicMethods
	def doWithApplicationContext = AwsPluginSupport.doWithApplicationContext
}
