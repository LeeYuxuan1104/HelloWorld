package com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class MBlueTooth {
	//	设备状态;
	private BluetoothAdapter bluetoothAdapter;
	private boolean fdevice;
	//	蓝牙状态;
	private boolean fbtooth;
	//	蓝牙属性;
	private String  name;
	private String  address;
	private String  UUID;
	private String  ConnName;
	private ArrayList<Map<String, String>> listDevices;
	
	
	//////////////////////////////////////////////////////////////////////////////
	// 01.构造函数;
	public MBlueTooth() {
		this.bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		this.fdevice=hasBlueToothAdapter();
	}
	// 01.1重载方法;
	public MBlueTooth(String name,String address){
		this.name=name;
		this.address=address;
	}
	
	public BluetoothAdapter getBluetoothAdapter() {
		return bluetoothAdapter;
	}
	public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
		this.bluetoothAdapter = bluetoothAdapter;
	}
	////////////////////////////////////////////////////////////////////////////////
	//	02.蓝牙设备存在?
	private boolean hasBlueToothAdapter(){
		if(bluetoothAdapter!=null){
			return true;
		}else return false;
	}
	//////////////////////////////////////////////////////////////////////////////
	//	03.设备存在;
	public boolean isFdevice() {
		return fdevice;
	}
	//////////////////////////////////////////////////////////////////////////////
	//	04.蓝牙开启?
	public boolean isFbtooth() {
		fbtooth=bluetoothAdapter.isEnabled();
		return fbtooth;
	}
	//////////////////////////////////////////////////////////////////////////////
	//	05.设置蓝牙;
	public void setBlueToothOpen(){
		bluetoothAdapter.enable();
	}
	public void setBlueToothClose(){
		bluetoothAdapter.disable();
	}
	//////////////////////////////////////////////////////////////////////////////
	//	06.获得蓝牙属性;
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	@SuppressLint("NewApi")
	public ArrayList<Map<String, String>> getListDevices() {
		listDevices = new ArrayList<Map<String,String>>();
		// 获取所有已经绑定的蓝牙设备
		Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
		int nCount=0;
		if (devices.size() > 0) {
			for (BluetoothDevice device : devices) {
				Map<String, String> map=new HashMap<String, String>();
				map.put("name", device.getName());
				map.put("address", device.getAddress());
				listDevices.add(map);
				nCount++;
				if(nCount==1){
					break;
				}
			}
		}
		return listDevices;
	}
	//////////////////////////////////////////////////////////////////////////////
	//	07.UUID
	public String getUUID() {
		UUID="ffffffff-ea31-5a0f-0617-ee160033c587";
		return UUID;
	}
	public String getConnName() {
		ConnName="Bluetooth_Socket";
		return ConnName;
	}
}
