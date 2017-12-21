package com.douguo.uprofile.mobile.model;

import java.io.Serializable;

public class MobileModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9087072999174133785L;
	
	private int id;
	private String devi;
	private String brand;
	private String model;
	private String modelName;
	private String consumeLevel ;
	private String modelDesc ;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDevi() {
		return devi;
	}
	public void setDevi(String devi) {
		this.devi = devi;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getConsumeLevel() {
		return consumeLevel;
	}
	public void setConsumeLevel(String consumeLevel) {
		this.consumeLevel = consumeLevel;
	}
	public String getModelDesc() {
		return modelDesc;
	}
	public void setModelDesc(String modelDesc) {
		this.modelDesc = modelDesc;
	}
	
	
}