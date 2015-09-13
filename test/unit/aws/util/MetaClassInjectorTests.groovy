package aws.util

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

import grails.plugin.aws.util.ConfigReader
import grails.plugin.aws.util.MetaClassInjector
import grails.plugin.aws.meta.AwsPluginSupport
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.junit.Test

import java.util.Date

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

      assert result == false
   }

   @Test
   void sesIsDisabledWhenEnabledIsFalse() {
      mockSESEnabled(false)

      def result = MetaClassInjector.sesIsDisabled()

      assert result == true
   }

   @Test
   void sesIsDisabledWhenNotConfiguredDefaultsToFalse() {
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