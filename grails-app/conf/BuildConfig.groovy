grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {

    inherits("global") {
    }

    log "warn" 

    repositories {	
		mavenCentral()
		grailsPlugins()
        grailsHome()
        grailsCentral()
    }

	dependencies {
        compile 'net.java.dev.jets3t:jets3t:0.8.1'
        compile 'javax.mail:mail:1.4.1'
        compile 'commons-httpclient:commons-httpclient:3.1'
        compile 'commons-logging:commons-logging:1.1.1'
		compile 'org.codehaus.jackson:jackson-core-asl:1.7.2'
		compile 'com.amazonaws:aws-java-sdk:1.1.7', {
			excludes 'stax-api', 'jackson-core-asl', 'commons-httpclient', 'commons-logging'
		}
     }
}
