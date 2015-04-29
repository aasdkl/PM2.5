package com.example.pm25.connectivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.pm25.model.ModelCallBackListener;
import com.example.pm25.util.MyLog;

/**
 * http连接的帮助类
 * @author Administrator
 *
 */
public final class ConnectHelper {
	
	/**
	 * 发送请求，返回json值，使用jsonTools包进行转换
	 * <b>注</b>：此处没有新开线程
	 * @param address api的url
	 * @return
	 */
	public static String sendRequest(final String address, ModelCallBackListener listener) {
		StringBuilder response = new StringBuilder();
		HttpURLConnection connection = null;
		try {
			URL url = new URL(address);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(8000);
			connection.setReadTimeout(8000);
			InputStream is = connection.getInputStream();
			//读取得到的输入流
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
		} catch (Exception e) {
			listener.onError(e);
			// 将返回值情空，设置为空字符串
			response.setLength(0);
			response.append("");
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return response.toString();
	}
}
