package me.steffenjacobs.fetchgrades.web;

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

public class ParserSession {
	
	private static final Logger LOG = LoggerFactory.getLogger(ParserSession.class);
	
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
	
	public static List<Module> convertListToModules(List<Element[]> list) {
		ArrayList<Module> moduleList = new ArrayList<>();

		for (Iterator<Element[]> iterator = list.iterator(); iterator.hasNext();) {
			moduleList.add(parseModule(iterator.next()));
		}
		return moduleList;
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

}
