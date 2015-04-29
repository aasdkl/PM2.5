package com.example.pm25.model;

import java.util.List;

public interface ModelCallBackListener<T> {

	public void onFinish(final List<T> cities);

	public void onError(final Exception e);

}
