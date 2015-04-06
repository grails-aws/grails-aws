import grails.plugin.aws.meta.AwsPluginSupport

class AwsGrailsPlugin {

    def version                  = "1.9.13.2"
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
    def documentation            = "https://grails-aws.github.io/grails-aws/$version/"

    def license                  = "APACHE"
    def organization             = [  name: "Grails AWS", url: "https://github.com/grails-aws" ]
    def developers               = [[ name: "Lucas Teixeira", email: "lucastex@gmail.com" ],
                                    [ name: "Jay Prall", email: "jay@prall.net" ]]
    def scm                      = [  url: "https://github.com/grails-aws/grails-aws" ]
    def issueManagement          = [  system: "GITHUB", url: "https://github.com/grails-aws/grails-aws/issues" ]

    def onChange                 = AwsPluginSupport.onChange
    def doWithSpring             = AwsPluginSupport.doWithSpring
    def onConfigChange           = AwsPluginSupport.onConfigChange
    def doWithDynamicMethods     = AwsPluginSupport.doWithDynamicMethods
}
