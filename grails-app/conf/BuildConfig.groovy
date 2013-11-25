grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        compile 'net.java.dev.jets3t:jets3t:0.9.0'
        compile 'javax.mail:mail:1.4.1'
        compile 'commons-httpclient:commons-httpclient:3.1'
        compile 'commons-logging:commons-logging:1.1.1'
        compile 'org.codehaus.jackson:jackson-core-asl:1.7.2'
        compile 'com.amazonaws:aws-java-sdk:1.2.12', {
            excludes 'stax-api', 'jackson-core-asl', 'commons-httpclient', 'commons-logging'
        }
    }

    plugins {
        build ':release:2.2.1', ':rest-client-builder:1.0.3', {
            export = false
        }
    }
}
