package com.model.entity;

public class MEInfo {
	private int _id;
	private String name;
	private double value;
	private String unit;
	private String note;
	private int id;
	private long time;
	public MEInfo(String name, double value, String unit, String note,
			int id, long time) {
		super();
		this.name = name;
		this.value = value;
		this.unit = unit;
		this.note = note;
		this.id = id;
		this.time = time;
	}
	public MEInfo(int _id, String name, double value, String unit, String note,
			int id, long time) {
		super();
		this._id = _id;
		this.name = name;
		this.value = value;
		this.unit = unit;
		this.note = note;
		this.id = id;
		this.time = time;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
