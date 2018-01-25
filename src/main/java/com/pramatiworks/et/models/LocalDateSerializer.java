package com.pramatiworks.et.models;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {

	@Override
	public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("expenseDate", localDate.getDayOfMonth() + "/" + localDate.getMonthValue() + "/" + localDate.getYear());
		jsonGenerator.writeNumberField("day", localDate.getDayOfMonth());
		jsonGenerator.writeNumberField("month", localDate.getMonthValue());
		jsonGenerator.writeNumberField("year", localDate.getYear()); 
		jsonGenerator.writeEndObject();
	}

}
