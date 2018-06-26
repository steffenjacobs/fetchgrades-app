package me.steffenjacobs.fetchgrades.gradefetcher;

public class User {

	private String name;
	private String birthdayAndLocation;
	private String degree;
	private String matrikel;
	private String address;
	
	public User(String name, String birthdayAndLocation, String degree, String matrikel, String adress) {
		super();
		this.name = name;
		this.birthdayAndLocation = birthdayAndLocation;
		this.degree = degree;
		this.matrikel = matrikel;
		this.address = adress;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthdayAndLocation() {
		return birthdayAndLocation;
	}
	public void setBirthdayAndLocation(String birthdayAndLocation) {
		this.birthdayAndLocation = birthdayAndLocation;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getMatrikel() {
		return matrikel;
	}
	public void setMatrikel(String matrikel) {
		this.matrikel = matrikel;
	}
	public String getAdress() {
		return address;
	}
	public void setAdress(String adress) {
		this.address = adress;
	}
	
	@Override
	public String toString() {
		return "Name: " + this.name + "\n" +
				"Birthday & Location: " + this.birthdayAndLocation + "\n" +
				"Degree: " + this.degree + "\n" +
				"Matrikel NR: " + this.matrikel + "\n" +
				"address: " + this.address + "\n";
	}
	
	
}
