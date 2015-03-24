package de.metanome.backend.result_postprocessing.result_analyzer.ucc;

import de.metanome.algorithm_integration.results.Result;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.backend.result_postprocessing.io_helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.io_helper.InputAnalyzer;
import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.ResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.ranking.MinMaxRanker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static de.metanome.backend.result_postprocessing.visualizing_utils.JSONPrinter.printUCCClusters;
import static de.metanome.backend.result_postprocessing.visualizing_utils.JSONPrinter.printUCCHistograms;


/**
 * Created by Marie Schaeffer on 12.11.2014.
 */
public class UCCAnalyzer extends ResultAnalyzer {

    private List<Double> columnUniqueness;
    private List<ColumnCombination> results;
    private InputAnalyzer inputAnalyzer;
    private List<KMeans.UCCHistogram> histograms;
    private String outputDirectoryPath = "Visualization-Experiments/UCCAnalyzer";
    private int maxSize;


    public static List<ColumnCombination> createFromResult(List<Result> result, int columnCount, TableInformation tableInformation) {

        List<ColumnCombination> newResults = new ArrayList<>();
        for (Result r : result) {
            UniqueColumnCombination ucc = (UniqueColumnCombination) r;
            List<Integer> columnIdentifiers = InputAnalyzer.extractColumnNumbers(
                    ucc.getColumnCombination().getColumnIdentifiers(), tableInformation);
            newResults.add(new ColumnCombination(columnIdentifiers, columnCount));
        }
        return newResults;
    }

  @Override
  protected void analyzeResultsWithoutTupleData(List<Result> oldResults) {

  }

  @Override
  protected void analyzeResultsWithTupleData(List<Result> oldResults) {

  }

  @Override
  protected void printResultsToFile(boolean useRowData) {

  }

  @Override
    protected void analyzeResults(List<Result> oldResults) {



        inputAnalyzer = new InputAnalyzer(relationalInputGenerators.get(0), true);

        int columnCount = inputAnalyzer.getTableInformation().getColumnCount();

        this.results =
                UCCAnalyzer.createFromResult(oldResults, columnCount, inputAnalyzer.getTableInformation());

        setColumnUniqueness();

//        inputAnalyzer.getTableInformation().getColumn(0).getDistinctValuesCount();

        histograms = new LinkedList<KMeans.UCCHistogram>();
//        calculateMaxSize();
        for(int i=0; i<results.size(); i++){
            if(results.get(i).getColumnCount() > 1) {
                histograms.add(createUCCHistogram(results.get(i)));
            }
        }
        if(histograms.size()>1){
            KMeans clusters = KMeans.cluster(histograms);

            printUCCClusters(this.outputDirectoryPath + "/UCCClusters.json", clusters.getClusterInfo());
            printUCCHistograms(this.outputDirectoryPath + "/UCCHistograms.json", histograms);

            // Print JSON-Data for Histograms of all UCCs
//            for(int i=0; i<results.size(); i++){
//                ColumnCombination ucc = results.get(i);
//                printUCCInfo(this.outputDirectoryPath + "/UCCHistogramme/UCC"+i+".json", getUCCInfo(ucc));
//            }


            ClusterPlotter plotter = new ClusterPlotter(columnUniqueness, clusters.getClusterContent(), getColumnNames());
            plotter.setVisible(true);
        }else{
            System.out.println("\nNot enough interesting histograms for clustering!\n");
        }

    }

    public List<String> getColumnNames(){
        List<String> namesList = new ArrayList<>();
        for(int i=0; i<inputAnalyzer.getTableInformation().getColumnCount(); i++){
            namesList.add(i, inputAnalyzer.getTableInformation().getColumn(i).getColumnName());
        }
        return namesList;
    }


    public void setColumnUniqueness(){
        columnUniqueness = new LinkedList<Double>();
        double lineCount = inputAnalyzer.getTableInformation().getRowCount();
        List<ColumnInformation> columns = inputAnalyzer.getTableInformation().getColumnInformationList();
        for(int i=0; i<columns.size(); i++){
            double distCount = columns.get(i).getDistinctValuesCount();

            columnUniqueness.add(i, distCount / lineCount);
        }
    }


    private void printClusterList(List<HashMap<String, Double>> clusters){

        //create output directory

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputDirectoryPath + "/minimized-fds.txt"));
            //.....

            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    private HashMap<String, Double> getUCCInfo(ColumnCombination ucc){
        HashMap<String, Double> uccInfo = new HashMap<>();
        for(int index : ucc.getColumnIndices()){
            uccInfo.put(this.inputAnalyzer.getTableInformation().getColumn(index).getColumnName(), this.columnUniqueness.get(index));
        }
        return uccInfo;
    }


    private void calculateMaxSize(){
        int max = 0;
        for(ColumnCombination ucc : this.results){
            max = Math.max(max, ucc.getColumnCount());
        }
        this.maxSize = max;
    }


    public KMeans.UCCHistogram createUCCHistogram(ColumnCombination ucc) {
            double size = ucc.getColumnCount();
            double minDist = 0;
            double maxDist = 0;
            double medianDist = 0;

        List<Double> tempUniqueness = new LinkedList<Double>();
        List<Double> tempDistances = new LinkedList<Double>();

        for(int i=0; i<ucc.getColumnCount() ; i++){
            tempUniqueness.add(i, columnUniqueness.get(ucc.getColumnIndices().get(i)));
        }

        //calculate average Uniqueness
        double sum = 0;
        for (double d : tempUniqueness){
            sum += d;
        }
        double avg = sum / tempUniqueness.size();

        //calculate distances
        Collections.sort(tempUniqueness);
        for( int i=0; i<tempUniqueness.size()-1; i++){
            tempDistances.add(i, tempUniqueness.get(i+1) - tempUniqueness.get(i));
        }
        Collections.sort(tempDistances);

        //Calculate Randomness
        MinMaxRanker ranker = new MinMaxRanker();
        Double randomness = ranker.calculate(ucc, inputAnalyzer.getTableInformation(), null);


        return new KMeans.UCCHistogram(
                ucc,
                tempUniqueness.get(0),
                tempUniqueness.get(tempUniqueness.size()-1),
                avg,
                tempDistances.get(0),
                tempDistances.get(tempDistances.size()-1),
                tempDistances.get(tempDistances.size()/2),
                size,
                randomness);
    }

}
