package me.steffenjacobs.fetchgrades.gradefetcher;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Module {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    private int examNumber;
    private String semester;
    private Date examDate;
    private int round;
    private String moduleName;
    private String examiner;
    private String examType;
    private double grade;
    private int ects;
    private boolean passed;
    private int attempt;

    public Module(String modulName, double grade, int ects) {

        this.moduleName = modulName;

        this.grade = grade;
        this.ects = ects;

    }

    public Module(int examNumber, String semester, Date examDate, int round, String moduleName, String examiner, String examType, double grade, int ects, boolean passed,
                  int attempt) {
        super();
        this.examNumber = examNumber;
        this.semester = semester;
        this.examDate = examDate;
        this.round = round;
        this.moduleName = moduleName;
        this.examiner = examiner;
        this.examType = examType;
        this.grade = grade;
        this.ects = ects;
        this.passed = passed;
        this.attempt = attempt;
    }

    public int getExamNumber() {
        return examNumber;
    }

    public String getSemester() {
        return semester;
    }

    public Date getExamDate() {
        return examDate;
    }

    public int getRound() {
        return round;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getExaminer() {
        return examiner;
    }

    public String getExamType() {
        return examType;
    }

    public double getGrade() {
        return grade;
    }

    public int getEcts() {
        return ects;
    }

    public boolean isPassed() {
        return passed;
    }

    public int getAttempt() {
        return attempt;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("MODUL:\n");
        buf.append("ExamNr:" + this.examNumber + "\n");
        buf.append("Semester: " + this.semester + "\n");
        buf.append("Exam Date: " + DATE_FORMAT.format(this.examDate) + "\n");
        buf.append("Round: " + this.round + "\n");
        buf.append("Module Name: " + this.moduleName + "\n");
        buf.append("Examiner: " + this.examiner + "\n");
        buf.append("Exam Type: " + this.examType + "\n");
        buf.append("Grade: " + this.grade + "\n");
        buf.append("ECTS: " + this.ects + "\n");
        buf.append("Passed: " + this.passed + "\n");
        buf.append("Attempt: " + this.attempt + "\n");
        buf.append("-----------------------------------------------------------------------------------------------\n");
        return buf.toString();
    }
}
