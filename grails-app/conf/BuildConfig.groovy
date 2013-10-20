grails.project.work.dir = 'target'

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
        mavenRepo "http://repository.codehaus.org"
        // Alfresco CMIS extensions
        mavenRepo "http://maven.alfresco.com/nexus/content/groups/public" 
    }
    dependencies {
        compile 'org.codehaus.groovy.modules.http-builder:http-builder:0.5.2'
        compile 'org.apache.chemistry.opencmis:chemistry-opencmis-commons-api:0.10.0'
        compile 'org.apache.chemistry.opencmis:chemistry-opencmis-commons-impl:0.10.0'
        compile 'org.apache.chemistry.opencmis:chemistry-opencmis-client-api:0.10.0'
        compile 'org.apache.chemistry.opencmis:chemistry-opencmis-client-impl:0.10.0'
        compile 'org.apache.chemistry.opencmis:chemistry-opencmis-client-bindings:0.10.0'
        compile 'org.apache.chemistry.opencmis:chemistry-opencmis-server-support:0.10.0'
        // Alfresco CMIS extension
        compile 'org.alfresco.cmis.client:alfresco-opencmis-extension:0.7'



    }

    plugins {
        build(":release:3.0.1",
              ":rest-client-builder:1.0.3") {
            export = false
        }
    }
}
