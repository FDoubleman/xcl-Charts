/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.demo.xclcharts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.axis.CategoryAxis;
import org.xclcharts.renderer.axis.DataAxis;

import com.demo.xclcharts.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * @ClassName MultiAxisChart02View
 * @Description  柱形图与折线图的结合例子,主要演示右轴
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */

public class MultiAxisChart01View extends DemoView {

	private String TAG = "MultiAxisChart02View";
	private AccountChartBean mAccountChartBean;
	private Context mContext;
	private int mExtWidthNum;
	private
	//标签轴
	List<String> chartLabels = new LinkedList<String>();
	List<BarData> chartData = new LinkedList<BarData>();
	
	//标签轴
	List<String> chartLabelsLn = new LinkedList<String>();
	LinkedList<LineData> chartDataLn = new LinkedList<LineData>();
	
	BarChart chart = new BarChart();
	LineChart lnChart = new LineChart();

	//private AccountChartsPresenter mAccountChartsPresenter;
	private ArrayList<Integer> mColorList;


	public MultiAxisChart01View(Context context) {
		super(context);
		mContext=context;
		initChart();
	}
	public MultiAxisChart01View(Context context,AccountChartBean accountChartBean) {
		super(context);
		mContext=context;
		this.mAccountChartBean =accountChartBean;
		mExtWidthNum = Integer.parseInt(accountChartBean.getTopicnum());
		int leng = mAccountChartBean.getTopiclist().size();
		initChart();
	}
	public MultiAxisChart01View(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initChart();
	}

	public MultiAxisChart01View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initChart();
	}
	/**
	 * 用于初始化
	 */
	private void initChart()
	{
		//initColor();
		initDate();
		chartLabels();
		chartDataLnSet();

		chartLnLabels();
		chartLnDataSet();

		chartRender();
		chartLnRender();

		//綁定手势滑动事件
		this.bindTouch(this, chart);
		this.bindTouch(this, lnChart);

	}

	private void initColor() {
		mColorList = new ArrayList<Integer>();
		Integer integer = Color.rgb(0, 255, 68);
		Integer integer2 = Color.rgb(0, 255, 255);
		Integer integer3 = Color.rgb(172, 255, 0);
		Integer integer4 = Color.rgb(148, 0, 211);
		Integer integer5 = Color.rgb(139, 0, 139);
		Integer integer6 = Color.rgb(144, 235, 144);
		Integer integer7 = Color.rgb(255, 62, 150);
		mColorList.add(integer);
		mColorList.add(integer2);
		mColorList.add(integer3);
		mColorList.add(integer4);
		mColorList.add(integer5);
		mColorList.add(integer6);
		mColorList.add(integer7);

	}

	//初始化获得 图表数据
	public void initDate(){
		//每道题各个选项占比详情 集合
		ArrayList<OptionRateBean> optionRateBeans = new ArrayList<OptionRateBean>();
		//每道题详情 集合
		ArrayList<TopicDetailsBean> topicDetailsBeans = new ArrayList<TopicDetailsBean>();
		//获得底部标签数据
		String [] mSelect = getResources().getStringArray(R.array.select_array);

		// 根据最多选项个数    创建 试题选项占比 的数据
		OptionRateBean optionRateBeanA = new OptionRateBean("A","0");
		OptionRateBean optionRateBeanB = new OptionRateBean("B","0");
		OptionRateBean optionRateBeanC = new OptionRateBean("C","1");
		OptionRateBean optionRateBeanD = new OptionRateBean("D","35");
		
		//添加数据
		optionRateBeans.add(optionRateBeanA);
		optionRateBeans.add(optionRateBeanB);
		optionRateBeans.add(optionRateBeanC);
		optionRateBeans.add(optionRateBeanD);


		for(int i = 0;i<1;i++ ){
			TopicDetailsBean topicDetailsBean =  new TopicDetailsBean("试题ID"+i,"30",optionRateBeans);
			topicDetailsBeans.add(topicDetailsBean);
		}
		//获得的网络数据
		mAccountChartBean = new AccountChartBean("a,b,c,d","9",topicDetailsBeans);

	}

	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //图所占范围大小
        chart.setChartRange(w, h);
        lnChart.setChartRange(w, h);
    }  	
	
	private void chartRender()
	{
		try {
			//设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....		
			int [] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(DisplayUtil.dip2px(mContext,105), ltrb[1], DisplayUtil.dip2px(mContext,105),ltrb[3]);
			//仅能横向移动
			chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);
			//禁止图标放缩
			chart.disableScale();
			//设置图标滑动模式
			chart.setChartDirection(XEnum.Direction.VERTICAL);	
			//标题  
			chart.setTitle(" ");
			chart.addSubtitle("客观题正确率及选项分布");
			//因为太长缩小标题字体
			//chart.getPlotTitle().getChartTitlePaint().setTextSize(20);
			//图例
			lnChart.getPlotLegend().hideBackground();
			//隐藏Key值    隐藏图例
			//chart.getPlotLegend().hide();
			
			//两个轴标题
			chart.getAxisTitle().setLeftTitle("选项占比( % )");
			chart.getAxisTitle().setRightTitle("正确率( % )");
			//数据量大  提高性能
			chart.disableHighPrecision();

			//标签轴   第几题
			chart.setCategories(chartLabels);				
			//数据轴   每题的选项数据集合
			chart.setDataSource(chartData);
			chart.getDataAxis().setAxisMax(100);
			chart.getDataAxis().setAxisSteps(20);
			//让柱子间没空白
			chart.getBar().setBarInnerMargin(0f);
			//指隔多少个轴刻度(即细刻度)后为主刻度
			//chart.getDataAxis().setDetailModeSteps(5);

			//定制数据轴上的标签格式
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){
	
				@Override
				public String textFormatter(String value) {
				    double label=Double.parseDouble(value);
					DecimalFormat df=new DecimalFormat("#0");
					return df.format(label).toString();
				}
				
			});
			
			//定制标签轴标签的标签格式
			CategoryAxis categoryAxis = chart.getCategoryAxis();
			//标签轴  文字倾斜 -15度
			//categoryAxis.setTickLabelRotateAngle(-15f);
			categoryAxis.getTickLabelPaint().setTextSize(18);
			categoryAxis.getTickLabelPaint().setTextAlign(Align.CENTER);
			
			categoryAxis.setLabelFormatter(new IFormatterTextCallBack(){
	
				@Override
				public String textFormatter(String value) {
					//String tmp = "c-["+value+"]";
					return value;
				}
				
			});
			//定制柱形顶上的标签格式
			chart.getBar().setItemLabelVisible(true);
			chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
				@Override
				public String doubleFormatter(Double value) {
					 DecimalFormat df=new DecimalFormat("#0.00");
					//DecimalFormat df=new DecimalFormat("#0");
					 if(value==1){
						 value=0.0;
					 }
					return df.format(value).toString();
				}});	
			
			//网格背景
			chart.getPlotGrid().showHorizontalLines();
			chart.getPlotGrid().showEvenRowBgColor();
			chart.getPlotGrid().showOddRowBgColor();
			chart.setBarCenterStyle(XEnum.BarCenterStyle.SPACE);
			//增加额外的宽度
			if(mExtWidthNum>8){
				chart.getPlotArea().extWidth((mExtWidthNum-8) *20f);
			}else {
				chart.getPlotArea().extWidth(200f);
			}


		} catch (Exception e) {
			Log.e(TAG, e.toString());			
		}
	}
	//对柱状图 数据设置
	public void chartDataLnSet()
	{
		//存放各个选项所对应的值
		List<Double> dataSeriesA = new LinkedList<Double>();
		List<Double>dataSeriesB = new LinkedList<Double>();
		List<Double>dataSeriesC = new LinkedList<Double>();
		List<Double>dataSeriesD = new LinkedList<Double>();
		List<Double>dataSeriesE = new LinkedList<Double>();
		List<Double>dataSeriesF = new LinkedList<Double>();
		List<Double> dataSeriesG = new LinkedList<Double>();
		//存放个选项集合
		ArrayList<List<Double>> mSelectLists =new ArrayList<List<Double>>();
		//获得图例指示数据
		String [] selectTable = getResources().getStringArray(R.array.select_array);

		ArrayList<TopicDetailsBean> topicDetailsBeans = mAccountChartBean.getTopiclist();//试题列表
		//遍历试题列表
		for(int x =0;x<topicDetailsBeans.size();x++){
//			获得 题目对应的正确率---》折线图数据
			topicDetailsBeans.get(x).getAccuracy();
			//获得 题目对应的 选项占比列表
			ArrayList<OptionRateBean> optionRateBeans = topicDetailsBeans.get(x).getOptionRightRateList();
			//获得每题的选项个数  即所对应的值
			int optNum = optionRateBeans.size();//选项个数

			for(int y = 0;y<optNum;y++){
				double optratD = Double.parseDouble(optionRateBeans.get(y).getOptionRatio());

				switch (y){
					case 0:
						//分装A选项的集合
						dataSeriesA.add(optratD);
						break;
					case 1:
						//分装B选项的集合
 						dataSeriesB.add(optratD);
						break;
					case 2:
						//分装C选项的集合
						dataSeriesC.add(optratD);
						break;
					case 3:
						//分装D选项的集合
						dataSeriesD.add(optratD);
						break;
					case 4:
						//分装E选项的集合
						dataSeriesE.add(optratD);
						break;
					case 5:
						//分装F选项的集合
						dataSeriesF.add(optratD);
						break;
					case 6:
						//分装G选项的集合
						dataSeriesG.add(optratD);
						break;
				}
			}

		}
		//将选项列表
		mSelectLists.add(dataSeriesA);
		mSelectLists.add(dataSeriesB);
		mSelectLists.add(dataSeriesC);
		mSelectLists.add(dataSeriesD);
		mSelectLists.add(dataSeriesE);
		mSelectLists.add(dataSeriesF);
		mSelectLists.add(dataSeriesG);

		Log.d(TAG, "A数据集合长度：" + dataSeriesA.size());
		Log.d(TAG, "B数据集合长度：" + dataSeriesB.size());

		int mostOptionNum = mAccountChartBean.getMostOptionNum();//最多选项
		//根据最多选项创建每列的 bar


		for(int i= 0 ;i<mostOptionNum;i++){
			Random random=new Random();
			//BarData BarData = new BarData(selectTable[i],mSelectLists.get(i),mColorList.get(i));
			BarData BarData = new BarData(selectTable[i],mSelectLists.get(i),Color.rgb(random.nextInt(240), random.nextInt(240), random.nextInt(240)));
			//BarData BarData = new BarData(selectTable[i],mSelectLists.get(i),Color.rgb(0, 221, 177));
			chartData.add(BarData);
		}



		/*BarData BarDataA = new BarData("A选项",dataSeriesA,Color.rgb(0, 221, 177));
		BarData BarDataB = new BarData("B选项",dataSeriesB,Color.rgb(238, 28, 161));
		BarData BarDataC = new BarData("C选项",dataSeriesC,Color.rgb(128, 28, 161));
		BarData BarDataD = new BarData("D选项",dataSeriesD,Color.rgb(28, 218, 61));

		chartData.add(BarDataA);
		chartData.add(BarDataB);
		chartData.add(BarDataC);
		chartData.add(BarDataD);*/


		/*//标签1对应的柱形数据集
		List<Double> dataSeries1= new LinkedList<Double>();	
		//控制多少组数据
		dataSeries1.add(40d); 
		dataSeries1.add(73d); 
		dataSeries1.add(48d); 
		dataSeries1.add(40d); 
		dataSeries1.add(73d); 
		dataSeries1.add(48d); 
		dataSeries1.add(40d); 
		dataSeries1.add(73d); 
		dataSeries1.add(48d); 
		
		List<Double> dataSeries2= new LinkedList<Double>();	
		dataSeries2.add(60d); 
		dataSeries2.add(85d); 
		dataSeries2.add(90d); 
		dataSeries2.add(60d); 
		dataSeries2.add(85d); 
		dataSeries2.add(90d);
		dataSeries2.add(60d); 
		dataSeries2.add(85d); 
		dataSeries2.add(90d);
		
		List<Double> dataSeries3= new LinkedList<Double>();	
		dataSeries3.add(20d); 
		dataSeries3.add(40d); 
		dataSeries3.add(100d);
		dataSeries3.add(20d); 
		dataSeries3.add(40d); 
		dataSeries3.add(100d);
		dataSeries3.add(20d); 
		dataSeries3.add(40d); 
		dataSeries3.add(100d);
		
		List<Double> dataSeries4= new LinkedList<Double>();	
		dataSeries4.add(62d); 
		dataSeries4.add(54d); 
		dataSeries4.add(10d);
		dataSeries4.add(72d); 
		dataSeries4.add(49d); 
		dataSeries4.add(18d);
		dataSeries4.add(25d); 
		dataSeries4.add(34d); 
		dataSeries4.add(10d);

		BarData BarDataA = new BarData("A选项",dataSeries1,Color.rgb(0, 221, 177));	
		BarData BarDataB = new BarData("B选项",dataSeries2,Color.rgb(238, 28, 161));		
		BarData BarDataC = new BarData("C选项",dataSeries3,Color.rgb(128, 28, 161));
		BarData BarDataD = new BarData("D选项",dataSeries4,Color.rgb(28, 218, 61));
		
		chartData.add(BarDataA);
		chartData.add(BarDataB);
		chartData.add(BarDataC);
		chartData.add(BarDataD);*/
	}
	
	private void chartLabels()
	{
		//获得试题列表集合
		ArrayList<TopicDetailsBean>	topicDetailsBeans = mAccountChartBean.getTopiclist();
		for(int i = 1;i<= topicDetailsBeans.size();i++){
			chartLabels.add("第"+i+"题");
		}

	}
	
	
	private void chartLnDataSet()
	{
		//三角折线    标签1对应的数据集
		LinkedList<Double> virtual= new LinkedList<Double>();	
		//virtual.add(0d); 			
		virtual.add(95d); 
		virtual.add(100d); 
		virtual.add(80d); 
		
		//圆形折线    标签2对应的数据集  折线图数据 集合
		LinkedList<Double> physical= new LinkedList<Double>();
		for(int i=0 ;i<mAccountChartBean.getTopiclist().size();i++){
		double accuracy =Double.parseDouble(mAccountChartBean.getTopiclist().get(i).getAccuracy());
			if(accuracy==0){
				accuracy=1;
			}
			physical.add(accuracy);
		}

		//physical.add(0d); 
		/*physical.add(11d);
		physical.add(25d); 
		physical.add(40d); 
		physical.add(11d); 
		physical.add(25d); 
		physical.add(40d);
		physical.add(11d); 
		physical.add(25d); 
		physical.add(40d);*/
				
		
		//将标签与对应的数据集分别绑定
		LineData lineData1 = new LineData("Virtual RT",virtual,Color.rgb(234, 83, 71));
//		LineData lineData2 = new LineData("Physical RT",physical,Color.rgb(75, 166, 51));
		
		LineData lineData2 = new LineData("准确率",physical,Color.rgb(75, 166, 51));
		lineData2.setDotRadius(5);
		lineData2.getLinePaint().setStrokeWidth(4f);
		lineData1.setDotStyle(XEnum.DotStyle.TRIANGLE);

		lineData1.getDotPaint().setColor(Color.rgb(234, 83, 71));

		
		LinkedList<Double> BarKey1= new LinkedList<Double>();				
		BarKey1.add(0d); 
		LinkedList<Double> BarKey2= new LinkedList<Double>();				
		BarKey2.add(0d); 
		
		//LineData lineDataBarKey1 = new LineData("Virtual OPM",BarKey1,Color.rgb(0, 221, 177));
		LineData lineDataBarKey2 = new LineData("准确率",BarKey2,Color.rgb(238, 28, 161));
		
		//lineDataBarKey1.setDotStyle(XEnum.DotStyle.RECT);
		lineDataBarKey2.setDotStyle(XEnum.DotStyle.RECT);
		
			
		//chartDataLn.add(lineDataBarKey1);
		chartDataLn.add(lineDataBarKey2);	
		//chartDataLn.add(lineData1);
		chartDataLn.add(lineData2);	
	}
	
	
	private void chartLnLabels()
	{
		int chartLabeNum = mAccountChartBean.getTopiclist().size();
		for(int i =0;i<chartLabeNum;i++){
			chartLabelsLn.add("正确率");
		}

	}
	
	private void chartLnRender()
	{
		try {
			//设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....		
			int [] ltrb = getBarLnDefaultSpadding();
			lnChart.setPadding(DisplayUtil.dip2px(mContext, 105), ltrb[1], DisplayUtil.dip2px(mContext, 105), ltrb[3]);
			//设置禁止放缩
			lnChart.disableScale();

			//调整轴显示位置
			lnChart.setDataAxisLocation(XEnum.AxisLocation.RIGHT);
			lnChart.setCategoryAxisLocation(XEnum.AxisLocation.TOP);
			//数据量大  提高性能
			lnChart.disableHighPrecision();
			lnChart.setDataSource(chartDataLn);
			renderLnAxis();	
			//指隔多少个轴刻度(即细刻度)后为主刻度
			//lnChart.getDataAxis().setDetailModeSteps(5);
			//仅能水平滑动
			lnChart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);
			//图例  显示   和位置的控制
			/*lnChart.getPlotLegend().show();
			lnChart.getPlotLegend().setType(XEnum.LegendType.COLUMN);
			lnChart.getPlotLegend().setVerticalAlign(XEnum.VerticalAlign.TOP);
			lnChart.getPlotLegend().setHorizontalAlign(XEnum.HorizontalAlign.LEFT);
			lnChart.getPlotLegend().hideBackground();*/
			//隐藏图例
			lnChart.getPlotLegend().hide();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}
	
	/**
	 * 折线图轴相关
	 */
	private void renderLnAxis()
	{
		//标签轴
		lnChart.setCategories(chartLabelsLn);		
		lnChart.getCategoryAxis().hide();	
		
		//设定数据源						
		lnChart.setDataSource(chartDataLn);
		//数据轴
		lnChart.setDataAxisLocation(XEnum.AxisLocation.RIGHT);		
		DataAxis dataAxis = lnChart.getDataAxis();		
		dataAxis.setAxisMax(100);
		dataAxis.setAxisMin(0);
		dataAxis.setAxisSteps(20);
		dataAxis.getAxisPaint().setColor(Color.rgb(51, 204, 204));	
		dataAxis.getTickMarksPaint().setColor(Color.rgb(51, 204, 204));
	
		//定制数据轴上的标签格式
		lnChart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {

			@Override
			public String textFormatter(String value) {
				double label = Double.parseDouble(value);
				DecimalFormat df = new DecimalFormat("#0");
				return df.format(label).toString();
			}

		});

		//允许线与轴交叉时，线会断开
		lnChart.setLineAxisIntersectVisible(false);
		
		//调整右轴显示风格
		lnChart.getDataAxis().setHorizontalTickAlign(Align.RIGHT);
		lnChart.getDataAxis().getTickLabelPaint().setTextAlign(Align.LEFT);
		
		lnChart.setXCoordFirstTickmarksBegin(true);
		//		
		lnChart.setBarCenterStyle(XEnum.BarCenterStyle.SPACE);
		//增加额外的宽度
		if(mExtWidthNum>8){
			lnChart.getPlotArea().extWidth((mExtWidthNum-8) *20f);
		}else {
			lnChart.getPlotArea().extWidth(200f);
		}
		//lnChart.getPlotArea().extWidth(200f);
	}
	
	@Override
	protected int[] getBarLnDefaultSpadding()
	{
		int [] ltrb = new int[4];
		ltrb[0] = DensityUtil.dip2px(getContext(), 40); //left	
		ltrb[1] = DensityUtil.dip2px(getContext(), 56); //top
		ltrb[2] = DensityUtil.dip2px(getContext(), 40); //right	
		ltrb[3] = DensityUtil.dip2px(getContext(), 36); //bottom							
		return ltrb;
	}
	
	@Override
    public void render(Canvas canvas) {
        try{
        	chart.render(canvas);
        	lnChart.render(canvas);
        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }

	
}
