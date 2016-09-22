package com.scale.shredder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MeasurementDispatcher {
	public MeasurementDispatcher(String hostname, String path, int port) {
		this.setPath(path);
		this.setHostname(hostname);
		this.setPort(port);
	}

	public boolean recordMeasurement(int measurement, String source) {

		boolean haveNotRecordedMeasurement = true;
		
		String url = "http://" + getHostname() + ":" + getPort()+ "/"+getPath()+"?measurement=" + measurement+ "&source=" + source;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in;

			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}	
			in.close();
			
			// Xreiazetai validation gia to success //
			System.out.println(response.toString());
			
			haveNotRecordedMeasurement = false;
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return haveNotRecordedMeasurement ;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private int port;
	private String hostname;
	private String path;
}