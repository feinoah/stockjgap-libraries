/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockalgdisplay;

import StockGP.StockGPProblem;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.GPProblem;
import org.jgap.gp.impl.DefaultGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.GPPopulation;

/**
 *
 * @author me
 */
public class StockAlgDisplay {
    static String file_path_enter;
    static String file_path_exit;
    static String ticker;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidConfigurationException {
        file_path_enter = args[0];
        file_path_exit = args[1];
        ticker = args[2];
        
        runProblem();
        System.exit(0);
    }
    
    
    
    private static GPPopulation returnPop(String path) throws FileNotFoundException
    {
        XStream xstream = new XStream(); 
        File f = new File(path);
        InputStream oi = new FileInputStream(f); 

        GPPopulation gPop = (GPPopulation) xstream.fromXML(oi);
        
        return gPop;
    }
    
    /**
     * Runs the Buy/Sell strategy
     * 
     * @throws InvalidConfigurationException 
     */
    public static void runProblem() throws InvalidConfigurationException
    {
        GPConfiguration config = new GPConfiguration();
        GPFitnessFunction gpFunc = new StockGPProblem.StockGPFitnessFunction();

        GPProblem problemEnterStrategy;
        GPProblem problemExitStrategy;

        config.setPopulationSize(1);
        config.setGPFitnessEvaluator(new DefaultGPFitnessEvaluator());
        config.setMinInitDepth(2);
        config.setMaxInitDepth(20);
        config.setMaxCrossoverDepth(20);
        config.setFitnessFunction(gpFunc);
        config.setStrictProgramCreation(false);
        config.setPreservFittestIndividual(true);

        try
        {
            //buy/sell/short/cover all should have the same population size
            GPPopulation loadedPop = returnPop(file_path_enter);
            
            if(loadedPop.getPopSize() >= 0)
            {
                config.setPopulationSize(loadedPop.getPopSize());
            }
        }
        catch(FileNotFoundException e)
        {
            System.exit(0);
        }        


        problemEnterStrategy = new StockGPProblem(config, ticker);
        problemExitStrategy = new StockGPProblem(config, ticker);


        GPGenotype gpEnter = problemEnterStrategy.create();
        GPGenotype gpExit = problemExitStrategy.create();

        gpEnter.setVerboseOutput(true);
        gpExit.setVerboseOutput(true);

        
        try
        {
            GPPopulation loadedBuyPop = returnPop(file_path_enter);

            //we should never error out because of what was defined in the above LOAD_POPULATION if statement
            for(int loadSize = 0; loadSize < loadedBuyPop.getPopSize(); loadSize++)
            {
                gpEnter.getGPPopulation().setGPProgram(loadSize, loadedBuyPop.getGPProgram(loadSize));
            }

            GPPopulation loadedSellPop = returnPop(file_path_exit);

            for(int loadSize = 0; loadSize < loadedSellPop.getPopSize(); loadSize++)
            {
                gpExit.getGPPopulation().setGPProgram(loadSize, loadedSellPop.getGPProgram(loadSize));
            }
        }
        catch(FileNotFoundException e)
        {
            System.exit(0);
        }

        System.out.println("BEST ENTER FORMULA / FITNESS:  '" + gpEnter.getFittestProgram().toStringNorm(0) + "' / " + gpEnter.getFittestProgram().getFitnessValue());
        System.out.println("BEST EXIT FORMULA / FITNESS:  '" + gpExit.getFittestProgram().toStringNorm(0) + "' / " + gpExit.getFittestProgram().getFitnessValue());
    }
}
