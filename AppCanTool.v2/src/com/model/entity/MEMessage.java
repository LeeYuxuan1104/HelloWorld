package com.model.entity;

public class MEMessage {
	private int _id;
	private String bo_flag;
	private int id;
	private String message_name;
	private int dlc;
	private String node_name;

	public MEMessage() {
	
	}

	public MEMessage(int _id, String bo_flag, int id, String message_name,
			int dlc, String node_name) {
		super();
		this._id = _id;
		this.bo_flag = bo_flag;
		this.id = id;
		this.message_name = message_name;
		this.dlc = dlc;
		this.node_name = node_name;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getBo_flag() {
		return bo_flag;
	}

	public void setBo_flag(String bo_flag) {
		this.bo_flag = bo_flag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage_name() {
		return message_name;
	}

	public void setMessage_name(String message_name) {
		this.message_name = message_name;
	}

	public int getDlc() {
		return dlc;
	}

	public void setDlc(int dlc) {
		this.dlc = dlc;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

	
}
