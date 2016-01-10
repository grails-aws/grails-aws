package aws.util

import grails.core.GrailsApplication
import grails.plugin.aws.util.MetaClassInjector
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.grails.config.NavigableMapConfig
import org.grails.config.PropertySourcesConfig
import org.junit.Test

@TestMixin(GrailsUnitTestMixin)
class MetaClassInjectorTests {

	@Test
	void integerDotSecondWhenInjected() {

		MetaClassInjector.injectIntegerMethods()
		assert 1.second.class == Date
	}

	@Test
	void sesIsDisabledWhenEnabledIsTrue() {
		mockSESEnabled(true)

		def result = MetaClassInjector.sesIsDisabled()

		assert !result
	}

	@Test
	void sesIsDisabledWhenEnabledIsFalse() {
		mockSESEnabled(false)

		def result = MetaClassInjector.sesIsDisabled()

		assert result
	}

	@Test
	void sesIsDisabledWhenNotConfiguredDefaultsToFalse() {
		mockSESEnabled(null)

		def result = MetaClassInjector.sesIsDisabled()

		assert !result
	}

	static def mockSESEnabled(val) {

		MetaClassInjector.application = [
				getConfig: {
					def config = new PropertySourcesConfig()
					config.grails.plugin.aws.ses.enabled = val
					return config
				}
		] as GrailsApplication
	}
}