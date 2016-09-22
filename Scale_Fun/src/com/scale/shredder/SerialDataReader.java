package com.scale.shredder;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialDataReader {
	public SerialDataReader(String comPort) { 
		setComPort(comPort);
	}
	public int readWeightMeasurement() { 
		SerialPort serialPort = new SerialPort(getComPort());
        int measurement = -1;
	    try {
	        serialPort.openPort();                //Open serial port
	        serialPort.setParams(9600, 8, 1, 0);  //Set parameters
	        boolean readSerial = true;
	        while ( readSerial ) {
	        	String measurementReceived = serialPort.readHexString();
				if  ( measurementReceived != null) {
					/* Alexiou Protocol:
					 * 02 ==> START BYTE
					 * 03 ==> END BYTE
					 */
	            	if ( measurementReceived.length() == 32 && measurementReceived.substring(0,2).equalsIgnoreCase("02") && measurementReceived.substring(measurementReceived.length()-2).equalsIgnoreCase("03") ) {
	            		ResourceBundle rb = ResourceBundle.getBundle("com.scale.bundle.conn");	
	            		String allDigits = measurementReceived.substring(6,23);
	            		String measurementStr = getMeasurement(allDigits);
	            		measurement = new Integer(measurementStr).intValue();
	            		
	            		// Check if load measured is more than zero:
	            		if ( measurement > 0 ) {
	            			readSerial = true;	
	            		}
	            		
	            	}
				}
	        }
	        serialPort.closePort();
	    }
	    catch (SerialPortException ex) {
	    	ex.printStackTrace();
	    }
        return measurement;
	}
	private void setComPort(String comPort) {
		this.comPort = comPort;
	}
	private String getComPort() {
		return this.comPort;
	}
	public String getMeasurement(String alldigits) {
		String[] digitTuple = alldigits.trim().split(" ");
		StringBuilder sb = new StringBuilder();
		for ( int i = 0 ; i < digitTuple.length ; i++) {
    		byte[] digitTupleBytes = fromHexString(digitTuple[i]);
      		String digit = null;
			try {
				digit = new String(digitTupleBytes ,"ISO8859-7");
				try {
					new Integer(digit).intValue();
					sb.append(""+digit);
				}
				catch (NumberFormatException e) {
					//e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}			
		}
		return sb.toString();
	}
	public byte[] fromHexString(final String encoded) {
		if ((encoded.length() % 2 ) != 0 ) {
			 throw new IllegalArgumentException("Input string must contain an even number of characters");
		}
		final byte result[]= new byte[encoded.length()/2];
		final char enc[] = encoded.toCharArray();
		for ( int i = 0 ; i < enc.length ; i +=2) {
			StringBuilder curr = new StringBuilder(2);
			curr.append(enc[i]).append(enc[i+1]);
			result[i/2] = (byte) Integer.parseInt(curr.toString(),16);
		}
		return result;
	}
	private String comPort;
}