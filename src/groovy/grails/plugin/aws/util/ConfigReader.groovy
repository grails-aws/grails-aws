package grails.plugin.aws.util

import org.apache.log4j.Logger

class ConfigReader {
	
	def config
	
	private static def log = Logger.getLogger(ConfigReader.class)
	
	public ConfigReader(ConfigObject configObject) {
		config = configObject.toProperties()
	}
	
	public def read(key, defaultValue = null) {
		def value = config[key] ?: defaultValue 
		log.debug "returning value (${value}) for key (${key})"
		return value 
	}
}