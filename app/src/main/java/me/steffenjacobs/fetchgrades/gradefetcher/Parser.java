package me.steffenjacobs.fetchgrades.gradefetcher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {

	private static final Logger LOG = LoggerFactory.getLogger(Parser.class);

	private Parser() {

	}


	public static boolean hasGrades(String html) {
		ArrayList<Element[]> list = new ArrayList<>();
		Document doc = Jsoup.parse(html);
		ArrayList<Element> elems = doc.getElementsByAttributeValue("class", "posrecords");
		return elems.size() > 0;
	}

	public static String parseLT(String html) {
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("input");
		for (Iterator<Element> iterator = links.iterator(); iterator.hasNext();) {
			Element element = iterator.next();
			if (element.hasAttr("value") && element.attr("value").matches("LT-(.*)")) {
				return element.attr("value");
			}
		}
		return null;
	}

	public static String parseASIURL(String html) {
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("a");
		int i = 0;
		for (Iterator<Element> iterator = links.iterator(); iterator.hasNext(); i++) {
			Element element = iterator.next();
			if (i == 2) {
				return element.attr("href");
			}
		}
		return null;
	}
	
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

	public static List<Module> parseGrades(String html) {
		ArrayList<Element[]> list = new ArrayList<>();
		Document doc = Jsoup.parse(html);
		ArrayList<Element> lol = doc.getElementsByAttributeValue("class", "posrecords");
		int i = 0;
		Element[] inner = new Element[11];
		for (Iterator<Element> iterator = lol.iterator(); iterator.hasNext(); i++) {
			Element element = iterator.next();
			inner[i] = element;
			if (i == 10) {
				list.add(inner);
				inner = new Element[11];
				i = -1;
			}
		}
		return convertListToModules(list);
	}

	private static Module parseModule(Element[] elements) {
		int examNr = Integer.parseInt(elements[0].text().trim().replaceAll("\u00a0", ""));
		String semester = elements[1].text().trim().replaceAll("\u00a0", "");
		int round = Integer.parseInt(elements[2].text().trim().replaceAll("\u00a0", ""));

		Calendar calendar = Calendar.getInstance();
		try {
			String[] details = elements[3].text().trim().replaceAll("\u00a0", "").split("\\.");
			calendar.set(Integer.parseInt(details[2]), Integer.parseInt(details[1]), Integer.parseInt(details[0]));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		String moduleName = elements[4].text().trim();
		String examiner = elements[5].text().trim().replaceAll("\u00a0", "");
		String examType = elements[6].ownText();
		Double grade = null;
		try {
			grade = Double.parseDouble(elements[7].text().replaceAll("\u00a0", "").replace(",", "."));
		} catch (NumberFormatException e) {
			grade = 0.0;
		}

		String ectsX = elements[8].toString();
		ectsX = ectsX.replace("<td align=\"center\" class=\"posrecords\"> <script type=\"text/javascript\" language=\"JavaScript\">", "");
		ectsX = ectsX.replace("<!--", "");
		ectsX = ectsX.replace("//-->", "");
		ectsX = ectsX.replace("</script> </td>", "");
		ectsX = ectsX.replace("document.write(Math.round(", "");
		ectsX = ectsX.replace("*10)/10);", "");
		ectsX = ectsX.replace("\n", "").replace("\r", "");
		ectsX = ectsX.trim();
		int ects = (int) Double.parseDouble((ectsX));

		boolean passed = elements[9].text().startsWith("bestanden");
		int attempt = Integer.parseInt(elements[10].text().subSequence(0, 1).toString());

		return new Module(examNr, semester, calendar.getTime(), round, moduleName, examiner, examType, grade, ects, passed, attempt);
	}

	public static List<Module> convertListToModules(List<Element[]> list) {
		ArrayList<Module> moduleList = new ArrayList<>();

		for (Iterator<Element[]> iterator = list.iterator(); iterator.hasNext();) {
			moduleList.add(parseModule(iterator.next()));
		}
		return moduleList;
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
