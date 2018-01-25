package com.pramatiworks.et.repositories;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.fields;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.pramatiworks.et.models.CategoryAnalysis;
import com.pramatiworks.et.models.DailyExpense;
import com.pramatiworks.et.models.MonthProjection;


public class ExpenseRepositoryImpl implements AnalysisOperations {

	private final MongoTemplate mongoTemplate;

	@Autowired
	public ExpenseRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<CategoryAnalysis> analysis(String emailId, LocalDate from, LocalDate to) {
		MatchOperation matchOperation = getMatchOperation(emailId, from, to);
		GroupOperation groupOperation = getGroupOperation();
		ProjectionOperation projectionOperation = getProjectionOperation();
		
		return mongoTemplate.aggregate(Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation), 
				                "Expenses", CategoryAnalysis.class).getMappedResults();
		
	}

	private MatchOperation getMatchOperation(String emailId, LocalDate from, LocalDate to) {
		Date fromDate = Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date toDate = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Criteria matchCriteria = where("username")
									.is(emailId)
									.andOperator(where("date").gte(fromDate).andOperator(where("date").lt(toDate)));
		return match(matchCriteria);
	}

	private GroupOperation getGroupOperation() {
		return group("category")
					.sum("amount").as("totalAmount");
	}
	
	private ProjectionOperation getProjectionOperation() {
		return project("totalAmount")
				.and("categoryName").previousOperation();
	}

	@Override
	public List<MonthProjection> getYearlyProjection(String emailId, int year) {
		MatchOperation matchByUserName = match(where("username").is(emailId));
		ProjectionOperation projection = aggregateSpendsMonthly();
		MatchOperation filterByYear = match(where("year").is(year));
		GroupOperation groupByMonth = groupByMonth();
		ProjectionOperation previousOperationProjection = project("totalAmount")
														.and("month")
														.previousOperation();
		
		Aggregation aggregation = Aggregation.newAggregation(matchByUserName,projection,filterByYear,groupByMonth,previousOperationProjection);
		
		return mongoTemplate.aggregate(aggregation, "Expenses",MonthProjection.class).getMappedResults();
	}
	
	private ProjectionOperation aggregateSpendsMonthly() {
		return project("amount")
				.andExpression("month(date)").as("month")
				.andExpression("year(date)").as("year");
	}
	
	private GroupOperation groupByMonth() {
		return group(fields().and("month"))
				.sum("amount").as("totalAmount");
	}

	@Override
	public List<DailyExpense> getMonthlyProjection(String emailId, int month, int year) {
		ProjectionOperation finalProjection = project("day").and("amount").previousOperation();
		
		Aggregation aggregation = Aggregation.newAggregation(
											match(where("username").is(emailId)),
											getProjectionWithMonthAndYear(),
											getMatchWithGiveMonthAndYear(emailId, month, year),
											sort(Sort.Direction.ASC, "day")
											);
		
		return mongoTemplate.aggregate(aggregation, "Expenses", DailyExpense.class).getMappedResults();
	}
	
	private MatchOperation getMatchWithGiveMonthAndYear(String emailId, int month, int year) {
		return match(where("month").is(month)
					.and("year").is(year));
					
					 
	}
	
	private ProjectionOperation getProjectionWithMonthAndYear() {
		return project("amount")
				.andExpression("month(date)").as("month")
				.andExpression("year(date)").as("year")
				.andExpression("dayOfMonth(date)").as("day");
	}

}
