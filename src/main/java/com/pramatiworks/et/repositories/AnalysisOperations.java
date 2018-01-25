package com.pramatiworks.et.repositories;

import java.time.LocalDate;
import java.util.List;

import com.pramatiworks.et.models.CategoryAnalysis;
import com.pramatiworks.et.models.DailyExpense;
import com.pramatiworks.et.models.MonthProjection;


public interface AnalysisOperations {

	List<CategoryAnalysis> analysis(String emailId, LocalDate from, LocalDate to);
	List<MonthProjection> getYearlyProjection(String emailId, int year);
	List<DailyExpense> getMonthlyProjection(String emailId, int month, int year);
}
