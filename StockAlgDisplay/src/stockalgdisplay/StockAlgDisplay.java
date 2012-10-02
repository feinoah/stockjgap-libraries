/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockalgdisplay;


import onevariableexecutor.OneVariableStrategyExecutor;
import org.jgap.InvalidConfigurationException;
import twovariableexecutor.TwoVariableStrategyExecutor;

/**
 *
 * @author me
 */
public class StockAlgDisplay {
    static String file_path;
    static String ticker;
    static String solution;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidConfigurationException {
        StockAlgDisplay.file_path = args[0];
        StockAlgDisplay.ticker = args[1];
        StockAlgDisplay.solution = args[2];
        
        runProblem();
        
        System.exit(0);
    }
    
    /**
     * Runs the Buy/Sell strategy
     * 
     * @throws InvalidConfigurationException 
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void runProblem() throws InvalidConfigurationException
    {
        if(solution.contentEquals("OneVariableBacktestLongProblem") || solution.contentEquals("OneVariableBacktestShortProblem"))
        {
            new OneVariableStrategyExecutor(StockAlgDisplay.ticker, StockAlgDisplay.file_path, StockAlgDisplay.solution);
        }
        else if(solution.contentEquals("TwoVariableBacktest"))
        {
            new TwoVariableStrategyExecutor(StockAlgDisplay.ticker, StockAlgDisplay.file_path, StockAlgDisplay.solution);
        }
    }
}
