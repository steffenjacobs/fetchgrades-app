package me.steffenjacobs.fetchgrades.web;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.util.EntityUtils;


public class RetrieveMyRegisteredModules {
	
	public static String fetchPruefungenNotenspiegel() throws  IOException{
		HttpGet httpget = new HttpGet(Constants.URL_PORTAL2_GRADES);
		httpget.setHeader("Cookie", Session.cookie);
		CloseableHttpResponse response = Session.httpClient.execute(httpget);
		String html = EntityUtils.toString(response.getEntity());
		return html;
	}
	
	public static String fetchMyRegisteredExamsPage(String html) throws IOException{
		String asiUrl = ParserRetrieveMyRegisteredModules.parseMyRegisteredExamsASIURL(html);
		HttpGet httpget = new HttpGet(asiUrl);
		httpget.setHeader("Cookie", Session.cookie);
		CloseableHttpResponse response = Session.httpClient.execute(httpget);
		String htmlPage = EntityUtils.toString(response.getEntity());
		return htmlPage;
	}
	
	public static String fetchMyRegisteredExamsTable(String html) throws IOException {
		String tableURL = ParserRetrieveMyRegisteredModules.parseMyRegisteredExamsTableURL(html);		
		HttpGet httpget = new HttpGet(tableURL);
		httpget.setHeader("Cookie", Session.cookie);
		CloseableHttpResponse response = Session.httpClient.execute(httpget);
		String htmlPage = EntityUtils.toString(response.getEntity());
		return htmlPage;		
	}
	
	public static User parseUser(String html) {
		return ParserRetrieveMyRegisteredModules.parseUserInfo(html);
	}
	
	public static ArrayList<MyRegisteredModule> parseMyRegisteredModules(String html){
		return ParserRetrieveMyRegisteredModules.parseMyRegisteredExams(html);		
	}

}
