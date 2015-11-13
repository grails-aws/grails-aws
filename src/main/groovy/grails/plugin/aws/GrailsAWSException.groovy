package grails.plugin.aws

class GrailsAWSException extends Exception {

	GrailsAWSException() {
		super()
	}

	GrailsAWSException(String message) {
		super(message)
	}
}
