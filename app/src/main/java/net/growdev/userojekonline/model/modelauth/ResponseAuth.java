package net.growdev.userojekonline.model.modelauth;

import com.google.gson.annotations.SerializedName;

public class ResponseAuth{

	@SerializedName("result")
	private boolean result;

	@SerializedName("msg")
	private String msg;

	@SerializedName("idUser")
	private String idUser;

	@SerializedName("data")
	private Data data;

	@SerializedName("token")
	private String token;

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

	public void setIdUser(String idUser){
		this.idUser = idUser;
	}

	public String getIdUser(){
		return idUser;
	}

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}
}