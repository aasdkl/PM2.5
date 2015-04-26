package com.example.fm25.util;

public enum PM25APIs {
	CITY("http://www.pm25.in/api/querys.json"),
	;
	
	// PM2.5的测试token
	private static final String TOKEN = "5j1znBVAsnSf5xQyNQyq";
	// url的基本地址
	private String url;
	// 用于缓存用户需要的url
	private StringBuilder temp;
	
	private PM25APIs(String url) {
		this.url=url+"?token="+TOKEN;
		temp=new StringBuilder(this.url);
	}
	
	/**
	 * 获取最终生成的url
	 * @return
	 */
	@Override
	public String toString() {
		String result = temp.toString();
		temp.setLength(0);//删除temp中旧的数据
		temp.append(this.url);
		return result;
	}
	
	/**
	 * 添加城市参数
	 * @param city
	 * @return 添加参数后的对象，允许链式调用
	 */
	public PM25APIs addCity(String city) {
		addPara("city",city);
		return this;
	}

	/**
	 * 增加参数，当前存储的API为：<br>
	 * xxx.json?token=xxx
	 * @param name
	 * @param value 
	 */
	private void addPara(String name, String value) {
		temp.append("&").append(name).append("=").append(value);
	}
}
