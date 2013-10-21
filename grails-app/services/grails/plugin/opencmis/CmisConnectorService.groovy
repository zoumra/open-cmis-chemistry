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

    def grailsApplication

    private final static SessionFactory chemistrySessionFactory = SessionFactoryImpl.newInstance()
    private final static SessionFactory alfrescoSessionFactory = SessionFactoryImpl.newInstance()
    private static Session cmisSessionChemistry
    private static Session cmisSessionAlfresco

    def getChemistryConfig() {
        return grailsApplication.config.grails?.plugin?.opencmis?.chemistry
    }

    def getAlfrescoConfig() {
        return grailsApplication.config.grails?.plugin?.opencmis?.alfresco 
    }

    def connect(String repository,String accessToken) {
        if(repository == 'chemistry') {
            if(cmisSessionChemistry == null){ 
                cmisSessionChemistry = getChemistrySession(chemistryConfig)
            } else {
                getCurrentChemistrySession()
            }
        } else if(repository == 'alfresco') {
            if(cmisSessionAlfresco == null) {
                this.cmisSessionAlfresco = getAlfrescoSession(alfrescoConfig,accessToken)
            } else {
                getCurrentAlfrescoSession()                
            }
        } else {
            log.error "no repository defined"
        }
    }

    public static Session getCurrentChemistrySession() {
       return cmisSessionChemistry;
    }

     public static Session getCurrentAlfrescoSession() {
       return cmisSessionAlfresco;
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

        if (cmisSessionChemistry == null) {
            def chemistryParams = [(SessionParameter.USER):chemistryConfig.user ?: 'username',
                                   (SessionParameter.PASSWORD):chemistryConfig.password ?: 'password',
                                   (SessionParameter.ATOMPUB_URL):chemistryConfig.atomurl ?: 'http://localhost:8081/inmemory/atom',
                                   (SessionParameter.REPOSITORY_ID):chemistryConfig.repoid ?: 'A1',
                                   (SessionParameter.BINDING_TYPE):BindingType.ATOMPUB.value()]

            // Session factory
            cmisSessionChemistry = chemistrySessionFactory.createSession(chemistryParams)
        }
        cmisSessionChemistry
    }

    public static Session getAlfrescoSession(Map alfrescoConfig, String accessToken) {
        if (cmisSessionAlfresco == null) {
            def alfrescoParams = [(SessionParameter.ATOMPUB_URL) : alfrescoConfig.atomurl,
                                  (SessionParameter.BINDING_TYPE) : BindingType.ATOMPUB.value(),
                                  (SessionParameter.AUTH_HTTP_BASIC) : "false",
                                  (SessionParameter.HEADER+".0") : "Authorization: Bearer " + accessToken,
                                  (SessionParameter.OBJECT_FACTORY_CLASS) : "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl"]

            List<Repository> repositories = alfrescoSessionFactory.getRepositories(alfrescoParams)
            cmisSessionAlfresco = repositories[0].createSession()
        }
        cmisSessionAlfresco
    }

    // Repository Info and capabilities
    def getRepositoryInfo(Session cmisSession) {
        RepositoryInfo repoInfo = cmisSession.getRepositoryInfo();
        return repoInfo
    }

    def getRepositoryCaps(Session cmisSession) {
        RepositoryCapabilities repoCaps =  cmisSession.getRepositoryInfo().getCapabilities();
        return repoCaps
    }

    def getRootFolder(Session cmisSession) {
        def repoRootFolder = cmisSession.getRootFolder()
        return repoRootFolder
    }
}
