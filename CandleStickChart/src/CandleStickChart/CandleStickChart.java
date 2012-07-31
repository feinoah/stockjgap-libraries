package CandleStickChart;


import Functions.Functions;
import java.awt.Color;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import postgreswork.PostgresWork;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Krufe
 */
public class CandleStickChart extends ApplicationFrame {
    private JFreeChart jchart;
    private long start_time;
    private long end_time;
    private String ticker;
    private PostgresWork pWork;
    private int interval;
    
    public CandleStickChart(String ticker, String title, int interval)
    {
        super(title);
        
        this.ticker = ticker;
        this.interval = interval;
        this.pWork = PostgresWork.getPWorkObject(false);
    }
    
    public void setStartTime(long t)
    {
        this.start_time = t;
    }
    
    public void setEndTime(long t)
    {
        this.end_time = t;
    }
    
    public void render()
    {
        DateAxis    domainAxis       = new DateAxis("Date");
        NumberAxis  rangeAxis        = new NumberAxis("Price");
        CandlestickRenderer renderer = new CandlestickRenderer();
        XYDataset   dataset          = getDataSet();
        
        XYPlot mainPlot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);

        final long ONE_DAY = 2*390 *60 * 1000;
        XYLineAndShapeRenderer maRenderer = new XYLineAndShapeRenderer(true, false);
        XYDataset              maSataset  = MovingAverage.createMovingAverage(dataset, "MA", ONE_DAY, 0);
        mainPlot.setRenderer(1, maRenderer);
        mainPlot.setDataset (1, maSataset);
        
        //Do some setting up, see the API Doc
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setDrawVolume(true);
        rangeAxis.setAutoRangeIncludesZero(false);
        domainAxis.setTimeline( SegmentedTimeline.newMondayThroughFridayTimeline() );

        //Now create the chart and chart panel
        JFreeChart chart = new JFreeChart(this.ticker, null, mainPlot, true);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1200, 400));

        this.add(chartPanel);
        this.pack();
        this.setVisible(true);
    }
    
    protected AbstractXYDataset getDataSet()
    {
        //This is the dataset we are going to create
        DefaultOHLCDataset result;
        //This is the data needed for the dataset
        OHLCDataItem[] data;

        //This is where we go get the data, replace with your own data source
        data = getData();

        //Create a dataset, an Open, High, Low, Close dataset
        result = new DefaultOHLCDataset(this.ticker, data);

        return result;
    }
    
    //This method uses yahoo finance to get the OHLC data
    protected OHLCDataItem[] getData() {
        List<OHLCDataItem> dataItems = new ArrayList();
        
        String query = "SELECT interval_start, open, close, high, low, volume_for_interval FROM " + Functions.getTableName(this.interval) + " WHERE ticker = '" + this.ticker + "'";
        if(this.start_time > 0)
        {
            query = query + " AND interval_start >= " + this.start_time;
        }
        if(this.end_time > 0)
        {
            query = query + " AND interval_start <= " + this.end_time;
        }
        ArrayList res = this.pWork.runQuery(query);
        
        Iterator i = res.iterator();
        while(i.hasNext())
        {
            ArrayList r = (ArrayList) i.next();
            
            Date date = new Date(((Long)((ArrayList)r.get(0)).get(1)));
            double open = Double.parseDouble(((BigDecimal)((ArrayList)r.get(1)).get(1)).toString());
            double close = Double.parseDouble(((BigDecimal)((ArrayList)r.get(2)).get(1)).toString());
            double high = Double.parseDouble(((BigDecimal)((ArrayList)r.get(3)).get(1)).toString());
            double low = Double.parseDouble(((BigDecimal)((ArrayList)r.get(4)).get(1)).toString());
            double volume = Double.parseDouble(((Long)((ArrayList)r.get(5)).get(1)).toString());

            OHLCDataItem item = new OHLCDataItem(date, open, high, low, close, volume);
            dataItems.add(item);
        }
        
        //Convert the list into an array
        OHLCDataItem[] data = dataItems.toArray(new OHLCDataItem[dataItems.size()]);

        return data;
    }
}
