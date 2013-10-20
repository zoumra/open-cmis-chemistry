<!DOCTYPE html>
<html>
<head>
<title>Callback</title>
<script type="text/javascript">
function refreshParentAndClose()
{
  // Force refresh parent
  window.opener.location.reload(true);

  // Close
  if (window.opener.progressWindow)
  {
    window.opener.progressWindow.close()
  }

  window.close();
}
</script>
</head>
<body onload="refreshParentAndClose()">
Nothing to see here, move along...
</body>
</html>