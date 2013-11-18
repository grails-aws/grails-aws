import grails.plugin.aws.meta.AwsPluginSupport

class AwsGrailsPlugin {

    def version                  = "1.2.12.3"
    def grailsVersion            = "1.3.0 > *"
    def loadAfter                = ['services', 'controllers']
    def observe                  = ['services', 'controllers']
    def pluginExcludes           = [
        "grails-app/views/**",
        "grails-app/controllers/**/*Controller.groovy",
        "grails-app/services/**/*Service.groovy",
		  "src/docs/**",
        "testScripts/*.groovy"
    ]

    def title                    = "Grails AWS Plugin"
    def description              = "Amazon Web Services (AWS) grails plugin provides easy access to simpler functions of AWS"
    def documentation            = "http://blanq.github.com/grails-aws"

    def license                  = "APACHE"
    def organization             = [  name: "Blanq", url: "http://github.com/blanq" ]
    def developers               = [[ name: "Lucas Teixeira", email: "lucastex@gmail.com" ]]
    def scm                      = [  url: "https://github.com/blanq/grails-aws" ]
    def issueManagement          = [  system: "JIRA", url: "http://jira.grails.org/browse/GPAWS" ]

    def onChange                 = AwsPluginSupport.onChange
    def doWithSpring             = AwsPluginSupport.doWithSpring
    def onConfigChange           = AwsPluginSupport.onConfigChange
    def doWithDynamicMethods     = AwsPluginSupport.doWithDynamicMethods
}
