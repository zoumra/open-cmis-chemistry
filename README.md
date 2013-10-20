OpenCMIS for Grails
===================

Grails plugin for OpenCMIS.
Repositories Handler and Apache Chemistry binding services allow
to integrate content centric solution with Grails framework.
 
# Apache Chemistry InMemory

For development purpose, Apache Chemistry InMemory Content Repository can be used.
Add Chemistry parameters to config.groovy :

grails.plugin.opencmis.chemistry.user='username'
grails.plugin.opencmis.chemistry.password='password'
grails.plugin.opencmis.chemistry.atomurl='http://localhost:8081/inmemory/atom'
grails.plugin.opencmis.chemistry.repoid='A1'

# Alfresco Cloud

To enable Alfresco Cloud OAuth, it requires to retrieve callback, key and secret parameters from your Alfresco's developer account:
Callback Url example => http://localhost:8080/{app.name}/contentRepository/alfrescoCallback
	
For more information, visit https://developer.alfresco.com

Alfresco's network config must be set in config.groovy:

grails.plugin.opencmis.alfresco.scope='public_api'
grails.plugin.opencmis.alfresco.apiurl='https://api.alfresco.com'
grails.plugin.opencmis.alfresco.cmisurl='cmis/versions/1.0/atom'
grails.plugin.opencmis.alfresco.atomurl='https://api.alfresco.com/cmis/versions/1.0/atom'
grails.plugin.opencmis.alfresco.tokenurl='https://api.alfresco.com/auth/oauth/versions/2/token'
grails.plugin.opencmis.alfresco.authurl='https://api.alfresco.com/auth/oauth/versions/2/authorize'
grails.plugin.opencmis.alfresco.key='your_key'
grails.plugin.opencmis.alfresco.secret='your_secret'


More content repositories implementations to come. Follow the project.


