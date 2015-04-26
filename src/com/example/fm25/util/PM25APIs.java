package com.example.fm25.util;

public enum PM25APIs {
	CITY("http://www.pm25.in/api/querys.json")
	;
	
	private static final String TOKEN = "5j1znBVAsnSf5xQyNQyq";
	private String url;
	
	private PM25APIs(String url) {
		this.url = url+"?token="+TOKEN;
	}
	
}
