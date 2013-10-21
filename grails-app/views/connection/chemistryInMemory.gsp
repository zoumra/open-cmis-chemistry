<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>Apache Chemistry inMemory</title>
</head>
<body>
  <g:if test="${authenticated}">
    <p>
      <g:if test="${success}">
        <br/>&nbsp;Information for inMemory Local Server:
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
        <br/>&nbsp;Error retrieving datas from inMemory.
      </g:else>
    </p>
    <p>
      <br/>&nbsp;Click <g:link controller="connection" action="resetSession">here</g:link> if you'd like to invalidate your session and start again.
    </p>
  </g:if>
  <g:else>
    <p>
      <br/>&nbsp;Your login has expired, please <g:link controller="connection" action="chemistryInMemory">return to the start</g:link> to re-authenticate.
    </p>
  </g:else>
</body>
</html>