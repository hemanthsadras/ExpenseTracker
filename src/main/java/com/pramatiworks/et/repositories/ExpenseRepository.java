package com.pramatiworks.et.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pramatiworks.et.models.Expense;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense,String> , AnalysisOperations {
	List<Expense> findByUsernameAndDateBetween(String username, LocalDate from, LocalDate to);
}
