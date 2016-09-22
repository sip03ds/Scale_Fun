package com.scale.shredder;

import java.util.ResourceBundle;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class WeightRecorder {
	public static void main(String args[]) throws InterruptedException {

		ResourceBundle rb = ResourceBundle.getBundle("gr.anamet.bundle.conn");

		final GpioController gpio = GpioFactory.getInstance();

		// Silo 1
		final GpioPinDigitalInput doorInput1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);
		final GpioPinDigitalOutput doorOutput1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02,rb.getString("door1"), PinState.HIGH);
		final GpioPinDigitalOutput doorLed1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22,rb.getString("door1"), PinState.LOW);
		final GpioPinDigitalOutput buttonLed1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28,rb.getString("door1"), PinState.LOW);
		doorOutput1.setShutdownOptions(true, PinState.LOW);
		doorOutput1.low();
		doorInput1.addListener(new DoorListener( rb.getString("host") , new Integer(rb.getString("port")).intValue() ,  rb.getString("path") , doorOutput1 , rb.getString("source1") , rb.getString("serial1") , doorLed1 , buttonLed1 )); // String host , int port ,  String path , GpioPinDigitalOutput output, String source , String serial

		// Silo 2
		final GpioPinDigitalInput doorInput2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);
		final GpioPinDigitalOutput doorOutput2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06,rb.getString("door2"), PinState.HIGH);
		final GpioPinDigitalOutput doorLed2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23,rb.getString("door2"), PinState.LOW);
		final GpioPinDigitalOutput buttonLed2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29,rb.getString("door2"), PinState.LOW); 
		doorOutput2.setShutdownOptions(true, PinState.LOW);
		doorOutput2.low();
		doorInput2.addListener(new DoorListener( rb.getString("host") , new Integer(rb.getString("port")).intValue() ,  rb.getString("path") , doorOutput1 , rb.getString("source2") , rb.getString("serial2") , doorLed2 , buttonLed2 ));

		// Logger ...
		System.out.println(" ... complete the GPIO #00 or GPIO #04 circuit !!!");

		// keep program running until user aborts (CTRL-C)
		for (;;) {
			Thread.sleep(500);
		}
	}
}
