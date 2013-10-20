class OpencmisGrailsPlugin {
    // the plugin version
    def version = "0.10.0-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.2.1 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]
    def loadAfter = ['services', 'controllers']


    // TODO Fill in these fields
    def title = "OpenCMIS SDK Plugin" // Headline display name of the plugin
    def author = "Ludovik Lacroix"
    def authorEmail = "ludovik@lacroix.in"
    def description = '''\
The OpenCMIS plugin allows your grails application to use
open binding services with content repositories like Apache 
InMemoryRepository and Alfresco server.
It provides wrappers services around Apache Chemistry API for Java. Using the API, developers
can build content centric solutions.
'''

    // URL to the plugin's documentation
    def documentation = "https://github.com/webvibes/grails-opencmis/blob/master/README.md"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "Webvibes", url: "http://www.webvibes.in" ]

    // Any additional developers beyond the author specified above.
    //def developers = [ [ name: "llax", email: "ludovik@lacroix.in" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "github", url: "https://github.com/webvibes/grails-opencmis/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/webvibes/grails-opencmis" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { ctx ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
