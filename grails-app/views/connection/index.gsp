<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>OpenCMIS: repositories available</title>
</head>
<body>
<g:if test="${rep == 'INMEMORY'}">
  <h5><g:link action="chemistryInMemory">Connect</g:link> to Apache Chemistry inMemory server</h5>
</g:if>
<g:elseif test="${rep == 'ALFRESCO'}">
  <h5><g:link action="alfrescoConnect">Connect</g:link> to Alfresco Cloud Network</h5>
</g:elseif>
<g:else>
	<h5> No repository defined, check configurattion examples </h5>
</g:else>
</body>
</html>