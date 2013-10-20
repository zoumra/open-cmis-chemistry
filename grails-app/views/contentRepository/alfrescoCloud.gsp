<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>Network Information - ${params.id}</title>
</head>
<body>
  <g:if test="${authenticated}">
    <p>
      <g:if test="${success}">
        <br/>&nbsp;Information for Alfresco Cloud network ${params.id}:
        <ul>
          <li>Repository name: ${repoName}</li>
          <li>Root Folder's name: ${rootFolder}</li>
        </ul>
        <br/>&nbsp;Sites:
        <ul>
          <li>// Not yet implemented</li>
        </ul>
      </g:if>
      <g:else>
        <br/>&nbsp;Error retrieving datas from Alfresco repository..
      </g:else>
    </p>
    <p>
      <br/>&nbsp;Click <g:link controller="contentRepository" action="resetSession">here</g:link> if you'd like to invalidate your access token and start again.
    </p>
  </g:if>
  <g:else>
    <p>
      <br/>&nbsp;Your login has expired, please <g:link controller="contentRepository" action="alfrescoConnect">return to the start</g:link> to re-authenticate.
    </p>
  </g:else>
</body>
</html>