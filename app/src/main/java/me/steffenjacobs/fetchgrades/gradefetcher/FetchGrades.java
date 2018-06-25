package me.steffenjacobs.fetchgrades.gradefetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import org.apache.http.Header;
//import org.apache.http.NameValuePair;
//import org.apache.http.ParseException;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class FetchGrades {

	private static final Logger LOG = LoggerFactory.getLogger(FetchGrades.class);

	private CloseableHttpClient httpClient;

	private String cookie;
	private String lt;

	private String username;
	private String password;

	public FetchGrades(String username, String password) {
		httpClient = HttpClients.createDefault();
		this.username = username;
		this.password = password;
	}

	private void fetchCookie() throws IOException {
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
		this.cookie = tempCookie.split("\\;")[0];
		LOG.info(this.cookie);
		this.lt = (Parser.parseLT(html));
	}

	private void performLogin() throws IOException {
		HttpPost httppost = new HttpPost(Constants.URL_ILIAS);
		httppost.setHeader("Cookie", this.cookie);
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("username", this.username));
		params.add(new BasicNameValuePair("password", this.password));
		params.add(new BasicNameValuePair("lt", lt));
		params.add(new BasicNameValuePair("execution", "e1s1"));
		params.add(new BasicNameValuePair("_eventId", "submit"));
		params.add(new BasicNameValuePair("submit", "Anmelden"));
		httppost.setEntity(new UrlEncodedFormEntity(params));
		httpClient.execute(httppost);
	}

	private String fetchPruefungenNotenspiegel() throws IOException {
		HttpGet httpget = new HttpGet(Constants.URL_PORTAL2_GRADES);
		httpget.setHeader("Cookie", this.cookie);
		CloseableHttpResponse response = httpClient.execute(httpget);
		return EntityUtils.toString(response.getEntity());
	}

	private String fetchNotenPage(String html) throws IOException {
		String asiUrl = Parser.parseASIURL(html);
		HttpGet httpget = new HttpGet(asiUrl);
		httpget.setHeader("Cookie", this.cookie);
		CloseableHttpResponse response = httpClient.execute(httpget);
		return EntityUtils.toString(response.getEntity());
	}

	private void performLogout() throws IOException {
		HttpGet httpget = new HttpGet(Constants.URL_LOGOUT);
		httpget.setHeader("Cookie", this.cookie);
		httpClient.execute(httpget);

		this.cookie = null;
		this.lt = null;
		httpClient.close();
	}

	public List<Module> fetchGrades() throws IOException {
		this.fetchCookie();
		this.performLogin();
		String htmlPage = this.fetchNotenPage(this.fetchPruefungenNotenspiegel());
		this.performLogout();
		return Parser.parseGrades(htmlPage);
	}

	public boolean hasGrades() throws IOException{
		this.fetchCookie();
		this.performLogin();
		String htmlPage = this.fetchNotenPage(this.fetchPruefungenNotenspiegel());
		this.performLogout();
		return Parser.hasGrades(htmlPage);
	}
}
