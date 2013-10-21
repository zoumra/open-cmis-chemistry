package grails.plugin.opencmis

import org.springframework.beans.factory.InitializingBean
import org.codehaus.groovy.grails.web.util.WebUtils

import org.apache.chemistry.opencmis.commons.*
import org.apache.chemistry.opencmis.commons.data.*
import org.apache.chemistry.opencmis.commons.enums.*
import org.apache.chemistry.opencmis.client.api.*

class CmisRepositoryHelperService implements InitializingBean {

    static transactional = false

    RepositoryInfo repoInfo
    RepositoryCapabilities repoCaps

    def grailsApplication
	def repoRootFolder
    def repository
    def cmisSession

    public void afterPropertiesSet() {
        def repositoryConf = grailsApplication.config.grails?.plugin?.opencmis?.repository
        if(repositoryConf){
            this.repository = repositoryConf
        } else {
            log.error "No repository defined."
        }
        
    }

    def getCmisSession() {
        this.cmisSession = WebUtils.retrieveGrailsWebRequest().getSession().cmis
        return cmisSession
    }

    def getRepository() {
        return grailsApplication.config.grails?.plugin?.opencmis?.repository
    }

	// Repository Info and capabilities
    def getRepositoryInfo() {
        if(this.getCmisSession()) {
            this.repoInfo = cmisSession.getRepositoryInfo()
        } else {
            log.error "Open a connection first."
        }
        return repoInfo
    }

    def getRepositoryCaps() {
        if(this.getCmisSession()) {
            this.repoCaps =  cmisSession.getRepositoryInfo().getCapabilities();
        } else {
            log.error "Open a connection first."
        }
        return repoCaps
    }

    def getRootFolder() {
        if(this.getCmisSession()) {
            this.repoRootFolder = cmisSession.getRootFolder()
        } else {
            log.error "Open a connection first."
        }
        return repoRootFolder
    }

    def showRootDescendants() {
        String desc = ""
        if(this.getCmisSession()) {
            this.repoCaps =  cmisSession.getRepositoryInfo().getCapabilities();
            this.repoRootFolder = cmisSession.getRootFolder()
                if (!this.repoCaps.isGetDescendantsSupported()) {
                    log.info "Warning: getDescendants not supported in this repository"
                } else {
                log.info "getDescendants is supported on this repository."
                desc = this.repoRootFolder.getName() + " : "
                    for (t in this.repoRootFolder.getDescendants(-1)) {
                    printTree(t , "");
                    }
                }
        } else {
            log.error "Open a connection first."
        }
        return desc
    }

    private static String printTree(Tree<FileableCmisObject> tree, String tab) {
    def s = tab + "Descendant "+ tree.getItem().getName()
        for (t in tree.getChildren()) {
            printTree(t, tab + "  ");
        }
    }

}
