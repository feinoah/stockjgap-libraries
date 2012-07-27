package CandleStickChart;


import Functions.Functions;
import java.awt.Color;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.*;
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

        //Do some setting up, see the API Doc
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setDrawVolume(false);
        rangeAxis.setAutoRangeIncludesZero(false);
        domainAxis.setTimeline( SegmentedTimeline.newMondayThroughFridayTimeline() );

        //Now create the chart and chart panel
        JFreeChart chart = new JFreeChart(this.ticker, null, mainPlot, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 300));

        this.add(chartPanel);
        this.pack();
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
        List<OHLCDataItem> dataItems = new ArrayList<>();
        
        String query = "SELECT interval_start, open, close, high, low, volume_for_interval FROM " + Functions.getTableName(this.interval) + " WHERE ticker = '" + this.ticker + "' AND (interval_start >= " + this.start_time + " AND interval_end <= " + this.end_time + ")";
        System.out.println(query);
        ArrayList res = this.pWork.runQuery(query);
        
        Iterator i = res.iterator();
        while(i.hasNext())
        {
            ArrayList r = (ArrayList) i.next();
            
            Iterator item1 = r.iterator();
            
            while(item1.hasNext())
            {
                Date date = new Date(((Long)((ArrayList)r.get(0)).get(1)));
                double open = Double.parseDouble(((BigDecimal)((ArrayList)r.get(1)).get(1)).toString());
                double close = Double.parseDouble(((BigDecimal)((ArrayList)r.get(2)).get(1)).toString());
                double high = Double.parseDouble(((BigDecimal)((ArrayList)r.get(3)).get(1)).toString());
                double low = Double.parseDouble(((BigDecimal)((ArrayList)r.get(4)).get(1)).toString());
                double volume = Double.parseDouble(((Long)((ArrayList)r.get(5)).get(1)).toString());
                
                OHLCDataItem item = new OHLCDataItem(date, open, high, low, close, volume);
                dataItems.add(item);
                System.out.println("HERE");
            }
            
        }
/*        try {
            String strUrl= "http://ichart.finance.yahoo.com/table.csv?s="+stockSymbol+"&a=0&b=1&c=2008&d=3&e=30&f=2008&ignore=.csv";
            URL url = new URL(strUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            DateFormat df = new SimpleDateFormat("y-M-d");

            String inputLine;
            in.readLine();
            while ((inputLine = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(inputLine, ",");

                Date date       = df.parse( st.nextToken() );
                double open     = Double.parseDouble( st.nextToken() );
                double high     = Double.parseDouble( st.nextToken() );
                double low      = Double.parseDouble( st.nextToken() );
                double close    = Double.parseDouble( st.nextToken() );
                double volume   = Double.parseDouble( st.nextToken() );
                double adjClose = Double.parseDouble( st.nextToken() );

                OHLCDataItem item = new OHLCDataItem(date, open, high, low, close, volume);
                dataItems.add(item);
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //Data from Yahoo is from newest to oldest. Reverse so it is oldest to newest
        Collections.reverse(dataItems);
*/
        //Convert the list into an array
        OHLCDataItem[] data = dataItems.toArray(new OHLCDataItem[dataItems.size()]);

        return data;
    }
}
