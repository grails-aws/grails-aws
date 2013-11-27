package grails.plugin.aws.util

import static java.util.Calendar.*

import java.util.Calendar as CAL

class DateRangeCreator {

	static Date fromSeconds(quantity) {
		def c = CAL.instance
		c.add SECOND, quantity
		c.time
	}

	static Date fromMinutes(quantity) {
		def c = CAL.instance
		c.add MINUTE, quantity
		c.time
	}

	static Date fromHours(quantity) {
		def c = CAL.instance
		c.add HOUR, quantity
		c.time
	}

	static Date fromDays(quantity) {
		def c = CAL.instance
		c.add DAY_OF_MONTH, quantity
		c.time
	}

	static Date fromMonths(quantity) {
		def c = CAL.instance
		c.add MONTH, quantity
		c.time
	}

	static Date fromYears(quantity) {
		def c = CAL.instance
		c.add YEAR, quantity
		c.time
	}
}
