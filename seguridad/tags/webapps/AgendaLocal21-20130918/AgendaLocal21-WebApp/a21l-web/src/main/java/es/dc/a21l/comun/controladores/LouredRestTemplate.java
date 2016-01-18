package es.dc.a21l.comun.controladores;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.protocol.Protocol;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class LouredRestTemplate extends RestTemplate {

	public LouredRestTemplate(String login, String password,Integer puerto) {
		HttpClient client = new HttpClient();
		Protocol.registerProtocol("https", 
				new Protocol("https", new SimpleSSLProtocolSocketFactory(), puerto));
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				login, password);
		client.getState().setCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT,
						AuthScope.ANY_REALM), credentials);
		CommonsClientHttpRequestFactory commons = new CommonsClientHttpRequestFactory(
				client);
		setRequestFactory(commons);
	}
}