package grails.plugin.aws.util

class MetaClassInjector {
	
	public void injectIntegerMethods() {
				
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
}