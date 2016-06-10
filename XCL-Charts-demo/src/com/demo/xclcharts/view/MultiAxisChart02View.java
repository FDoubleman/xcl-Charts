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

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.AxesChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.axis.CategoryAxis;
import org.xclcharts.renderer.axis.DataAxis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;

/**
 * @ClassName MultiAxisChart02View
 * @Description  柱形图与折线图的结合例子,主要演示右轴
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */

public class MultiAxisChart02View extends DemoView {

	private String TAG = "MultiAxisChart02View";
	private
	//标签轴
	List<String> chartLabels = new LinkedList<String>();
	List<BarData> chartData = new LinkedList<BarData>();
	
	//标签轴
	List<String> chartLabelsLn = new LinkedList<String>();
	LinkedList<LineData> chartDataLn = new LinkedList<LineData>();
	
	BarChart chart = new BarChart();
	LineChart lnChart = new LineChart();
	
	public MultiAxisChart02View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initChart();
	}
	
	/**
	 * 用于初始化
	 */
	private void initChart()
	{			
		chartLabels();
		chartDataLnSet();
		
		chartLnLabels();
		chartLnDataSet();	
		
		chartRender();
		chartLnRender();
		
		//綁定手势滑动事件
				this.bindTouch(this,chart);
				this.bindTouch(this,lnChart);
	}

	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //图所占范围大小
        chart.setChartRange(w,h);
        lnChart.setChartRange(w,h);
    }  	
	
	private void chartRender()
	{
		try {
						
			//设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....		
			int [] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
			//仅能横向移动
			chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);
			//禁止图标放缩
			chart.disableScale();
			//设置图标滑动模式
			chart.setChartDirection(XEnum.Direction.VERTICAL);	
			//标题  
			chart.setTitle("表格统计");
			chart.addSubtitle("(答题统计)");	
			//因为太长缩小标题字体
			//chart.getPlotTitle().getChartTitlePaint().setTextSize(20);
			//图例
			lnChart.getPlotLegend().hideBackground();
			//隐藏Key值    隐藏图例
			//chart.getPlotLegend().hide();
			
			//两个轴标题
			chart.getAxisTitle().setLeftTitle("占比(%)");
			chart.getAxisTitle().setRightTitle("正确率(%)");			
			
			//标签轴   第几题
			chart.setCategories(chartLabels);				
			//数据轴   每题的选项数据集合
			chart.setDataSource(chartData);
			chart.getDataAxis().setAxisMax(100);
			chart.getDataAxis().setAxisSteps(10);	
			//让柱子间没空白
			chart.getBar().setBarInnerMargin(0f);
			//指隔多少个轴刻度(即细刻度)后为主刻度
			//chart.getDataAxis().setDetailModeSteps(5);
			
			//定制数据轴上的标签格式
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){
	
				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub				
				
				    double label=Double.parseDouble(value);
					DecimalFormat df=new DecimalFormat("#0");
					return df.format(label).toString();
				}
				
			});
			
			//定制标签轴标签的标签格式
			CategoryAxis categoryAxis = chart.getCategoryAxis();
			categoryAxis.setTickLabelRotateAngle(-15f);			
			categoryAxis.getTickLabelPaint().setTextSize(15);
			categoryAxis.getTickLabelPaint().setTextAlign(Align.CENTER);
			
			categoryAxis.setLabelFormatter(new IFormatterTextCallBack(){
	
				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub				
					//String tmp = "c-["+value+"]";
					return value;
				}
				
			});
			//定制柱形顶上的标签格式
			chart.getBar().setItemLabelVisible(true);
			chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
				@Override
				public String doubleFormatter(Double value) {
					// TODO Auto-generated method stub
					// DecimalFormat df=new DecimalFormat("#0.00");
					DecimalFormat df=new DecimalFormat("#0");
					return df.format(value).toString();
				}});	
			
			//网格背景
			chart.getPlotGrid().showHorizontalLines();
			chart.getPlotGrid().showEvenRowBgColor();
			chart.getPlotGrid().showOddRowBgColor();
			chart.setBarCenterStyle(XEnum.BarCenterStyle.SPACE);
			//增加额外的宽度
			//chart.getPlotArea().extWidth(1000f);
			//chart.getPlotArea().extWidth(200f);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());			
		}
	}
	private void chartDataLnSet()
	{
		//标签1对应的柱形数据集
		List<Double> dataSeries1= new LinkedList<Double>();	
		//控制多少组数据
		dataSeries1.add(40d); 
		/*dataSeries1.add(73d); 
		dataSeries1.add(48d); 
		dataSeries1.add(40d); 
		dataSeries1.add(73d); 
		dataSeries1.add(48d); 
		dataSeries1.add(40d); 
		dataSeries1.add(73d); 
		dataSeries1.add(48d); */
		
		List<Double> dataSeries2= new LinkedList<Double>();	
		dataSeries2.add(60d); 
		/*dataSeries2.add(85d); 
		dataSeries2.add(90d); 
		dataSeries2.add(60d); 
		dataSeries2.add(85d); 
		dataSeries2.add(90d);
		dataSeries2.add(60d); 
		dataSeries2.add(85d); 
		dataSeries2.add(90d);*/
		
		List<Double> dataSeries3= new LinkedList<Double>();	
		dataSeries3.add(20d); 
		/*dataSeries3.add(40d); 
		dataSeries3.add(100d);
		dataSeries3.add(20d); 
		dataSeries3.add(40d); 
		dataSeries3.add(100d);
		dataSeries3.add(20d); 
		dataSeries3.add(40d); 
		dataSeries3.add(100d);*/
		
		List<Double> dataSeries4= new LinkedList<Double>();	
		dataSeries4.add(62d); 
		/*dataSeries4.add(54d); 
		dataSeries4.add(10d);
		dataSeries4.add(72d); 
		dataSeries4.add(49d); 
		dataSeries4.add(18d);
		dataSeries4.add(25d); 
		dataSeries4.add(34d); 
		dataSeries4.add(10d);*/

		BarData BarDataA = new BarData("A选项",dataSeries1,Color.rgb(0, 221, 177));	
		BarData BarDataB = new BarData("B选项",dataSeries2,Color.rgb(238, 28, 161));		
		BarData BarDataC = new BarData("C选项",dataSeries3,Color.rgb(128, 28, 161));
		BarData BarDataD = new BarData("D选项",dataSeries4,Color.rgb(28, 218, 61));
		
		chartData.add(BarDataA);
		chartData.add(BarDataB);
		chartData.add(BarDataC);
		chartData.add(BarDataD);
	}
	
	private void chartLabels()
	{
		
		chartLabels.add("第一题"); 
		/*chartLabels.add("第二题");
		chartLabels.add("第三题"); 
		chartLabels.add("第四题"); 
		chartLabels.add("第五题");
		chartLabels.add("第六题");
		chartLabels.add("第七题"); 
		chartLabels.add("第八题");
		chartLabels.add("第九题");*/
	
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
		//physical.add(0d); 
		physical.add(11d); 
		/*physical.add(25d); 
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
		//chartLabelsLn.add(" "); 
		chartLabelsLn.add("4 Cores Per Node"); 
		chartLabelsLn.add("8 Cores per Node");
		chartLabelsLn.add("4 Cores Per Node"); 
		chartLabelsLn.add("8 Cores per Node");
		chartLabelsLn.add("4 Cores Per Node"); 
		chartLabelsLn.add("8 Cores per Node");
		chartLabelsLn.add("4 Cores Per Node"); 
		chartLabelsLn.add("8 Cores per Node");
		chartLabelsLn.add("4 Cores Per Node"); 
	
		//chartLabelsLn.add(" "); 
	}
	
	private void chartLnRender()
	{
		try {
			//设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....		
			int [] ltrb = getBarLnDefaultSpadding();
			lnChart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);	
			//设置禁止放缩
			lnChart.disableScale();	
			
			//调整轴显示位置
			lnChart.setDataAxisLocation(XEnum.AxisLocation.RIGHT);
			lnChart.setCategoryAxisLocation(XEnum.AxisLocation.TOP);
			
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
			// TODO Auto-generated catch block
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
		dataAxis.setAxisSteps(10);			
		dataAxis.getAxisPaint().setColor(Color.rgb(51, 204, 204));	
		dataAxis.getTickMarksPaint().setColor(Color.rgb(51, 204, 204));
	
		//定制数据轴上的标签格式
		lnChart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){

			@Override
			public String textFormatter(String value) {
				// TODO Auto-generated method stub				
			
			    double label=Double.parseDouble(value);
				DecimalFormat df=new DecimalFormat("#0");
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
		//lnChart.getPlotArea().extWidth(1000f);
		//nChart.getPlotArea().extWidth(200f);
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
