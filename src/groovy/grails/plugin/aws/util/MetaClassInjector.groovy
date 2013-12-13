package grails.plugin.aws.util

import grails.plugin.aws.meta.AwsPluginSupport

class MetaClassInjector {

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

	static Boolean sesIsEnabled() {
		Boolean.valueOf(AwsPluginSupport.configurationReader.read("grails.plugin.aws.ses.enabled", "true"))
	}
	static void injectSesMethods(grailsApplication, applicationContext) {

		def targetClasses = []
		targetClasses.addAll(grailsApplication.controllerClasses)
		targetClasses.addAll(grailsApplication.serviceClasses)

		targetClasses.each { clazz ->

			clazz.metaClass.sesMail = { Closure sendConfigClosure ->

				def enabled = sesIsEnabled()
				if (!enabled) {
					log.info "[AWS SES] Aborting attemp to send e-mail. E-mail sending disabled on this environment"
					return
				}

				applicationContext.sendSesMail.send(sendConfigClosure)
			}
		}
	}
}
