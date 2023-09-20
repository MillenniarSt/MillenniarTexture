package io.github.MillenniarSt.MillenniarTexture.Console;

import java.io.Serializable;

/*           //////////////////////////////////
*           //								    //
*           //    //          //                //
*           //    ////      ////    //////      //
*           //	  //  //  //  //  //            //
*           //	  //    //    //  //            //
*           //	  //          //    //////      //
*           //    //          //          //	//
*           //    //          //          //    //
*           //    //          //  ////////      //
*           //                                  //
*             /////// Millenniar Studios ///////
*/

public class ConsoleLine implements Serializable {
	
	private static final long serialVersionUID = 2L;
	
	public static final String DEBUG = "ffffff";
	public static final String SUCCESS = "1a8f1e";
	public static final String INFO = "2d7ebc";
	public static final String WARN = "d2ab26";
	public static final String ERROR = "cf6310";
	public static final String FATAL = "c10c0c";
	public static final String CRASH = "8c2d40";
	
	private String location;
	private String text;
	private String state;
	private Exception exception;
	
	public ConsoleLine(String location, String text, String state) {
		this.location = location;
		this.text = text;
		this.state = state;
	}
	
	public ConsoleLine(String location, String text, String state, Exception exception) {
		this.location = location;
		this.text = text;
		this.state = state;
		this.exception = exception;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
}
