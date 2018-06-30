package me.steffenjacobs.fetchgrades.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserRetrieveMyRegisteredModules {

	public static String parseMyRegisteredExamsASIURL(String html) {
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("a");
		int i = 0;
		for (Iterator<Element> iterator = links.iterator(); iterator.hasNext(); i++) {
			Element element = (Element) iterator.next();
			if(i == 1){
				return element.attr("href");
			}
		}
		return null;
	}
	
	public static String parseMyRegisteredExamsTableURL(String html) {
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("a");
		
		int i = 0;
		for (Iterator<Element> iterator = links.iterator(); iterator.hasNext(); i++) {
			Element element = (Element) iterator.next();
			
			if(i == 2) {
				
				return element.attr("href");
			}
			
		}
		return null;
		
	}
	
	public static User parseUserInfo(String html) {
		Document doc = Jsoup.parse(html);
		ArrayList<Element> posheaders = doc.getElementsByClass("posheader");
		
		String user = "";
		String birthdayAndLocation = "";
		String degree = "";
		String matrikel = "";
		String address = "";
		
		int i = 0;
		for (Iterator<Element> iterator = posheaders.iterator(); iterator.hasNext(); i++) {
			Element element = (Element) iterator.next();
			
			switch(i) {
			case 1: 
				 user = element.text();
				 break;
			case 3:
				 birthdayAndLocation = element.text();
				 break;
			case 5: 
				 degree = element.text();
				 break;
			case 7: 
				 matrikel = element.text();
				 break;
			case 9: 
				 address = element.text();
				 break;
				 default: break;
			}
			
			//System.out.println(element.toString());
		}
		return new User(user, birthdayAndLocation, degree, matrikel, address);
	}
	
	public static MyRegisteredModule parseMyRegisteredExamsModule(Element ele2) {
		ArrayList<Element> tdl = ele2.getElementsByTag("td");
		MyRegisteredModule mrm = new MyRegisteredModule();
		int k = 0;
		for (Iterator<Element> iterator = tdl.iterator(); iterator.hasNext(); k++) {
			Element element = (Element) iterator.next();
			//System.out.println(element.toString());
			
			switch(k){
			case 0: mrm.setExamNr(Integer.parseInt(element.text())); break;
			case 1: mrm.setModuleName(element.text());break;
			case 2: mrm.setExaminer(element.text());break;
			case 3: mrm.setExamType(element.text());break;
			case 4: mrm.setSemester(element.text());break;
			case 5: 
				try {
					Calendar calendar = Calendar.getInstance();
					String[] details = element.text().trim().replaceAll("\u00a0", "").split("\\.");
		            calendar.set(Integer.parseInt(details[2]), Integer.parseInt(details[1]), Integer.parseInt(details[0]));
		            mrm.setDate(calendar.getTime());
				}catch(ArrayIndexOutOfBoundsException e) {
					mrm.setDate(null);
				}
				break;
			case 6: mrm.setRound(Integer.parseInt(element.text())); break;
			case 7: mrm.setExamTime(element.text());break;
			case 8: mrm.setSeat( element.text());break;
			case 9: mrm.setRoom(element.text());break;
			default: break;
			}
		}
		
		return mrm;
	}
	
	public static ArrayList<MyRegisteredModule> parseMyRegisteredExams(String html) {
		ArrayList<MyRegisteredModule> myRegisteredModulesList = new ArrayList<>();
		
		Document doc = Jsoup.parse(html);
		ArrayList<Element> mod_n = doc.getElementsByTag("table");
		
		int i = 0;
		for (Iterator<Element> iterator = mod_n.iterator(); iterator.hasNext(); i++) {
			Element element = (Element) iterator.next();
			
			if(i == 1) {
				//System.out.println(element.toString());
				ArrayList<Element> trs = element.getElementsByTag("tr");
				
				int j = 0;
				for (Iterator<Element> iterator2 = trs.iterator(); iterator2.hasNext(); j++) {
					Element ele2 = (Element) iterator2.next();
					//System.out.println(ele2.toString());
					
					if(j > 0) { //j == 0 are columnheaders
						myRegisteredModulesList.add(parseMyRegisteredExamsModule(ele2));
						

					}										
				}								
			}						
		}
		return myRegisteredModulesList;
	}
	
}
