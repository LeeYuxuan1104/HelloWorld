package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.model.tool.MTDBHelper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


public class VDrawLineActivity extends Activity implements OnClickListener{
	/*内容*/
	private Context 	 	mContext; 
	/*控件*/
	private Button	 	 	vBack;
	private TextView 	 	vTopic,
							vResult;
	private Spinner  	 	vSignals;
	private SimpleAdapter	vAdapter; 
	private GetInfoReceiver	getInfoReceiver;
	private IntentFilter 	getInfoFilter;
	// 曲线内容;
	private LinearLayout vLayLine;
	/*控件内容*/
	// 用于存放每条折线的点数据
    private XYSeries 	 vLines;
    // 用于存放所有需要绘制的XYSeries
    private XYMultipleSeriesDataset vDataset;
    // 用于存放每条折线的风格
    private XYSeriesRenderer vRenderer;
    // 用于存放所有需要绘制的折线的风格
    private XYMultipleSeriesRenderer vXYMultipleSeriesRenderer;
    private GraphicalView vChart;
	/*参数的内容*/
    private String     	  pTargetFromService="com.from.service.to.activity";
    //	物理参数;
    private String 		  pname_chn,
    					  punit
    					  ;
	private String 	   	  name,
						  id,
						  stime="[时间]"
						  ;
	private double 		  min,
						  max;
	private int    	   	  column,
						  //选值的大小;
						  chooseposition
						  ;
	private double 		  choosevalue;
	private ArrayList<Map<String, String>> list;

	private ArrayList<Double> listReceives;
	
	/*自定义类*/
    private MTDBHelper 	  mHelper;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_drawline);
		initView();
		initEvent();
		//	物理参数初始化;
		pname_chn="";
		//	物理单位初始化;
		punit	 ="";
		min		 =0;
		max		 =100;
		initChart(pname_chn,punit,min,max);
	}

	/*控件初始化*/
	private void initView(){
		vBack	=(Button) findViewById(R.id.btnBack);
		vTopic	=(TextView) findViewById(R.id.tvTopic);
		vSignals=(Spinner) findViewById(R.id.spDrawLines);
		vResult =(TextView) findViewById(R.id.tvResult);
		vLayLine=(LinearLayout) findViewById(R.id.layRoot);
		
	}
	/*事件初始化*/
	private void initEvent(){
		mContext=VDrawLineActivity.this;
		vBack.setText(R.string.act_back);
		vTopic.setText(R.string.act_line);
		vBack.setOnClickListener(this);
		/**/
		getInfoReceiver = new GetInfoReceiver();
		getInfoFilter 	= new IntentFilter();
		//	数据进行初始化;
		listReceives	= new ArrayList<Double>();
		getInfoFilter.addAction(pTargetFromService);
		registerReceiver(getInfoReceiver, getInfoFilter);
		//
		Intent	intent	= getIntent();
		Bundle	bundle	= intent.getExtras();
		name		  	= bundle.getString("name");
		id			  	= bundle.getString("id");
		column		  	= bundle.getInt("column");
		/*数据信息的列表*/
		list			= loadSignals();
		vAdapter		= new SimpleAdapter(mContext, list, R.layout.act_item2, new String[]{"name"}, new int[]{R.id.content});
		vSignals.setAdapter(vAdapter);
		/*进行事件的选择*/	
		vSignals.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				listReceives	 =	new ArrayList<Double>();
				String 		tmp	 =	list.get(position).get("name");
				String		unit =	list.get(position).get("unit");
				String		smin =	list.get(position).get("min");
				String		smax =	list.get(position).get("max");
				punit			 =  unit;
				min				 =	Double.parseDouble(smin);
				max				 =  Double.parseDouble(smax);
				chooseposition	 =	position;
				pname_chn		 =	tmp;	
				vLayLine.removeView(vChart);
				initChart(pname_chn, punit,min,max);
				vChart.invalidate();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});	
	}
	/*数据加载*/
	private ArrayList<Map<String, String>> loadSignals(){
		ArrayList<Map<String, String>> list=	new ArrayList<Map<String,String>>();
		String 			  sql =	"select sg_flag,signal_name,unit,rank from can_signal where id="+id;
		mHelper				  = new MTDBHelper(mContext);
		ArrayList<String[]> datas=mHelper.query(sql);
		for(String[] items:datas){
			String sg_flag		=items[0];
			String signal_name	=items[1];
			String unit			=items[2];
			if(unit.equals("\"\"")){
				unit="";
			}
			
			String rank			=items[3];
			String minvalue		=rank.substring(0, rank.indexOf("|"));
			String maxvalue		=rank.substring(rank.indexOf("|")+1, rank.length());
			String name			=sg_flag+signal_name;
			Map<String, String> map=new HashMap<String, String>();
			map.put("name", name);
			map.put("unit", unit);
			map.put("min", minvalue);
			map.put("max", maxvalue);

			list.add(map);
		}
		mHelper.closedb();
		return list;
	}
	
	/*初始化信息*/
	private void initChart(String pname_chn,String unit,double min,double max) {       
		//	标线信息内容;
		//	物理名称;
        vLines 	  				  = new XYSeries(pname_chn);
        vRenderer 				  = new XYSeriesRenderer();
        vDataset  				  = new XYMultipleSeriesDataset();
        vXYMultipleSeriesRenderer = new XYMultipleSeriesRenderer();

        //	初始化划线——对XYSeries和XYSeriesRenderer的对象的参数赋值
        loadLines(vLines);
        //	初始化图表内容;
        initRenderer(vRenderer, Color.GREEN, PointStyle.CIRCLE, true);

        //将XYSeries对象和XYSeriesRenderer对象分别添加到XYMultipleSeriesDataset对象和XYMultipleSeriesRenderer对象中。
        vDataset.addSeries(vLines);
        vXYMultipleSeriesRenderer.addSeriesRenderer(vRenderer);
        //配置chart参数
        setChartSettings(vXYMultipleSeriesRenderer, stime, unit, 0, 10, min, max, Color.RED,Color.WHITE);

        //通过该函数获取到一个View 对象
        vChart 						= ChartFactory.getLineChartView(this, vDataset, vXYMultipleSeriesRenderer);
        
        //将该View 对象添加到layout中。
        vLayLine.addView(vChart, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        
    }

    private void loadLines(XYSeries series) {
        // 随机生成两组数据
    	int nSize=listReceives.size();
    	
    	if(nSize>10){
    		listReceives.remove(0);
    	}
    	
    	nSize=listReceives.size();
    	
    	for(int i=0;i<nSize;i++){
    		double d=listReceives.get(i);
    		series.add(i, d);
    	}

    }

    private XYSeriesRenderer initRenderer(XYSeriesRenderer renderer, int color,
            PointStyle style, boolean fill) {
        // 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        renderer.setColor(color);
        renderer.setPointStyle(style);
        renderer.setFillPoints(fill);
        renderer.setLineWidth(1);
        return renderer;
    }
    /*图表的设置*/
    protected void setChartSettings(XYMultipleSeriesRenderer mXYMultipleSeriesRenderer,
            String xTitle, String yTitle, double xMin, double xMax,
            double yMin, double yMax, int axesColor, int labelsColor) {
        // 有关对图表的渲染可参看api文档
        mXYMultipleSeriesRenderer.setXTitle(xTitle);
        mXYMultipleSeriesRenderer.setYTitle(yTitle);
        mXYMultipleSeriesRenderer.setXAxisMin(xMin);
        mXYMultipleSeriesRenderer.setAxisTitleTextSize(30);
        mXYMultipleSeriesRenderer.setChartTitleTextSize(50);
        mXYMultipleSeriesRenderer.setLabelsTextSize(15);
        mXYMultipleSeriesRenderer.setXAxisMax(xMax);
        mXYMultipleSeriesRenderer.setYAxisMin(yMin);
        mXYMultipleSeriesRenderer.setYAxisMax(yMax);
        mXYMultipleSeriesRenderer.setAxesColor(axesColor);
        mXYMultipleSeriesRenderer.setLabelsColor(labelsColor);
        mXYMultipleSeriesRenderer.setShowGrid(true);
        mXYMultipleSeriesRenderer.setGridColor(Color.GRAY);
        mXYMultipleSeriesRenderer.setXLabels(20);
        mXYMultipleSeriesRenderer.setYLabels(10);
        mXYMultipleSeriesRenderer.setXTitle("");
        mXYMultipleSeriesRenderer.setYLabelsAlign(Align.RIGHT);
        mXYMultipleSeriesRenderer.setPointSize((float) 5);
        mXYMultipleSeriesRenderer.setShowLegend(true);
        mXYMultipleSeriesRenderer.setLegendTextSize(20);
    }

	@Override
	public void onClick(View view) {
		int vId=view.getId();
		switch (vId) {
		case R.id.btnBack:
			if(getInfoReceiver!=null){			
				unregisterReceiver(getInfoReceiver);
				getInfoReceiver=null;
			}
			finish();
			break;
			
		default:
			break;
		}
		
	}
	
	
	
	private String loadData(String id,int ncolumn){
		String sql="select * from signalinfo where id="+id+" order by time desc limit 0,"+column;
		mHelper=new MTDBHelper(mContext);
		ArrayList<String[]> datainfos=mHelper.query(sql);
	
		String   tmp  = "  "+name+"\r\n";
		String[] datas= datainfos.get(column-1-chooseposition);
		choosevalue	  = Double.parseDouble(datas[2]);
		listReceives.add(choosevalue);
		int nCount=1;
		
		for(int i=column-1;i>=0;i--){
			String [] items=datainfos.get(i);
			String name =items[1];
			String value=items[2];
			String unit =items[3];
			if(unit.equals("\"\"")){
				unit="";
			}
			tmp+="  "+name+":"+value+unit+"  ";
			if(nCount%4==0){
				tmp+="\r\n";
			}
			nCount++;
		}
		mHelper.closedb();
		return tmp;
	}
	/*广播机制*/
	//	接收器;
	//	数据进行注册;
	public class GetInfoReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent){
			Bundle  bundle=intent.getExtras();			
			int		pCount=bundle.getInt("count");
			//	接收信息的内容;
			if(pCount!=0){
				String str=loadData(id, column);
				vResult.setText(str);
				vLayLine.removeView(vChart);
				initChart(pname_chn,punit,min,max);
				vChart.invalidate();
			}	
		}
	}
	
}
