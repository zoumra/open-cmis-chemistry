package grails.plugin.opencmis

import java.util.Collections
import java.util.HashMap
import java.util.List
import java.util.Map

import org.apache.chemistry.opencmis.client.bindings.spi.StandardAuthenticationProvider

class AuthenticationProviderService extends StandardAuthenticationProvider {

@Override
  public Map<String, List<String>> getHTTPHeaders(String url) {

    Map<String, List<String>> headers = super.getHTTPHeaders(url)
    if (headers == null) {
      headers = new HashMap<String, List<String>>()
    }

    Object exampleUserObject =
      getSession().get("org.example.user")
    if (exampleUserObject instanceof String) {
      headers.put("example-user",
          Collections.singletonList((String) exampleUserObject))
    }

    Object exampleSecretObject = 
      getSession().get("org.example.secret");
    if (exampleSecretObject instanceof String) {
      headers.put("example-secret",
          Collections.singletonList((String) exampleSecretObject))
    }

    return headers;
  }
}