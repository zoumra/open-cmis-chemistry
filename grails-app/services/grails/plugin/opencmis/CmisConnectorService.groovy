package grails.plugin.opencmis

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*

import org.apache.chemistry.opencmis.client.api.Repository
import org.apache.chemistry.opencmis.client.api.Session
import org.apache.chemistry.opencmis.client.api.SessionFactory
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl
import org.apache.chemistry.opencmis.commons.SessionParameter
import org.apache.chemistry.opencmis.commons.enums.BindingType
import org.apache.chemistry.opencmis.commons.data.*

class CmisConnectorService {

    static transactional = false

    private final static def INMEMORY_REPOSITORY = 'INMEMORY'
    private final static def ALFRESCO_REPOSITORY = 'ALFRESCO'
    private final static SessionFactory sessionFactory = SessionFactoryImpl.newInstance()
    
    def grailsApplication
    def cmisRepositoryHelperService
    static Session cmisSession

    def getChemistryConfig() {
        return grailsApplication.config.grails?.plugin?.opencmis?.chemistry
    }

    def getAlfrescoConfig() {
        return grailsApplication.config.grails?.plugin?.opencmis?.alfresco 
    }

    def connect(String accessToken) {
        if(cmisSession == null) {
            if(cmisRepositoryHelperService.getRepository() == INMEMORY_REPOSITORY) {
                cmisSession = getChemistrySession(chemistryConfig)
            } else if(cmisRepositoryHelperService.getRepository() == ALFRESCO_REPOSITORY) {
                cmisSession = getAlfrescoSession(alfrescoConfig,accessToken)
            } else {
                log.error "no repository defined"
            }
        } else {
            getCurrentSession()
        }
    }

    def disconnect(Session cmisSession) {
        this.cmisSession = null
    }

    public static Session getCurrentSession() {
       return this.cmisSession
    }

    public static callAlfrescoApi(String apiPath, String accessToken, Closure onSuccess, Closure onFailure, Closure onException) {

        def url = "https://api.alfresco.com/" + (apiPath ?: "")
        def http = new HTTPBuilder(url)
        http.setHeaders( [ "Authorization" : "Bearer " + accessToken ]) // Header based token passing (preferred)

        try {
            http.request(Method.GET, ContentType.JSON) {
                response.success = { response, json ->
                    onSuccess(response, json)
                }
                response.failure = { response, reader ->
                    onFailure(response, reader)
                }
            }
        }
        catch(Exception e) {
            onException(e)
        }
    }

    public static Session getChemistrySession(Map chemistryConfig) {

        if (cmisSession == null) {
            def chemistryParams = [(SessionParameter.USER):chemistryConfig.user ?: 'username',
                                   (SessionParameter.PASSWORD):chemistryConfig.password ?: 'password',
                                   (SessionParameter.ATOMPUB_URL):chemistryConfig.atomurl ?: 'http://localhost:8081/inmemory/atom',
                                   (SessionParameter.REPOSITORY_ID):chemistryConfig.repoid ?: 'A1',
                                   (SessionParameter.BINDING_TYPE):BindingType.ATOMPUB.value()]

            // Session factory
            cmisSession = sessionFactory.createSession(chemistryParams)
        }
        cmisSession
    }

    public static Session getAlfrescoSession(Map alfrescoConfig, String accessToken) {
        if (cmisSession == null) {
            def alfrescoParams = [(SessionParameter.ATOMPUB_URL) : alfrescoConfig.atomurl,
                                  (SessionParameter.BINDING_TYPE) : BindingType.ATOMPUB.value(),
                                  (SessionParameter.AUTH_HTTP_BASIC) : "false",
                                  (SessionParameter.HEADER+".0") : "Authorization: Bearer " + accessToken,
                                  (SessionParameter.OBJECT_FACTORY_CLASS) : "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl"]

            List<Repository> repositories = sessionFactory.getRepositories(alfrescoParams)
            cmisSession = repositories[0].createSession()
        }
        cmisSession
    }

}
