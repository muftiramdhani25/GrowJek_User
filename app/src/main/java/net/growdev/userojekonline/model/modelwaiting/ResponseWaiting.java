package net.growdev.userojekonline.model.modelwaiting;

import com.google.gson.annotations.SerializedName;

public class ResponseWaiting{

	@SerializedName("result")
	private boolean result;

	@SerializedName("msg")
	private String msg;

	@SerializedName("driver")
	private String driver;

	public void setResult(boolean result){
		this.result = result;
	}

	public boolean isResult(){
		return result;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setDriver(String driver){
		this.driver = driver;
	}

	public String getDriver(){
		return driver;
	}
}