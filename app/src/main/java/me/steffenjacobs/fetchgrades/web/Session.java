package me.steffenjacobs.fetchgrades.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Session {

	private static final Logger LOG = LoggerFactory.getLogger(Session.class);

	public static CloseableHttpClient httpClient;

	public static String cookie;
	public static String lt;

	private static String username;
	private static String password;
	
	public static Long currentTimestamp;


	public static void setSessionCredentials(String username, String password) {
		Session.username = username;
		Session.password = password;
	}
	
	public static boolean isStillLoggedIn() {
		if(currentTimestamp != null && cookie != null && lt != null) {
			long diff = currentTimestamp - System.currentTimeMillis();
			
			return (diff < 29 * 60 * 1000 )? true : false; //30min
			
		}else {
			return false;
		}
	}

	private static void fetchCookie() throws IOException {
		httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(Constants.URL_ILIAS);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		String html = EntityUtils.toString(response.getEntity());
		Header[] headerArray = response.getAllHeaders();

		String tempCookie = "";
		for (int i = 0; i < headerArray.length; i++) {
			if (headerArray[i].getName().equals("Set-Cookie")) {
				tempCookie = headerArray[i].getValue();
				break;
			}
		}
		Session.cookie = tempCookie.split("\\;")[0];
		LOG.info(Session.cookie);
		Session.lt = (ParserSession.parseLT(html));
	}

	public static boolean performLogin(String username, String password) throws IOException {
		fetchCookie();
		
		Session.setSessionCredentials(username, password);
		HttpPost httppost = new HttpPost(Constants.URL_ILIAS);
		httppost.setHeader("Cookie", Session.cookie);
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("username", Session.username));
		params.add(new BasicNameValuePair("password", Session.password));
		params.add(new BasicNameValuePair("lt", lt));
		params.add(new BasicNameValuePair("execution", "e1s1"));
		params.add(new BasicNameValuePair("_eventId", "submit"));
		params.add(new BasicNameValuePair("submit", "Anmelden"));
		httppost.setEntity(new UrlEncodedFormEntity(params));
		CloseableHttpResponse response = httpClient.execute(httppost);
		
		int status_code = Integer.parseInt( response.getStatusLine().toString().split("\\s+")[1] );
		System.out.println(status_code);
		if(status_code == 302) {
			currentTimestamp = System.currentTimeMillis();
			return true;
		}else {
			return false;
		}		
	}
	
	public static void performLogout() throws IOException {
		HttpGet httpget = new HttpGet(Constants.URL_LOGOUT);
		httpget.setHeader("Cookie", Session.cookie);
		httpClient.execute(httpget);

		Session.cookie = null;
		Session.lt = null;
		Session.currentTimestamp = null;
		httpClient.close();
	}
	
	
	public static List<Module> fetchGrades() throws IOException {		
		if(! isStillLoggedIn()) {		
			boolean success = Session.performLogin(Session.username, Session.password);
			if(!success)
				return null;
			
		}		
		return ParserSession.parseGrades(RetrieveGrades.fetchNotenPage(RetrieveGrades.fetchPruefungenNotenspiegel()));
	}
	
	public static Object[] fetchMyRegisteredModules() throws ParseException, IOException{
		if(! isStillLoggedIn()) {		
			boolean success = Session.performLogin(Session.username, Session.password);
			System.out.println(success);
			if(!success)
				return null;			
		}		
		
		String htmlPage = RetrieveMyRegisteredModules.fetchMyRegisteredExamsPage(RetrieveMyRegisteredModules.fetchPruefungenNotenspiegel());
		String htmlel = RetrieveMyRegisteredModules.fetchMyRegisteredExamsTable(htmlPage);

		User u = RetrieveMyRegisteredModules.parseUser(htmlel);
		ArrayList<MyRegisteredModule> myRegisteredModuleList = RetrieveMyRegisteredModules.parseMyRegisteredModules(htmlel);		
		
		Object[] array = new Object[2];
		array[0] = u;
		array[1] = myRegisteredModuleList;
		return array;
	}
	
}
