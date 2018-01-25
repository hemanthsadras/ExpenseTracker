package com.pramatiworks.et;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pramatiworks.et.models.CategoryAnalysis;
import com.pramatiworks.et.models.DailyExpense;
import com.pramatiworks.et.models.MonthProjection;
import com.pramatiworks.et.repositories.ExpenseRepository;
import com.pramatiworks.et.utils.CalendarUtil;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {
	

	private ExpenseRepository expenseRepository;
	
	public AnalysisController(ExpenseRepository expenseRepository) {
		this.expenseRepository = expenseRepository;
	}
	
	@GetMapping("category/{month}/{year}")
	public List<CategoryAnalysis> getCategoryAnalsis(@PathVariable int month, @PathVariable int year) {
		LocalDate to;
		LocalDate from = LocalDate.of(year, month, 1);
		if(month >= 12) {
			to = LocalDate.of(year+1, 1, 1);
		}
		else {
			to = LocalDate.of(year, month+1,1);
		}
		
		String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
		List<CategoryAnalysis> results = this.expenseRepository.analysis(emailId, from, to);
		return results;
	}
	
	@GetMapping("/year/{year}")
	public List<MonthProjection> getYearlyProjection(@PathVariable int year) {
		String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
		List<MonthProjection> results = this.expenseRepository.getYearlyProjection(emailId, year);
		
		results.forEach(result -> {
			result.setMonthName(CalendarUtil.getMonthName(result.getMonth()));
		});
		return results;
	}
	
	
	@GetMapping("/month/{month}/{year}")
	public List<DailyExpense> getMonthlyProjection(@PathVariable int month, @PathVariable int year) {
		String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
		return this.expenseRepository.getMonthlyProjection(emailId, month, year);
	}

}
