package me.steffenjacobs.fetchgrades.web;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyRegisteredModule {
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	
	private int examNr;
	private String moduleName;
	private String examiner;
	private String examType;
	private String semester;
	private Date examDate;
	private int round;
	private String examTime;
	private String seat;
	private String room;
	

	public int getExamNr() {
		return examNr;
	}


	public void setExamNr(int examNr) {
		this.examNr = examNr;
	}


	public String getModuleName() {
		return moduleName;
	}


	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}


	public String getExaminer() {
		return examiner;
	}


	public void setExaminer(String examiner) {
		this.examiner = examiner;
	}


	public String getExamType() {
		return examType;
	}


	public void setExamType(String examType) {
		this.examType = examType;
	}


	public String getSemester() {
		return semester;
	}


	public void setSemester(String semester) {
		this.semester = semester;
	}


	public Date getDate() {
		return examDate;
	}


	public void setDate(Date date) {
		this.examDate = date;
	}


	public int getRound() {
		return round;
	}


	public void setRound(int round) {
		this.round = round;
	}


	public String getExamTime() {
		return examTime;
	}


	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}


	public String getSeat() {
		return seat;
	}


	public void setSeat(String seat) {
		this.seat = seat;
	}


	public String getRoom() {
		return room;
	}


	public void setRoom(String room) {
		this.room = room;
	}
	
	
	@Override
	public String toString() {	
		return "ExamNr: " + this.examNr + "\n" +
				"Module name: " + this.moduleName + "\n" + 
				"Examiner: " + this.examiner + "\n" + 
				"Exam Type: " + this.examType + "\n" + 
				"Semester: " + this.semester + "\n" + 
				"Exam Time: " + this.examTime + "\n" + 
				"Exam Date: " + (this.examDate == null ? " - " : DATE_FORMAT.format(this.examDate)) + "\n" +
				"Round: " + this.round + "\n" + 
				"Seat: " + this.seat + "\n" +
				"Room: " + this.room + "\n" +
				"------------------------------------\n";
	}

}
