package com.douguo.dc.util;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;

public final class JFreeChartUtil {
    public static void main(String[] args) {
        double[][] data = new double[][]{{1230, 1110, 1120, 2332, 234, 23423, 43534, 234}, {720, 750, 860, 23, 456, 57, 567, 435}, {830, 780, 700, 23, 5464, 2342, 566, 4523}};
        String[] rowKeys = {"下单数", "成交单数", "交易额"};
        String[] columnKeys = {"2015-05-01", "2015-05-02", "2015-05-03", "2015-05-04", "2015-05-05", "2015-05-06", "2015-05-07", "2015-05-08"};
        String chartTitle = "电商小时销量分布图";
        String categoryLabel = "时间";
        String valueLabel = "销量";
//		JFreeChartUtil.buildBarChat3D(chartTitle, categoryLabel, valueLabel, rowKeys, columnKeys, data);
        String chartPath = "/Users/zyz/Desktop/img-0.jpg";
        //JFreeChartUtil.buildBarChat(chartTitle, categoryLabel, valueLabel, rowKeys, columnKeys, data,chartPath);
//        JFreeChartUtil.buildLineChat(chartTitle, categoryLabel, valueLabel, rowKeys, columnKeys, data, chartPath);

        Map<String,Double> mapData = new HashMap<String, Double>();
        mapData.put("苹果", (double) 2323);
        mapData.put("栗子", (double) 2343);
        mapData.put("香蕉", (double) 254);
        mapData.put("蓝莓", (double) 2234);

        JFreeChartUtil.buildPieChat(chartTitle,  mapData, chartPath, 590, 500);
        System.out.println(BigDecimalUtil.format(new BigDecimal(1983837443), 0));
    }

    /**
     * 生成柱状图
     *
     * @param chartTitle    图标题
     * @param categoryLabel x轴标题
     * @param valueLabel    y轴标题
     * @param rowKeys
     * @param columnKeys
     * @param data
     */
    public static void buildBarChat3D(String chartTitle, String categoryLabel, String valueLabel, String[] rowKeys,
                                      String[] columnKeys, double[][] data) {

        CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);

        JFreeChart chart = ChartFactory.createBarChart(chartTitle, categoryLabel, valueLabel, dataset,
                PlotOrientation.HORIZONTAL, true, false, false);
        CategoryPlot plot = chart.getCategoryPlot();
        // 设置网格背景颜色‰
        // plot.setBackgroundPaint(Color.white);
        plot.setBackgroundPaint(plot.DEFAULT_GRIDLINE_PAINT);
        // 设置网格竖线颜色
        // plot.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        // plot.setRangeGridlinePaint(Color.pink);

        // 显示每个柱的数值，并修改该数值的字体属性
        BarRenderer3D renderer = new BarRenderer3D();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);

        // 默认的数字显示在柱子中，通过如下两句可调整数字的显示
        // 注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                TextAnchor.BASELINE_LEFT));
        renderer.setItemLabelAnchorOffset(10D);

        // 设置每个地区所包含的平行柱的之间距离
        renderer.setItemMargin(0.1);
        plot.setRenderer(renderer);

        // 设置地区、销量的显示位置
        // 将下方的“肉类”放到上方
        // plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
        // 将默认放在左边的“销量”放到右方
        // plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        File file = new File("/Users/zyz/Desktop/jfreechat001.jpg");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ChartUtilities.saveChartAsJPEG(file, chart, 700, 500);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void buildBarChat(String chartTitle, String categoryLabel, String valueLabel, String[] rowKeys,
                                    String[] columnKeys, double[][] data, String chartPath) {

        CategoryDataset datasetNew = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);

        JFreeChart chart = ChartFactory.createBarChart(chartTitle, categoryLabel, valueLabel, datasetNew, PlotOrientation.VERTICAL,
                true, false, false);

        CategoryPlot plot = chart.getCategoryPlot();
        // 设置网格背景颜色
        // plot.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        // plot.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        // plot.setRangeGridlinePaint(Color.pink);

        // 显示每个柱的数值，并修改该数值的字体属性
        BarRenderer renderer = new BarRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);

        // 默认的数字显示在柱子中，通过如下两句可调整数字的显示
        // 注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE3,
                TextAnchor.BASELINE_RIGHT));
        renderer.setItemLabelAnchorOffset(10D);
        renderer.setItemMargin(0.1);

        plot.setRenderer(renderer);

        //
        File file = new File(chartPath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ChartUtilities.saveChartAsJPEG(file, chart, 1500, 1000);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 自定义的颜色的柱形图
     * @param chartTitle
     * @param categoryLabel
     * @param valueLabel
     * @param rowKeys
     * @param columnKeys
     * @param data
     * @param chartPath
     */

    public static void buildBarChat2(String chartTitle, String categoryLabel, String valueLabel, String[] rowKeys,
                                    String[] columnKeys, double[][] data, String chartPath) {

        CategoryDataset datasetNew = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);

        //创建主题样式
        StandardChartTheme mChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        mChartTheme.setExtraLargeFont(new Font("黑体", Font.BOLD, 24));
        //设置轴向字体
        mChartTheme.setLargeFont(new Font("黑体", Font.PLAIN, 15));
        //设置图例字体
       mChartTheme.setRegularFont(new Font("黑体", Font.PLAIN, 15));
        //应用主题样式
        ChartFactory.setChartTheme(mChartTheme);

        JFreeChart chart = ChartFactory.createBarChart(chartTitle, categoryLabel, valueLabel, datasetNew, PlotOrientation.VERTICAL,
                true, false, false);

        CategoryPlot plot = chart.getCategoryPlot();
        // 设置网格背景颜色
         plot.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        // plot.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        // plot.setRangeGridlinePaint(Color.pink);

        //x轴
        CategoryAxis mDomainAxis = plot.getDomainAxis();
        //设置x轴标题的字体
        mDomainAxis.setLabelFont(new Font("黑体", Font.PLAIN, 18));
        //设置x轴坐标字体
        mDomainAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 16));
        //y轴
        ValueAxis mValueAxis = plot.getRangeAxis();
        //设置y轴标题字体
        mValueAxis.setLabelFont(new Font("黑体", Font.PLAIN, 18));
        //设置y轴坐标字体
        mValueAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 16));

        // 显示每个柱的数值，并修改该数值的字体属性
        //String[] colorValues = { "#19A15F", "#4C8BF5","#5A5AD6", "#55BFE8","#249029", "#6F716F","#08D1F0" };
        String[] colorValues = { "#19A15F", "#4C8BF5","#087112", "#2E6FAF","#5A5AD6", "#292798","#08D1F0" };
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        for (int i=0;i<rowKeys.length;i++){
            renderer.setSeriesPaint(i, Color.decode(colorValues[i]));
        }
       // renderer.setDrawBarOutline(false);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);

        // 默认的数字显示在柱子中，通过如下两句可调整数字的显示
        // 注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE3,
                TextAnchor.BASELINE_RIGHT));
        renderer.setItemLabelAnchorOffset(10D);
        renderer.setItemMargin(0.1);

        renderer.setShadowVisible(false); //不显示阴影
        //去掉渐变色
        renderer.setBarPainter( new StandardBarPainter() );
        renderer.setItemMargin(-0.01);

        plot.setRenderer(renderer);

        //
        File file = new File(chartPath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ChartUtilities.saveChartAsJPEG(file, chart, 1500, 1000);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * x轴字体倾斜45度
     * @param chartTitle
     * @param categoryLabel
     * @param valueLabel
     * @param rowKeys
     * @param columnKeys
     * @param data
     * @param chartPath
     */
    public static void buildBarChat3(String chartTitle, String categoryLabel, String valueLabel, String[] rowKeys,
                                     String[] columnKeys, double[][] data, String chartPath) {

        CategoryDataset datasetNew = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);

        //创建主题样式
        StandardChartTheme mChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        mChartTheme.setExtraLargeFont(new Font("黑体", Font.BOLD, 24));
        //设置轴向字体
        mChartTheme.setLargeFont(new Font("黑体", Font.PLAIN, 15));
        //设置图例字体
        mChartTheme.setRegularFont(new Font("黑体", Font.PLAIN, 15));
        //应用主题样式
        ChartFactory.setChartTheme(mChartTheme);

        JFreeChart chart = ChartFactory.createBarChart(chartTitle, categoryLabel, valueLabel, datasetNew, PlotOrientation.VERTICAL,
                true, false, false);

        CategoryPlot plot = chart.getCategoryPlot();
        // 设置网格背景颜色
        plot.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        // plot.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        // plot.setRangeGridlinePaint(Color.pink);

        //x轴
        CategoryAxis mDomainAxis = plot.getDomainAxis();
        //x轴字体倾斜45度
        mDomainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        //设置x轴标题的字体
        mDomainAxis.setLabelFont(new Font("黑体", Font.PLAIN, 18));
        //设置x轴坐标字体
        mDomainAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 16));
        //y轴
        ValueAxis mValueAxis = plot.getRangeAxis();
        //设置y轴标题字体
        mValueAxis.setLabelFont(new Font("黑体", Font.PLAIN, 18));
        //设置y轴坐标字体
        mValueAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 16));

        // 显示每个柱的数值，并修改该数值的字体属性
        //String[] colorValues = { "#19A15F", "#4C8BF5","#5A5AD6", "#55BFE8","#249029", "#6F716F","#08D1F0" };
        String[] colorValues = { "#19A15F", "#4C8BF5","#087112", "#2E6FAF","#5A5AD6", "#292798","#08D1F0" };
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        for (int i=0;i<rowKeys.length;i++){
            renderer.setSeriesPaint(i, Color.decode(colorValues[i]));
        }
        // renderer.setDrawBarOutline(false);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);

        // 默认的数字显示在柱子中，通过如下两句可调整数字的显示
        // 注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE3,
                TextAnchor.BASELINE_RIGHT));
        renderer.setItemLabelAnchorOffset(10D);
        renderer.setItemMargin(0.1);

        renderer.setShadowVisible(false); //不显示阴影
        //去掉渐变色
        renderer.setBarPainter( new StandardBarPainter() );
        renderer.setItemMargin(-0.01);

        plot.setRenderer(renderer);

        //
        File file = new File(chartPath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ChartUtilities.saveChartAsJPEG(file, chart, 1500, 1000);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void buildLineChat(String chartTitle, String categoryLabel, String valueLabel, String[] rowKeys,
                                     String[] columnKeys, double[][] data, String chartPath) {
        JFreeChartUtil.buildLineChat(chartTitle, categoryLabel, valueLabel, rowKeys, columnKeys, data, chartPath, 700, 500);
    }


    public static void buildLineChat(String chartTitle, String categoryLabel, String valueLabel, String[] rowKeys,
                                     String[] columnKeys, double[][] data, String chartPath, int width, int height) {

        CategoryDataset linedataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);

        JFreeChart chart = ChartFactory.createLineChart(chartTitle, categoryLabel, valueLabel, linedataset, PlotOrientation.VERTICAL, true, true, false);


        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(true);  //设置背景网格线是否可见
        plot.setDomainGridlinePaint(Color.BLACK); //设置背景网格线颜色
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setNoDataMessage("没有数据");//没有数据时显示的文字说明。



        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) plot.getRenderer();
        //设置曲线是否显示数据点
        lineandshaperenderer.setBaseShapesVisible(true);
        //设置曲线显示各数据点的值
        CategoryItemRenderer xyitem = plot.getRenderer();
        xyitem.setBaseItemLabelsVisible(true);
        xyitem.setBaseItemLabelFont(new Font("Dialog", 1, 14));
        plot.setRenderer(xyitem);
        ValueAxis rangeAxis = plot.getRangeAxis();
        //距离上边距边框距离
        rangeAxis.setUpperMargin(0.3);
        //距离下边框边距距离
        rangeAxis.setLowerMargin(0.5);
        CategoryAxis domainAxis = plot.getDomainAxis();
        // 左边距 边框距离
        domainAxis.setLowerMargin(0.05);
        // 右边距 边框距离,防止最后边的一个数据靠近了坐标轴。
        domainAxis.setUpperMargin(0.1);
        //rangeAxis.setAutoTickUnitSelection(true);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        //创建主题样式  
        StandardChartTheme standardChartTheme=new StandardChartTheme("CN");  
        //设置标题字体  
        standardChartTheme.setExtraLargeFont(new Font(chartPath,Font.BOLD,20));
        //设置图例的字体  
        standardChartTheme.setRegularFont(new Font(chartPath, Font.PLAIN,15));
        //设置轴向的字体  
        standardChartTheme.setLargeFont(new Font(chartPath,Font.PLAIN,15));
        //应用主题样式  
        ChartFactory.setChartTheme(standardChartTheme);



       //自定义颜色
        String[] colorValues = { "#C23531", "#2F4554","#61A0A8", "#D48265","#0A7648", "#D60AA7","#08D1F0" };
        for (int i=0;i<rowKeys.length;i++){
            lineandshaperenderer.setSeriesPaint(i, Color.decode(colorValues[i]));
        }




        //--------字符乱码   李常  开始------------
        Font font1 = new Font("宋体", Font.BOLD, 20); // 设定字体、类型、字号
        Font font2 = new Font("宋体", Font.PLAIN, 15);
        chart.getTitle().setFont(font1); // 标题
        chart.getLegend().setItemFont(font2);// 最下方
        chart.getCategoryPlot().getDomainAxis().setLabelFont(font2);// 相当于横轴或理解为X轴
        chart.getCategoryPlot().getRangeAxis().setLabelFont(font2);// 相当于竖轴理解为Y轴
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(font2);//横轴上的字
        chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);//解决字体模糊问题
        //----------------结束--------------------
        
        //
        File file = new File(chartPath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ChartUtilities.saveChartAsJPEG(file, chart, width, height);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 生成饼状图
     *
     * @param chartTitle
     * @param categoryLabel
     * @param valueLabel
     * @param rowKeys
     * @param columnKeys
     * @param data
     * @param chartPath
     * @param width
     * @param height
     */
    public static void buildPieChat3D(String chartTitle, String categoryLabel, String valueLabel, String[] rowKeys,
                                      String[] columnKeys, double[][] data, String chartPath, int width, int height) {

        // 创建饼图数据对象

        DefaultPieDataset dfp = new DefaultPieDataset();

        dfp.setValue("管理人员", 25);
        dfp.setValue("市场人员", 35);
        dfp.setValue("开发人员", 20);
        dfp.setValue("后勤人员", 5);
        dfp.setValue("财务人员", 15);

        // 饼状图的解决办法
        // createpieChart创建饼图
        JFreeChart chart = ChartFactory.createPieChart3D("CityInfoPort公司组织架构图", dfp, true, true, true);
        // 图片背景色
        chart.setBackgroundPaint(Color.red);
        // 设置标题文字
        ChartFrame frame = new ChartFrame("CityInfoPort公司组织架构图 ", chart, true);
        // 取得饼图plot对象
        PiePlot plot = (PiePlot) chart.getPlot();
        // 图形边框颜色
        plot.setBaseSectionPaint(Color.WHITE);
        // 图形边框粗细
        plot.setBaseSectionOutlineStroke(new BasicStroke(1.0f));

        // 指定图片的透明度(0.0-1.0)
        plot.setForegroundAlpha(0.65f);
        // 指定显示的饼图上圆形(false)还椭圆形(true)
        plot.setCircular(true);

        // 设置第一个 饼块section 的开始位置，默认是12点钟方向
        plot.setStartAngle(360);
        // 设置鼠标悬停提示
        plot.setToolTipGenerator(new StandardPieToolTipGenerator());

        // 设置突出显示的数据块
        plot.setExplodePercent("One", 0.1D);
        // 设置饼图各部分标签字体
        plot.setLabelFont(new Font("宋体", Font.ITALIC, 20));
        // 设置分饼颜色
        plot.setSectionPaint(0, new Color(244, 194, 144));
        // plot.setSectionPaint("2", new Color(144, 233, 144));
        // 设置图例说明Legend上的文字
        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 30));
        // 定义字体格式
        Font font = new java.awt.Font("黑体", java.awt.Font.CENTER_BASELINE, 50);
        TextTitle title = new TextTitle("项目状态分布");
        title.setFont(font);
        // 设置字体,非常关键不然会出现乱码的,下方的字体
        chart.setTitle(title);
        frame.pack();
        frame.setVisible(true);

        //
        File file = new File(chartPath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ChartUtilities.saveChartAsJPEG(file, chart, width, height);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 生成饼状图
     *
     * @param chartTitle
     * @param mapData
     * @param chartPath
     * @param width
     * @param height
     */
    public static void buildPieChat(String chartTitle, Map<String,Double> mapData , String chartPath, int width, int height) {


        //1.设置数据
        DefaultPieDataset pieDataset = new DefaultPieDataset();

        for(Map.Entry<String ,Double > entry : mapData.entrySet()){
            if(entry.getKey() != null){
                pieDataset.setValue(entry.getKey(), entry.getValue());
            }
        }

        //2. 用工厂类创建饼图
        JFreeChart pieChart = ChartFactory.createPieChart(chartTitle, pieDataset, true, true, false);
        // RenderingHints做文字渲染参数的修改
        // VALUE_TEXT_ANTIALIAS_OFF表示将文字的抗锯齿关闭.
//        pieChart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        //3. 得到饼图的Plot对象
        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        //
        //4. 设置扇区颜色
        //设置扇区分离显示
//        piePlot.setExplodePercent("篮球火", 0.2D);
        //设置扇区边框不可见
//        piePlot.setSectionOutlinesVisible(false);

        //5. 设置扇区标签显示格式：关键字：值(百分比)
        piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}：{1}({2})"));
        //设置扇区标签颜色
        piePlot.setLabelBackgroundPaint(new Color(220, 220, 220));
        piePlot.setLabelFont((new Font("宋体", Font.PLAIN, 12)));


        ///
        setNoDataMessage(piePlot);
        setNullAndZeroValue(piePlot);

        //
        File file = new File(chartPath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ChartUtilities.saveChartAsJPEG(file, pieChart, width, height);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 生成饼状图
     *
     * @param chartTitle
     * @param mapData
     * @param chartPath
     * @param width
     * @param height
     */
    public static void buildPieChat(String chartTitle, Map<String,Double> mapData , String chartPath, int width, int height,int titleFontSize,int itemFontSize) {


        //1.设置数据
        DefaultPieDataset pieDataset = new DefaultPieDataset();

        for(Map.Entry<String ,Double > entry : mapData.entrySet()){
            if(entry.getKey() != null){
                pieDataset.setValue(entry.getKey(), entry.getValue());
            }
        }

        //2. 用工厂类创建饼图
        JFreeChart pieChart = ChartFactory.createPieChart(chartTitle, pieDataset, true, true, false);

        // RenderingHints做文字渲染参数的修改
        // VALUE_TEXT_ANTIALIAS_OFF表示将文字的抗锯齿关闭.
        //pieChart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        //3. 得到饼图的Plot对象
        PiePlot piePlot = (PiePlot) pieChart.getPlot();

        //4. 设置扇区颜色
        setPieColor(piePlot,pieDataset);
        //设置扇区分离显示
//        piePlot.setExplodePercent("篮球火", 0.2D);
        //设置扇区边框不可见
//        piePlot.setSectionOutlinesVisible(false);

        //5. 设置扇区标签显示格式：关键字：值(百分比)
      //  piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}：{1}({2})"));
        piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}：({2})"));

        ///
        setNoDataMessage(piePlot);
        setNullAndZeroValue(piePlot);

        //设置背景颜色
        pieChart.setBackgroundPaint(Color.WHITE);
        piePlot.setBackgroundPaint(Color.WHITE);
        piePlot.setLabelBackgroundPaint(Color.WHITE);

        //设置扇区标签颜色
        piePlot.setLabelFont((new Font("宋体", Font.PLAIN, 14)));
        pieChart.getLegend().setItemFont(new Font("宋体",Font.BOLD,itemFontSize));
        pieChart.setTitle(new TextTitle(chartTitle,new Font("宋体",Font.BOLD,titleFontSize)));

        //
        File file = new File(chartPath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ChartUtilities.saveChartAsJPEG(file, pieChart, width, height);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    public static void setLabel(PiePlot pieplot) {
        //设置扇区标签显示格式：关键字：值(百分比)
        pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}：{1}({2} percent)"));
        //设置扇区标签颜色
        pieplot.setLabelBackgroundPaint(new Color(220, 220, 220));
        pieplot.setLabelFont((new Font("宋体", Font.PLAIN, 12)));
    }

    public static void setNoDataMessage(PiePlot pieplot) {
        //设置没有数据时显示的信息
        pieplot.setNoDataMessage("无数据");
        //设置没有数据时显示的信息的字体
        pieplot.setNoDataMessageFont(new Font("宋体", Font.BOLD, 14));
        //设置没有数据时显示的信息的颜色
        pieplot.setNoDataMessagePaint(Color.red);
    }

    public static void setNullAndZeroValue(PiePlot piePlot) {
        //设置是否忽略0和null值
        piePlot.setIgnoreNullValues(true);
        piePlot.setIgnoreZeroValues(true);
    }




    /**
     * 自定义饼图颜色
     * @param plot
     * @param dataset
     */
    private static void setPieColor(PiePlot plot,  DefaultPieDataset dataset){

            String[] colorValues = { "#93A9D0", "#4573A7", "#89A54E", "#71588F", "#4298AF", "#DB843D" ,"#AA4644",
                                      "#FFC8B4", "#99FF99", "#9900FF", "#008888", "#D2B48C", "#A9A9A9" ,"#E0FFFF",
                                       "#483D8B", "#00BFFF", "#FFA07A", "#FFB6C1", "#A52A2A", "#4400CC" ,"#990099"};

            //设置颜色

            Paint[] colors = new Paint[colorValues.length];
            for (int i = 0; i < colorValues.length; i++) {
                colors[i] = Color.decode(colorValues[i]);
            }

            //将颜色分配到每一块上
            for (int i = 0; i <dataset.getKeys().size(); i++) {
                plot.setSectionPaint(dataset.getKey(i), colors[i % colors.length]);
            }
    }


}