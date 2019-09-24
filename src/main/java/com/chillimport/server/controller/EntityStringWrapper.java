package com.chillimport.server.controller;

import com.chillimport.server.entities.Entity;
import com.chillimport.server.entities.Location;

/**
 * This class is needed to transfer two classes in a RequestBody
 * See: ThingController.create() ...   
 */
public class EntityStringWrapper<T extends Entity>{
	private T entity;
	private String string;
	
	public EntityStringWrapper() {
		
	}
	
	public EntityStringWrapper(T entity, String string) {
		this.entity = entity;
		this.string = string;
	}
	 
	public void setString(String string) {
		this.string = string; 
	}
	
	public void setEntity(T entity) {
		this.entity = entity;
	}
	
	public String getString() {
		return this.string;
	}
	
	public T getEntity() {
		return this.entity;
	}
}
