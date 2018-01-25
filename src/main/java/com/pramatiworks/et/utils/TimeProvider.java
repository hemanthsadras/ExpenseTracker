package com.pramatiworks.et.utils;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class TimeProvider implements Serializable
{
	private static final long serialVersionUID = 2629878500822877269L;
	
	public Date now()
	{
		return new Date();
	}

}
