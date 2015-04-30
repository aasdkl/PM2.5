package com.example.pm25.model;

import java.util.List;

/**
 * 获取数据时所开线程的回调函数
 * @author Administrator
 * @param <T>
 */
public interface ModelCallBackListener<T> {

	/**
	 * 结束后返回值的处理
	 * @param cities
	 */
	public void onFinish(final List<T> objs);

	/**
	 * 抛出的异常处理
	 * @param e
	 */
	public void onError(final Exception e);

}
