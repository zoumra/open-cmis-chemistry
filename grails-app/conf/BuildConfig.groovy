grails.project.work.dir = 'target'

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
        mavenRepo "http://repository.codehaus.org"
        // Alfresco CMIS extensions
        mavenRepo "http://maven.alfresco.com/nexus/content/groups/public"
    }

    dependencies {
        compile 'org.codehaus.groovy.modules.http-builder:http-builder:0.5.2', {
            excludes 'appengine-api-1.0-sdk', 'commons-io', 'groovy', 'log4j'
        }
        compile 'org.apache.chemistry.opencmis:chemistry-opencmis-commons-api:0.10.0', {
            excludes 'slf4j-api'
        }
        compile 'org.apache.chemistry.opencmis:chemistry-opencmis-commons-impl:0.10.0', {
            excludes 'slf4j-api'
        }
        compile 'org.apache.chemistry.opencmis:chemistry-opencmis-client-api:0.10.0', {
            excludes 'slf4j-api'
        }
        compile 'org.apache.chemistry.opencmis:chemistry-opencmis-client-impl:0.10.0', {
            excludes 'log4j', 'slf4j-api', 'slf4j-log4j12'
        }
        compile 'org.apache.chemistry.opencmis:chemistry-opencmis-client-bindings:0.10.0', {
            excludes 'log4j', 'slf4j-api', 'slf4j-log4j12'
        }
        compile 'org.apache.chemistry.opencmis:chemistry-opencmis-server-support:0.10.0', {
            excludes 'servlet-api', 'slf4j-api'
        }
        // Alfresco CMIS extension
        compile 'org.alfresco.cmis.client:alfresco-opencmis-extension:0.7'
    }

    plugins {
        build ':release:3.0.1', ':rest-client-builder:1.0.3', {
            export = false
        }
    }
}
