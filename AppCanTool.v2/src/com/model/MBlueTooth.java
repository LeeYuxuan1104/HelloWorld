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
		//	蓝牙适配器的初始化;
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		//	数据列表信息初始化;
		this.listInfos=new ArrayList<String>();
	}
	public BluetoothAdapter getmBluetoothAdapter() {
		return mBluetoothAdapter;
	}
	
	//	具备蓝牙设备;
	public boolean hasBlueToothDevice(){
		if(mBluetoothAdapter==null) 
			return false ;
		else return true;
	}
	//	蓝牙设备开关;
	public boolean isBlueToothOpen(){
		if(mBluetoothAdapter.isEnabled())
			return true;
		else return false;
	}
	//	使蓝牙设备关闭;
	public void setBlueToothClose(){
		mBluetoothAdapter.disable();
	}
	//	获取设备信息列表;
	public ArrayList<Map<String, String>> getListDevices() {
		listDevices = new ArrayList<Map<String,String>>();
		// 获取所有已经绑定的蓝牙设备
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
	//	获取设备接收信息列表;
	public ArrayList<String> getListInfos(){
		
		return listInfos;
	}
	public ArrayList<String> getListInfosClear(){
		listInfos.clear();
		return listInfos;
	}
	
}
