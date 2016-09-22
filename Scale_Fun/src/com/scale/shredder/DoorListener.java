package com.scale.shredder;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class DoorListener implements GpioPinListenerDigital {
	DoorListener(String host , int port ,  String path , GpioPinDigitalOutput output1,  String source , String serial , GpioPinDigitalOutput led , GpioPinDigitalOutput button) {
		this.setSerial(serial);
		this.setHost(host);
		this.setPort(port);
		this.setPath(path);
		this.setSource(source);
		this.setOutput(output);
		this.setLed(led);
		this.setButton(button);
	}
	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		System.out.println(" Event from: : " + event.getPin() + " = " + event.getState()); 
		
		if ( event.getState() == event.getState().HIGH ) {
			System.out.println("Button Pressed");
			
			getLed().high();
			getButton().high();
			
			//getOutput().high();			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//getOutput().low();

			// Step 2 - READ SERIAL DATA
			System.out.println("Read data");
			SerialDataReader sdr = new SerialDataReader( getSerial() );
			int measurement = sdr.readWeightMeasurement();					
			System.out.println(measurement);
			// Step 3 - SEND DATA TO SERVER 
			System.out.println("Send data");
			MeasurementDispatcher disp = new MeasurementDispatcher( getHost() , getPath() , getPort() );
			if ( disp.recordMeasurement(measurement, getSource()  ) ) {
				System.out.println("Open the door");
				//getOutput().high();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				getOutput().low();
			}	
			getLed().low();
			getButton().low();
		}		
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public GpioPinDigitalOutput getOutput() {
		return output;
	}
	public void setOutput(GpioPinDigitalOutput output) {
		this.output = output;
	}
	public GpioPinDigitalOutput getLed() {
		return led;
	}
	public void setLed(GpioPinDigitalOutput led) {
		this.led = led;
	}
	public GpioPinDigitalOutput getButton() {
		return button;
	}
	public void setButton(GpioPinDigitalOutput button) {
		this.button = button;
	}
	private String serial;
	private String host;
	private int port;
	private String path;
	private String source;
	private GpioPinDigitalOutput output;
	private GpioPinDigitalOutput led;
	private GpioPinDigitalOutput button;
}