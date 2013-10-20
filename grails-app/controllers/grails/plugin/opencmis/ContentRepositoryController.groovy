package grails.plugin.opencmis

import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.apache.http.*
import org.apache.http.client.*
import org.apache.http.protocol.*
import org.apache.http.params.*
import org.apache.http.client.params.*
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

class ContentRepositoryController {

    LinkGenerator grailsLinkGenerator

    def repositoryHandlerService
    def grailsApplication
    
    def index() {

    }

    def alfrescoConnect() {
       if (session["accessToken"]) {
           def accessToken = session["accessToken"]
           def http = new HTTPBuilder("https://api.alfresco.com/")
           http.setHeaders( [ "Authorization" : "Bearer " + accessToken ]) // Header based token passing (preferred)

           http.request(GET, JSON) {
              response.success = { response, json ->
                log.debug "####SUCCESS:\n" + response.toString() + "\n\tBody:\n" + String.valueOf(json)
                [ authenticated : true,
                  success : true,
                  networks : json ]
              }
              response.failure = { response, reader ->
                log.debug "####FAILURE:\n" + response.toString() + "\n\tBody:\n" + reader.text
                [ authenticated : true,
                  success : false,
                  errorMessage : "HTTP Status Code ${response.getStatus()} returned by Alfresco" ]
              }
           }
       }
       else {
           [
               authenticated : false,
               alfrescoOAuthAuthorizeURI : "https://api.alfresco.com/auth/oauth/versions/2/authorize",
               clientId : grailsApplication.config.grails?.plugin?.opencmis?.alfresco?.key,
               scope : grailsApplication.config.grails?.plugin?.opencmis?.alfresco?.scope,
               responseType : "code",
               callbackURL : grailsLinkGenerator.link(action : "alfrescoCallback", absolute : true)
           ]
       }
    }

    def alfrescoCallback() {
        def authCode = params.code

        if (!authCode) {
            throw new Exception("authCode not provided to callback")
        }
        else {
            // Exchange the auth code for an access token (we have a short period of time to do this)
            def apiKey = grailsApplication.config.grails?.plugin?.opencmis?.alfresco?.key
            def secret = grailsApplication.config.grails?.plugin?.opencmis?.alfresco?.secret
            def http = new HTTPBuilder("https://api.alfresco.com/")

            log.debug "About to POST https://api.alfresco.com/auth/oauth/versions/2/token\n" +
                      "\tcode : " + authCode + "\n" +
                      "\tclient_id : " + apiKey + "\n" +
                      "\tclient_secret : " + secret + "\n" +
                      "\tredirect_uri : " + grailsLinkGenerator.link(action : "alfrescoCallback", absolute : true) + "\n" +
                      "\tgrant_type : authorization_code"

            http.request(POST, JSON) {
              uri.path = "/auth/oauth/versions/2/token"
              requestContentType = ContentType.URLENC
              body = [ code : authCode,
                       client_id : apiKey,
                       client_secret : secret,
                       redirect_uri : grailsLinkGenerator.link(action : "alfrescoCallback", absolute : true),
                       grant_type : "authorization_code" ]
              response.success = { response, json ->
                session["accessToken"] = json["access_token"] // Note: shoving the token in the session is fine for a demo app, but _NOT_ ok in a real app!
                session["refreshToken"] = json["refresh_token"]
                log.debug "####SUCCESS:\n" + response.toString() + "\n" + String.valueOf(json)
              }
              response.failure = { response -> log.debug "####FAILURE:\n" + response.toString() }
            }
        }
    }

    def alfrescoCloud() {
      def networkId = params.id
      def accessToken = session["accessToken"]

      if (accessToken)
      {
         def session = repositoryHandlerService.connect('alfresco',accessToken)
         def name = session.getRepositoryInfo().getName()
         def rf = session.getRootFolder().getName()
         
        if(name && rf) {
          [ authenticated : true, success : true, repoName : name , rootFolder: rf]
        }
      

      }
      else
      {
        resetSession()
      }
    }

    def resetSession() {
      session.invalidate()
      redirect(action: "index")
    }

}