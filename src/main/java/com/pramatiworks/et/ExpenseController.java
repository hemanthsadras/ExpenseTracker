package com.pramatiworks.et;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pramatiworks.et.models.Expense;
import com.pramatiworks.et.repositories.ExpenseRepository;

@RestController
@RequestMapping("/expense")
public class ExpenseController {
	
	private ExpenseRepository expenseRepository;
	
	public ExpenseController(ExpenseRepository expenseRepository) {
		this.expenseRepository = expenseRepository;
	}
	
	@GetMapping("/{id}")
	public Expense getExpense(@PathVariable String id) {
		Expense expense = this.expenseRepository.findOne(id);
		return expense;
	}
	
	@GetMapping("/month")
	public List<Expense> getExpensesForCurrentMonth() {
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		LocalDate from = LocalDate.of(currentYear, currentMonth, 1);
		LocalDate to;
		if(currentMonth < 12) {
			to = LocalDate.of(currentYear, currentMonth+1, 1);
		}
		else {
			to = LocalDate.of(currentYear+1, 1, 1);
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return this.expenseRepository.findByUsernameAndDateBetween(username, from, to);
	}
	
	
	@GetMapping("/{year}/{month}")
	public List<Expense> getExpensesForPeriod(@PathVariable int year, @PathVariable int month) {
		LocalDate from = LocalDate.of(year, month, 1);
		LocalDate to;
		if(month < 12) {
			to = LocalDate.of(year, month+1, 1);
		}
		else {
			to = LocalDate.of(year+1, 1, 1);
		}
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();	
		return this.expenseRepository.findByUsernameAndDateBetween(username, from, to);
	
	}
	
	@PostMapping
	public Expense addExpense(@RequestBody Expense newExpense) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		newExpense.setUsername(username);
		Expense expense = this.expenseRepository.insert(newExpense);
		return expense;
	}
	
	@DeleteMapping("/{id}")
	public void deleteExpense(@PathVariable String id) {
		this.expenseRepository.delete(id);
	}

}
