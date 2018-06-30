package me.steffenjacobs.fetchgrades.web;

import java.util.List;

public final class GradeCalculator {

	private GradeCalculator() {

	}

	public static double calculateAverage(List<Module> grades) {
		if (grades != null) {
			int sumEcts = 0;
			double sumGrades = 0;
			for (int i = 0; i < grades.size(); i++) {
				sumGrades += grades.get(i).getGrade() * grades.get(i).getEcts();
				sumEcts += grades.get(i).getEcts();
			}
			if (sumEcts == 0) {
				return Double.NaN;
			}
			return sumGrades / sumEcts;
		} else {
			return 0;
		}
	}
}
