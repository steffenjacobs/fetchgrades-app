package me.steffenjacobs.fetchgrades.web;

public final class Constants {
	private Constants() {

	}

	public static final String URL_ILIAS = "https://cas.uni-mannheim.de/cas/login?service=https%3A%2F%2Filias.uni-mannheim.de%2Filias.php%3FbaseClass%3DilPersonalDesktopGUI%26cmd%3DjumpToSelectedItems";
	public static final String URL_PORTAL2_GRADES = "https://cas.uni-mannheim.de/cas/login?service=https%3A%2F%2Fportal.uni-mannheim.de%2Fqisserveriframe%2Frds%3Fstate%3Duser%26type%3D1%26topitem%3Dpruefungen%26language%3Dde";
	public static final String URL_LOGOUT = "https://cas.uni-mannheim.de/cas/logout?service=https%3A%2F%2Fportal2.uni-mannheim.de/portal2/rds%3Fstate%3Duser%26type%3D4%26category%3Dauth.logout";
}
