package me.steffenjacobs.fetchgrades.web;

import java.io.IOException;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.util.EntityUtils;

public class RetrieveGrades {

	
	public static String fetchPruefungenNotenspiegel() throws IOException {
		HttpGet httpget = new HttpGet(Constants.URL_PORTAL2_GRADES);
		httpget.setHeader("Cookie", Session.cookie);
		CloseableHttpResponse response = Session.httpClient.execute(httpget);
		Session.currentTimestamp = System.currentTimeMillis();
		return EntityUtils.toString(response.getEntity());
	}

	public static String fetchNotenPage(String html) throws IOException {
		String asiUrl = ParserRetrieveGrades.parseASIURL(html);
		HttpGet httpget = new HttpGet(asiUrl);
		httpget.setHeader("Cookie", Session.cookie);
		CloseableHttpResponse response = Session.httpClient.execute(httpget);
		Session.currentTimestamp = System.currentTimeMillis();
		return EntityUtils.toString(response.getEntity());
	}
	
}
