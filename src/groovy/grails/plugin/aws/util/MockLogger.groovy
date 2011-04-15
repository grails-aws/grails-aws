package grails.plugin.aws.util

class MockLogger {
	
	def out
	
	public MockLogger(_out = System.out) {
		out = _out
	}

	void info(message) {
		out.println "[info] ${message}"
	}
	
	void warn(message) {
		out.println "[warn] ${message}"	
	}
	
	void debug(message) {
		out.println "[debug] ${message}"	
	}
	
	void error(message) {
		out.println "[error] ${message}"	
	}

}