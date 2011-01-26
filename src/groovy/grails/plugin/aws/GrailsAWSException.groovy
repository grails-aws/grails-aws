package grails.plugin.aws

class GrailsAWSException extends Exception {
	
	public GrailsAWSException() {
		super()
	}
	
	public GrailsAWSException(String message) {
		super(message)
	}
}