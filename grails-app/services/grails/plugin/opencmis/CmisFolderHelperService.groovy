package grails.plugin.opencmis

import org.codehaus.groovy.grails.web.util.WebUtils

import org.apache.chemistry.opencmis.commons.*
import org.apache.chemistry.opencmis.commons.data.*
import org.apache.chemistry.opencmis.commons.enums.*
import org.apache.chemistry.opencmis.client.api.*

class CmisFolderHelperService {

	def grailsApplication
	def cmisConnectorService
	def cmisRepositoryHelperService
	def cmisSession


    def getCurrentSession() {
        if (!cmisSession) {
             this.cmisSession = WebUtils.retrieveGrailsWebRequest().getSession().cmis
        }
        return cmisSession
    }


    def createFolder(String folderName) {

    	def rootfolder = cmisRepositoryHelperService.getRootFolder()	
    	if (Action.CAN_CREATE_FOLDER in rootFolder.allowableActions.allowableActions) {
    		def props = ['cmis:objectTypeId': 'cmis:folder',
                 		'cmis:name' : folderName,'cmis:created_by':'opencmis'] // change to user name

    	def sf = rootFolder.createFolder(props)
    	log.info("Folder created!" + "id:" + sf.id + sf.name)
    	}
    }
}
