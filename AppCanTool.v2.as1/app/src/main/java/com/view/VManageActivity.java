package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model.entity.Element;
import com.model.tool.MTDBHelper;
import com.model.tool.TreeViewAdapter;
import com.model.tool.TreeViewItemClickListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class VManageActivity extends Activity implements OnClickListener {
    /*上下文声明*/
    private Context mContext;
    private Intent mIntent;
    private SimpleAdapter mAdapter;
    private MTDBHelper helper;
    /*控件的声明*/
    private TextView vTopic;
    private Button vBack;
    private ListView vlvShow;

    private ArrayList<Element> elements;

    private ArrayList<Element> elementsData;


    /*参量的声明*/
//	private ArrayList<Map<String, String>> list;
    /*定义类声明*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_vmanage);
        initView();
        initEvent();
    }

    //	初始化控件;
    private void initView() {
        vBack = (Button) findViewById(R.id.btnBack);
        vTopic = (TextView) findViewById(R.id.tvTopic);
        vlvShow = (ListView) findViewById(R.id.lvShow);
    }

    //	初始化方法;
    private void initEvent() {
        //	上下文初始化;
        mContext = VManageActivity.this;
        vTopic.setText(R.string.tip_manage);
        vBack.setText(R.string.act_back);
        //	下方按钮添加事件监听;
        vBack.setOnClickListener(this);
        helper = new MTDBHelper(mContext);
        init();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        TreeViewAdapter treeViewAdapter = new TreeViewAdapter(
                elements, elementsData, inflater);
        TreeViewItemClickListener treeViewItemClickListener = new TreeViewItemClickListener(treeViewAdapter);
        vlvShow.setAdapter(treeViewAdapter);
        vlvShow.setOnItemClickListener(treeViewItemClickListener);

    }

    private void init() {
        elements = new ArrayList<Element>();
        elementsData = new ArrayList<Element>();
        String sql = "select * from can_message";
        ArrayList<String[]> datasMessage = helper.query(sql);
        for (String[] items : datasMessage) {
            String bo_flag = items[1];
            String id = items[2];
            String message_name = items[3];
            String dlc = items[4];
            String node_name = items[5];
            String topic = bo_flag + id + "_" + message_name + " " + dlc + " " + node_name;
            Element e = new Element(topic, Element.TOP_LEVEL, Integer.parseInt(id), Element.NO_PARENT, true, false);
            elements.add(e);
            elementsData.add(e);
        }
        sql = "select * from can_signal";
        ArrayList<String[]> datasSignal = helper.query(sql);
        for (String[] items : datasSignal) {

            int _id = Integer.parseInt(items[0]);
            String sg_flag = items[1];
            String signal_name = items[2];
//			String way=items[3];
//			String judge=items[4];
//			String rank=items[5];
//			String unit=items[6];
//			String node_name=items[7];

            int id = Integer.parseInt(items[8]);

            String topic = sg_flag + " " + signal_name;
            Element e = new Element(topic, Element.TOP_LEVEL + 1, _id, id, false, false);
            elementsData.add(e);
        }

    }

    @Override
    public void onClick(View view) {
        int pId = view.getId();
        switch (pId) {
            case R.id.btnBack:
                finish();
                break;
            default:
                break;
        }
    }
//	private ArrayList<Map<String, String>> loadData(){
//		ArrayList<Map<String, String>> list=new ArrayList<Map<String,String>>();
//		String sql="select * from signalinfo";
//		ArrayList<String[]> datas=helper.query(sql);
//		for(String[] items:datas){
//			Map<String, String> map=new HashMap<String, String>();
//			map.put("_id", items[0]);
//			map.put("name", items[1]);
//			map.put("value", items[2]);
//			map.put("unit", items[3]);
//			map.put("note", items[4]);
//			map.put("id", items[5]);
//			map.put("time", items[6]);
//			list.add(map);
//		}
//		return list;
//	}
}
