package grails.plugin.aws.util

import grails.plugin.aws.meta.AwsPluginSupport

class MetaClassInjector {
	
	public static void injectIntegerMethods() {
				
		Integer.metaClass.propertyMissing = { name ->			
			
			if (name == "second" || name == "seconds") {
				return DateRangeCreator.fromSeconds(delegate)
			} else if (name == "minute" || name == "minutes") {
				return DateRangeCreator.fromMinutes(delegate)
			} else if (name == "hour" || name == "hours") {
				return DateRangeCreator.fromHours(delegate)
			} else if (name == "day" || name == "days") {
				return DateRangeCreator.fromDays(delegate)
			} else if (name == "month" || name == "months") {
				return DateRangeCreator.fromMonths(delegate)
			} else if (name == "year" || name == "years") {
				return DateRangeCreator.fromYears(delegate)
			} else {
				throw new MissingPropertyException(delegate)
			}
		}
	}
	
	public static void injectS3UploadMethods(applicationContext) {
		
		File.metaClass.s3upload = { Closure s3Config ->
			def s3FileUploadBean = applicationContext.getBean('s3FileUpload')
			s3FileUploadBean.fileUpload(delegate, s3Config)
		}
		
		InputStream.metaClass.s3upload = { String name, Closure s3Config ->
			def s3FileUploadBean = applicationContext.getBean('s3FileUpload')
			s3FileUploadBean.inputStreamUpload(delegate, name, s3Config)
		}
	}
	
	public static void injectSesMethods(grailsApplication, applicationContext) {
		
		def targetClasses = []
		targetClasses.addAll(grailsApplication.controllerClasses)
		targetClasses.addAll(grailsApplication.serviceClasses)
		
		targetClasses.each { clazz ->
			
			clazz.metaClass.sesMail = { Closure sendConfigClosure ->
				
				def enabled = Boolean.valueOf(AwsPluginSupport.configurationReader.read("grails.plugin.aws.ses.enabled", "true"))
				if (!enabled) {
					log.info "[AWS SES] Aborting attemp to send e-mail. E-mail sending disabled on this environment"
					return
				}
				
				def sesBean = applicationContext.getBean('sendSesMail')
				sesBean.send(sendConfigClosure)
			}
		}
	}
	
}