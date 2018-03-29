package org.achartengine.chartdemo.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.chartdemo.demo.chart.AbstractDemoChart;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

public class Chart extends AbstractDemoChart {
  public static final int[] COLOR = new int[] { Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW };
  // 36, 29.78/-10, 15, 39
  public static final int[] BASE = new int[] { 36, 39, 15, 39};
  private static final String[] TITLE = new String[] { "抑郁", "焦虑", "幸福", "孤独"};
  public int base = 14;
  public String getName() {
    return "picture";
  }

  public String getDesc() {
    return "The average temperature in 4 Greek islands (line chart)";
  }

  public View execute(Context context, int num, List<Double> list, List<String[]> timeList) {
    String[] titles = new String[] { TITLE[num], "基准线" };
   /* x轴设置
    List<double[]> x = new ArrayList<double[]>();
    double[] xdou = new double[list.size()];
    double[] xbase = new double[base + list.size()];
    System.out.println(list.size());
    for (int j = 0; j < list.size(); j++) {
    			xdou[j] = j + 1;
    }
    for (int j = 0; j < base + list.size(); j++) {
    	xbase[j] = j;
    }
    x.add(xdou);
    x.add(xbase);
    
    
    List<double[]> values = new ArrayList<double[]>();
	double[] dou = new double[list.size()];
	double[] line = new double[list.size() + base];
	for (int i = 0; i < list.size(); i++) {
		dou[i] = list.get(i);
	}
	for (int i = 0; i < base + list.size(); i++) {
		line[i] = BASE[num];
	}
	values.add(dou);
	values.add(line);
	*/
    
    List<Date[]> dates = new ArrayList<Date[]>();
    int len = titles.length;
    for (int i = 0; i < len; i++) {
      dates.add(new Date[list.size()]);
      for(int j = 0; j < timeList.size(); j++ ){
    	  System.out.println("timelist.get(j)[0]" + timeList.get(j)[0]);
    	  int year = Integer.valueOf(timeList.get(j)[0]) - 1900;
    	  int month = Integer.valueOf(timeList.get(j)[1]) - 1;
    	  int day = Integer.valueOf(timeList.get(j)[2]);
    	  dates.get(i)[j] = new Date(year, month, day);
      }
    }
    List<double[]> values = new ArrayList<double[]>();
	double[] dou = new double[list.size()];
	double[] line = new double[list.size()];
	for (int i = 0; i < list.size(); i++) {
		dou[i] = list.get(i);
	}
	for (int i = 0; i < list.size(); i++) {
		line[i] = BASE[num];
	}
	values.add(dou);
	values.add(line);
	
	
	
    int[] colors = new int[] {COLOR[num], Color.RED};
    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.POINT};
//    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
//    		PointStyle.TRIANGLE, PointStyle.SQUARE };
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    int length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
  /*  int length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
      seriesRenderer.setDisplayChartValues(true);
    }*/
    setChartSettings(renderer, " ", "次数", "分值", 0.5, 12.5, 0, 80,
        Color.LTGRAY, Color.LTGRAY);
   // setChartSettings(renderer, " ", "时间", "分值", dates.get(0)[0].getTime(), dates.get(0)[timeList.size()-1].getTime(), 0, 80,
   // Color.LTGRAY, Color.LTGRAY);
    //System.out.println("dates.get(0)[timeList.size()-1]:" + dates.get(0)[timeList.size()-1]);
    //System.out.println("dates.get(0)[25]:" + dates.get(0)[25]);
   // System.out.println("dates.get(0)[timeList.size()-1]:" + dates.get(0)[timeList.size()-1].getTime());
    renderer.setXLabels(5);
    renderer.setYLabels(10);
    renderer.setPointSize((float)3);
    renderer.setShowGrid(true);
//    renderer.setShowLegend(false);
    renderer.setZoomButtonsVisible(true);
    renderer.setAxisTitleTextSize(18); //设置轴标签文本大小
    renderer.setLegendTextSize(15);  // 设置图例文本大小
/*  renderer.setXLabels(12);
    renderer.setYLabels(10);
    renderer.setShowGrid(true);
    renderer.setXLabelsAlign(Align.RIGHT);
    renderer.setYLabelsAlign(Align.CENTER);
    renderer.setZoomButtonsVisible(true);
    */
    
//    renderer.setPanLimits(new double[] { -10, 100, -10, 100 }); 
//    renderer.setZoomLimits(new double[] {  -100, 200, -100, 400 });
//    Intent intent = ChartFactory.getLineChartIntent(context, buildDataset(titles, x, values),
//        renderer, "psy");
    /*View view = ChartFactory.getLineChartView(context, buildDataset(titles, x, values), renderer);*/
    View view = ChartFactory.getTimeChartView(context, buildDateDataset(titles, dates, values), renderer, "MM/dd/yyyy");
    return view;
  }

@Override
public View execute(Context context, int num, List<Double> list) {
	// TODO Auto-generated method stub
	return null;
}

/*renderer.setBackgroundColor(Color.GRAY);// 背景颜色
 renderer.setLabelsTextSize(25);// 标签文字大小
 renderer.setChartTitle("");// 图表名称
 renderer.setChartTitleTextSize(30);// 图表名称大小
 renderer.setLegendTextSize(30);// 图标字体大小
 renderer.setLegendHeight(100);// 图标文字距离底边的高度
 renderer.setPanEnabled(false);//图表是否可以移动
 renderer.setZoomEnabled(true);//图表是否可以缩放
     renderer.getSeriesRendererAt(0).setDisplayChartValues(true);//设置柱子上是否显示数量值
    renderer.getSeriesRendererAt(1).setDisplayChartValues(true);//设置柱子上是否显示数量值
    renderer.setXLabels(12);//X轴的近似坐标数
    renderer.setYLabels(5);//Y轴的近似坐标数
    renderer.setXLabelsAlign(Align.LEFT);//刻度线与X轴坐标文字左侧对齐
    renderer.setYLabelsAlign(Align.LEFT);//Y轴与Y轴坐标文字左对齐
    renderer.setPanEnabled(true, false);//允许左右拖动,但不允许上下拖动.
    // renderer.setZoomEnabled(false);
    renderer.setZoomRate(1.1f);//放大的倍率
    renderer.setBarSpacing(0.5f);//柱子间宽度
*/
}

