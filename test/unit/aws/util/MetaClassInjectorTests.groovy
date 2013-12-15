package aws.util

import grails.plugin.aws.util.ConfigReader
import grails.plugin.aws.util.MetaClassInjector
import grails.plugin.aws.meta.AwsPluginSupport
import grails.test.mixin.support.GrailsUnitTestMixin
import org.codehaus.groovy.grails.commons.GrailsApplication


import grails.test.GrailsUnitTestCase
import java.util.Date

@TestMixin(GrailsUnitTestMixin)
class MetaClassInjectorTests extends GrailsUnitTestCase {

	void testIntegerDotSecondWhenInjected() {

		MetaClassInjector.injectIntegerMethods()
		assert 1.second.class == Date
	}

	void testSesIsDisabledWhenEnabledIsTrue() {
		mockSESEnabled(true)

		def result = MetaClassInjector.sesIsDisabled()

		assert result == false
	}

	void testSesIsDisabledWhenEnabledIsFalse() {
		mockSESEnabled(false)

		def result = MetaClassInjector.sesIsDisabled()

		assert result == true
	}

	void testSesIsDisabledWhenNotConfiguredDefaultsToFalse() {
		mockSESEnabled(null)

		def result = MetaClassInjector.sesIsDisabled()

		assert result == false
	}

	def mockSESEnabled(val) {

		MetaClassInjector.application = [
		                    getConfig: {
		                        def config = new ConfigObject()
		                        config.grails.plugin.aws.ses.enabled = val
		                        return config
		                    }
		                ] as GrailsApplication
	}
}