class OpencmisGrailsPlugin {
    def version = "0.10.0-SNAPSHOT"
    def grailsVersion = "2.0.0 > *"
    def loadAfter = ['services', 'controllers']
    def title = "OpenCMIS SDK Plugin"
    def author = "Ludovik Lacroix"
    def authorEmail = "ludovik@lacroix.in"
    def description = '''\
The OpenCMIS plugin allows your grails application to use
open binding services with content repositories like Apache
InMemoryRepository and Alfresco Cloud network.
It provides wrappers services around Apache Chemistry API for Java. Using the API, developers
can build content centric solutions.
'''
    def documentation = "https://github.com/webvibes/grails-opencmis/blob/master/README.md"

    def license = "APACHE"
    def organization = [ name: "Webvibes", url: "http://www.webvibes.in" ]
    def issueManagement = [ system: "github", url: "https://github.com/webvibes/grails-opencmis/issues" ]
    def scm = [ url: "https://github.com/webvibes/grails-opencmis" ]
}
