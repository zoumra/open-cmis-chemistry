package grails.plugin.opencmis


import grails.converters.JSON
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

import org.apache.chemistry.opencmis.client.api.Repository
import org.apache.chemistry.opencmis.client.api.Session
import org.apache.chemistry.opencmis.client.api.SessionFactory
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl
import org.apache.chemistry.opencmis.commons.SessionParameter
import org.apache.chemistry.opencmis.commons.enums.BindingType


class RepositoryHandlerService {

    static transactional = false

    def grailsApplication
    def sessionHolderService

    private final SessionFactory sessionFactory = SessionFactoryImpl.newInstance()
    private Session cmisSession;

    private def getChemistryConfig() {
        grailsApplication.config.grails?.plugin?.opencmis?.chemistry
    }

    private def getAlfrescoConfig() {
        grailsApplication.config.grails?.plugin?.opencmis?.alfresco
    }

    //TODO: JCR config

     def callAlfrescoApi(String apiPath, String accessToken, Closure onSuccess, Closure onFailure, Closure onException) {
       
        def url = "https://api.alfresco.com/" + (apiPath == null ? "" : apiPath)
        def http = new HTTPBuilder(url)
        http.setHeaders( [ "Authorization" : "Bearer " + accessToken ]) // Header based token passing (preferred)
  
        log.debug "About to GET ${url} with access token ${accessToken}"
  
        try {
          http.request(Method.GET, ContentType.JSON) {
          // uri.query = [ access_token : accessToken ] // Query string based token passing (NOT preferred)
            response.success = { response, json ->
              log.debug "####SUCCESS:\n" + response.toString() + "\n\tBody:\n" + String.valueOf(json)
              onSuccess(response, json)
            }
            response.failure = { response, reader ->
              log.debug "####FAILURE:\n" + response.toString() //+ "\n\tBody:\n" + reader.text
              onFailure(response, reader)
            }
          }
        }
        catch(Exception e) {
          onException(e)
        }
  
      }

    def getChemistrySession(Map chemistryConfig) {
        
        if (cmisSession == null) {
    	def chemistryParams = [(SessionParameter.USER):config.user ?: 'username',
		                      (SessionParameter.PASSWORD):config.password ?: 'password',
		                      (SessionParameter.ATOMPUB_URL):config.atomurl ?: 'http://localhost:8081/inmemory/atom',
		                      (SessionParameter.REPOSITORY_ID):config.repoid ?: 'A1',
		                      (SessionParameter.BINDING_TYPE):BindingType.ATOMPUB.value()]

		  // Session factory
     	  cmisSession = sessionFactory.createSession(chemistryParams)
        }
        cmisSession
	}

    def getAlfrescoSession(Map alfrescoConfig, String accessToken) {
         if (cmisSession == null) {
            def alfrescoParams = [(SessionParameter.ATOMPUB_URL) : alfrescoConfig.atomurl,
                         (SessionParameter.BINDING_TYPE) : BindingType.ATOMPUB.value(),
                         (SessionParameter.AUTH_HTTP_BASIC) : "false",
                         (SessionParameter.HEADER+".0") : "Authorization: Bearer " + accessToken,
                         (SessionParameter.OBJECT_FACTORY_CLASS) : "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl"]
        
            List<Repository> repositories = sessionFactory.getRepositories(alfrescoParams);
            cmisSession = repositories.get(0).createSession();
        }
        cmisSession
    }

	def connect(String repository,String accessToken) {
	
        if(repository == 'chemistry') {
            cmisSession = getChemistrySession(chemistryConfig)
        } else if(repository == 'alfresco'){
            cmisSession = getAlfrescoSession(alfrescoConfig,accessToken)
        } else {
            log.error 'no repository defined'
            cmisSession = null;
        }
       
        //sessionHolderService.instance.setSession(sessionName, session)
        cmisSession

	}

}
