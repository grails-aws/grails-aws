package aws.util

import grails.plugin.aws.util.ConfigReader
import grails.plugin.aws.util.MetaClassInjector
import grails.plugin.aws.meta.AwsPluginSupport

import grails.test.GrailsUnitTestCase
import java.util.Date

class MetaClassInjectorTests extends GrailsUnitTestCase {

	void testIntegerDotSecondWhenInjected() {

		MetaClassInjector.injectIntegerMethods()
		assert 1.second.class == Date
	}

	void testSesIsEnabledWhenConfiguredAsTrue() {
		mockConfigurationReader("grails.plugin.aws.ses.enabled = true")

		def result = MetaClassInjector.sesIsEnabled()

		assert result == true
	}

	void testSesIsEnabledWhenConfiguredAsFalse() {
		mockConfigurationReader("grails.plugin.aws.ses.enabled = false")

		def result = MetaClassInjector.sesIsEnabled()

		assert result == false
	}

	void testSesIsEnabledWhenMissingConfigDefaultsToTrue() {
		mockConfigurationReader("")

		def result = MetaClassInjector.sesIsEnabled()

		assert result == true
	}

	def mockConfigurationReader(String config) {
		def tmpFile = File.createTempFile("aws-plugin", "${System.currentTimeMillis()}")
		tmpFile.deleteOnExit()
		tmpFile << config
		AwsPluginSupport.configurationReader = new ConfigReader(new ConfigSlurper().parse(tmpFile.toURI().toURL()))
	}
}