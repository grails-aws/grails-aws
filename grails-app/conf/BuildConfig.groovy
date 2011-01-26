grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {

    inherits("global") {
    }

    log "warn" 

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
		mavenCentral()
    }

    dependencies {	
		compile 'net.java.dev.jets3t:jets3t:0.8.0'
		compile 'com.amazonaws:aws-java-sdk:1.1.4'
    }

}
