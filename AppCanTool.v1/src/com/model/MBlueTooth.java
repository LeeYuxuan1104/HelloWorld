package com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class MBlueTooth {
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayList<Map<String, String>> listDevices;
	private ArrayList<String> listInfos;
	
	public MBlueTooth() {
		//	�����������ĳ�ʼ��;
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		//	�����б���Ϣ��ʼ��;
		this.listInfos=new ArrayList<String>();
	}
	public BluetoothAdapter getmBluetoothAdapter() {
		return mBluetoothAdapter;
	}
	
	//	�߱������豸;
	public boolean hasBlueToothDevice(){
		if(mBluetoothAdapter==null) 
			return false ;
		else return true;
	}
	//	�����豸����;
	public boolean isBlueToothOpen(){
		if(mBluetoothAdapter.isEnabled())
			return true;
		else return false;
	}
	//	ʹ�����豸�ر�;
	public void setBlueToothClose(){
		mBluetoothAdapter.disable();
	}
	//	��ȡ�豸��Ϣ�б�;
	public ArrayList<Map<String, String>> getListDevices() {
		listDevices = new ArrayList<Map<String,String>>();
		// ��ȡ�����Ѿ��󶨵������豸
		Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
		if (devices.size() > 0) {
			for (BluetoothDevice device : devices) {
				Map<String, String> map=new HashMap<String, String>();
				map.put("name", device.getName());
				map.put("address", device.getAddress());
				listDevices.add(map);
			}
		}
		return listDevices;
	}
	public ArrayList<Map<String, String>> getListDevicesClear() {
		listDevices.clear();
		return listDevices;
	}
	//	��ȡ�豸������Ϣ�б�;
	public ArrayList<String> getListInfos(){
		
		return listInfos;
	}
	public ArrayList<String> getListInfosClear(){
		listInfos.clear();
		return listInfos;
	}
	
}
