package me.steffenjacobs.fetchgrades.web;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserRetrieveGrades {
	
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

}
