package grails.plugin.aws.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ConfigReader {

	private config

	private static Logger log = LoggerFactory.getLogger(this)

	ConfigReader(ConfigObject configObject) {
		config = configObject.toProperties()
	}

	def read(key, defaultValue = null) {
		def value = config[key] ?: defaultValue
		log.debug "returning value (${value}) for key (${key})"
		return value
	}
}
