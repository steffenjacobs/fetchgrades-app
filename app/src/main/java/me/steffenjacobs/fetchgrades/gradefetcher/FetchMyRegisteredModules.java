package me.steffenjacobs.fetchgrades.gradefetcher;

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

public class FetchMyRegisteredModules {

	private CloseableHttpClient httpclient;
	private CloseableHttpResponse response;
	private HttpGet httpget;
	private HttpPost httppost;
	
	private String cookie;
	private String lt;
	
	private String username;
	private String password;
	
	private User userObj;
	private ArrayList<MyRegisteredModule> myRegisteredModuleList;
	
	
	public FetchMyRegisteredModules(String username, String password) {
		httpclient = HttpClients.createDefault();
		this.username = username;
		this.password = password;
	}
	
	
	public User getUserObj() {
		return userObj;
	}

	public void setUserObj(User userObj) {
		this.userObj = userObj;
	}

	public ArrayList<MyRegisteredModule> getMyRegisteredModuleList() {
		return myRegisteredModuleList;
	}

	public void setMyRegisteredModuleList(ArrayList<MyRegisteredModule> myRegisteredModuleList) {
		this.myRegisteredModuleList = myRegisteredModuleList;
	}

	
	public void fetchCookie() throws ParseException, IOException{
		httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(Constants.URL_ILIAS);
		response = httpclient.execute(httpget);
		String html = EntityUtils.toString(response.getEntity());
		Header[] headerArray = response.getAllHeaders();
		String cookie = "";
		for (int i = 0; i < headerArray.length; i++) {
			if (headerArray[i].getName().equals("Set-Cookie")) {
				cookie = headerArray[i].getValue();
				break;
			}
		}
		this.cookie = cookie.split("\\;")[0];	
		System.out.println(this.cookie);
		this.lt = (Parser.parseLT(html));
	}
	
	public void performLogin() throws IOException, IOException{
		this.httppost = new HttpPost(Constants.URL_ILIAS);
		httppost.setHeader("Cookie", this.cookie);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", this.username));
		params.add(new BasicNameValuePair("password", this.password));
		params.add(new BasicNameValuePair("lt", lt));
		params.add(new BasicNameValuePair("execution", "e1s1"));
		params.add(new BasicNameValuePair("_eventId", "submit"));
		params.add(new BasicNameValuePair("submit", "Anmelden"));
		httppost.setEntity(new UrlEncodedFormEntity(params));
		response = httpclient.execute(httppost);
	}
	
	public String fetchPruefungenNotenspiegel() throws IOException{
		this.httpget = new HttpGet(Constants.URL_PORTAL2_GRADES);
		httpget.setHeader("Cookie", this.cookie);
		response = httpclient.execute(httpget);
        return EntityUtils.toString(response.getEntity());
	}
	
	public String fetchMyRegisteredExamsPage(String html) throws IOException{
		String asiUrl = Parser.parseMyRegisteredExamsASIURL(html);
		httpget = new HttpGet(asiUrl);
		httpget.setHeader("Cookie", this.cookie);
		response = httpclient.execute(httpget);
        return EntityUtils.toString(response.getEntity());
	}
	
	public String fetchMyRegisteredExamsTable(String html) throws IOException {
		String tableURL = Parser.parseMyRegisteredExamsTableURL(html);		
		this.httpget = new HttpGet(tableURL);
		httpget.setHeader("Cookie", this.cookie);
		response = httpclient.execute(httpget);
        return EntityUtils.toString(response.getEntity());
		
	}
	
	public static User parseUser(String html) {
		return Parser.parseUserInfo(html);
	}
	
	public static ArrayList<MyRegisteredModule> parseMyRegisteredModules(String html){
		return Parser.parseMyRegisteredExams(html);		
	}
	
	
	public void performLogout() throws IOException{
		this.httpget = new HttpGet(Constants.URL_LOGOUT);
		httpget.setHeader("Cookie", this.cookie);
		this.response = httpclient.execute(httpget);
		
		this.cookie = null;
		this.lt = null;
	}
	
	public static FetchMyRegisteredModules fetchMyRegisteredModules(String username, String password) throws ParseException, IOException{
		FetchMyRegisteredModules n = new FetchMyRegisteredModules(username, password);
		n.fetchCookie();
		n.performLogin();
		String htmlPage = n.fetchMyRegisteredExamsPage(n.fetchPruefungenNotenspiegel());
		String htmlel = n.fetchMyRegisteredExamsTable(htmlPage);
		n.performLogout();

		System.out.println("Script executed successfully");
		n.userObj = parseUser(htmlel);
		n.myRegisteredModuleList = parseMyRegisteredModules(htmlel);		
		return n;
	}
	
	
	public static void main(String[] args) {
		try {
			if (args.length >= 2) {
				FetchMyRegisteredModules f = fetchMyRegisteredModules(args[0], args[1]);
				System.out.println(f.getUserObj());
				System.out.println(f.getMyRegisteredModuleList());
			
			}else {
				System.err.println("specify username & pw in sys args");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
