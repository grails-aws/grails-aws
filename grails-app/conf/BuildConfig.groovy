grails.project.work.dir = 'target'

// for backwards-compatibility, the docs are checked into gh-pages branch
grails.project.docs.output.dir = 'docs/manual'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenCentral()
    }

    dependencies {

        compile 'net.java.dev.jets3t:jets3t:0.9.0'
        // jetset 0.9.0 requires httpclient 4.1.2
		
        build 'org.apache.httpcomponents:httpcore:4.2'
        build 'org.apache.httpcomponents:httpclient:4.2'
        runtime 'org.apache.httpcomponents:httpcore:4.2'
        runtime 'org.apache.httpcomponents:httpclient:4.2'


        compile 'javax.mail:mail:1.4.1'
        compile 'commons-logging:commons-logging:1.1.1'
        compile 'org.codehaus.jackson:jackson-core-asl:1.7.2'
        compile 'com.amazonaws:aws-java-sdk:1.7.5', {
            excludes 'stax-api', 'jackson-core-asl', 'httpclient', 'commons-logging'
        }
    }

    plugins {
        build ':release:2.2.1', ':rest-client-builder:1.0.3', {
            export = false
        }
    }
}
