package grails.plugin.aws.util

import java.lang.Boolean

import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.plugin.aws.meta.AwsPluginSupport

class MetaClassInjector {

	/** Set at startup. */
	static GrailsApplication application
	
	static void injectIntegerMethods() {

		Integer.metaClass.propertyMissing = { name ->

			if (name == "second" || name == "seconds") {
				return DateRangeCreator.fromSeconds(delegate)
			}
			if (name == "minute" || name == "minutes") {
				return DateRangeCreator.fromMinutes(delegate)
			}
			if (name == "hour" || name == "hours") {
				return DateRangeCreator.fromHours(delegate)
			}
			if (name == "day" || name == "days") {
				return DateRangeCreator.fromDays(delegate)
			}
			if (name == "month" || name == "months") {
				return DateRangeCreator.fromMonths(delegate)
			}
			if (name == "year" || name == "years") {
				return DateRangeCreator.fromYears(delegate)
			}
			throw new MissingPropertyException(delegate)
		}
	}

	static void injectS3UploadMethods(applicationContext) {

		File.metaClass.s3upload = { Closure s3Config ->
			applicationContext.s3FileUpload.fileUpload(delegate, s3Config)
		}

		InputStream.metaClass.s3upload = { String name, Closure s3Config ->
			applicationContext.s3FileUpload.inputStreamUpload(delegate, name, s3Config)
		}
	}

	static sesIsDisabled() {
		def enabled = application.config.grails.plugin.aws.ses.enabled
		if (enabled instanceof Boolean) {
			return !enabled
		} else {
			return false
		}
	}

	static void injectSesMethods(grailsApplication, applicationContext) {

		def targetClasses = []
		targetClasses.addAll(grailsApplication.controllerClasses)
		targetClasses.addAll(grailsApplication.serviceClasses)

		targetClasses.each { clazz ->

			clazz.metaClass.sesMail = { Closure sendConfigClosure ->

				if (sesIsDisabled()) {
					log.info "[AWS SES] Aborting attempt to send e-mail. E-mail sending disabled on this environment"
					return
				}

				applicationContext.sendSesMail.send(sendConfigClosure)
			}
		}
	}
}
