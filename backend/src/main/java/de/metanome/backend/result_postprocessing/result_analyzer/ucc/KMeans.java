package de.metanome.backend.result_postprocessing.result_analyzer.ucc;

import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;

import java.util.*;

/**
 * Created by Marie Schäffer on 26.11.2014.
 */
public class KMeans {

    private int inputList[];
    private List<List<UCCHistogram>> clusters;
    private List<List<UCCHistogram>> tempClusters;
    private List<UCCHistogram> meansList;
    private List<Double>  diff;
    private int clusterCount;

    static double threshold = 0.03;  // Threshold for minimum improvement until algorithm is aborted

    public static KMeans  cluster(List<UCCHistogram> input){

        KMeans kmeans = new KMeans();
         int clusterCount = 2; //Initial number of Clusters is 2

        double prev=5, prevprev=5, improvement=1;
        double cohesion;
        while (improvement > threshold && clusterCount<input.size()/1.5){
            kmeans .setClusterCount(clusterCount);
            kmeans.init(input);
            kmeans.findClusters(input);
            cohesion = kmeans.calculateCohesion();
            improvement = 1 - cohesion / prevprev;
            prevprev = prev;
            prev = cohesion;


//            System.out.println("Cohesion: " + cohesion);

            clusterCount++;
        }
        kmeans.printResult();

        return kmeans;
    }

    public List<List<ColumnCombination>> getClusterContent() {
        List<List<ColumnCombination>> result = new LinkedList<List<ColumnCombination>>();
        for(List<UCCHistogram> cluster : clusters) {
            List<ColumnCombination> uccCluster = new LinkedList<ColumnCombination>();
            for(UCCHistogram histo : cluster) {
                uccCluster.add(histo.getUCC());
            }
            result.add(uccCluster);
        }
        return result;
    }

    public List<HashMap<String, Double>> getClusterInfo(){
        List<HashMap<String, Double>> info = new ArrayList<HashMap<String, Double>>();
        for(int i=0; i<clusterCount; i++){
            HashMap<String, Double> cluster = new HashMap<String, Double>();
            UCCHistogram meanHist = UCCHistogram.calculateMean(clusters.get(i));
            cluster.put("Size", (double)clusters.get(i).size());
            cluster.put("Average Number of Columns", meanHist.getCount());
            cluster.put("Average Uniqueness", meanHist.getAvg());
            cluster.put("Randomness", meanHist.getRandomness() );
            info.add(cluster);

        }
        return info;
    }



    private int cal_diff(UCCHistogram histogram) // This method will determine the cluster in which an element go at a particular step.
    {
        diff.clear();
        for(int i=0;i< clusters.size();++i){
            diff.add(i, meansList.get(i).calculateDiff(histogram));
        }
        return diff.indexOf(Collections.min(diff));

    }



    private void cal_mean() // This method will determine intermediate mean values
    {
        meansList.clear();
        for(int i=0;i< clusters.size();i++){

            meansList.add(i, UCCHistogram.calculateMean(clusters.get(i)));
        }
    }



    private boolean isFinished() // This checks if previous clusters ie. tempClusters and current clusters are same.Used as terminating case.
    {
        for(int i=0; i<clusters.size() && i<tempClusters.size();++i){
            if( tempClusters.get(i).size()!=clusters.get(i).size()){
                return false;
            }
            if(!(tempClusters.get(i).containsAll(clusters.get(i)) && clusters.get(i).containsAll(tempClusters.get(i)))){
                return false;
            }
        }
        return true;
    }



    private void init(List<UCCHistogram> input) {
    /* Initialising lists */
        clusters =new LinkedList<List<UCCHistogram>>();
        for(int i=0; i<clusterCount; i++){
            clusters.add(new LinkedList<UCCHistogram>());
        }
        tempClusters = new LinkedList<List<UCCHistogram>>();

        meansList =new LinkedList<UCCHistogram>();
        diff=new LinkedList<Double>();
        initializeMeans(input);
    }

    private  double calculateCohesion() {
        double maxDiff=0;
        for(int i=0; i<clusters.size(); i++){
            for(int j=0; j<clusters.get(i).size(); j++){
                maxDiff = Math.max(maxDiff, meansList.get(i).calculateDiff(clusters.get(i).get(j)));
            }
        }
        return maxDiff;
    }

    private  void findClusters(List<UCCHistogram> input) {
        int temp=0;
        boolean finished=false;
        do
        {
            for(int i=0;i< clusters.size();i++) //clear old clusters
            {
                    clusters.get(i).clear();
                }

            for(int i=0;i< input.size();i++) // calculate correct bucket for every histogram
            {
                temp=cal_diff(input.get(i));
                clusters.get(temp).add(input.get(i));
            }
            cal_mean(); // call to method which will calculate mean
             // check if terminating condition is satisfied.
            finished = isFinished();
            if(! finished) {
/*Take backup of clusters in tempClusters so that you can check for equivalence in next step*/
                tempClusters.clear();
                for (int i = 0; i < clusters.size(); i++){

                    List<UCCHistogram> tempList = new LinkedList<UCCHistogram>();
                    tempList.addAll(clusters.get(i));
                    tempClusters.add(tempList);
                }
            }
            //printIntermediateResult();

            //printIntermediateMeans();

        } while(!finished);
    }

    private  void printIntermediateMeans() {
        System.out.println("\n\n\nMeans: ");
        for(int i=0;i< meansList.size();i++){
            System.out.print("M"+(i+1));
            System.out.print(meansList.get(i).toString()+"\n ");
        }
    }

    private  void printIntermediateResult() {
        System.out.println("\n\nClusters at this step: ");
        for(int i=0;i< clusters.size();i++){
            System.out.print("K"+(i+1)+"{ ");
            for(int j=0; j<clusters.get(i).size();j++)
                System.out.print(clusters.get(i).get(j).toString()+" ");
            System.out.println("}");
        }
    }

    private  void printResult() {
        System.out.println("\n\n\nThe Final Clusters By Kmeans are as follows: ");
        for(int i=0;i< clusters.size();i++){
            System.out.print("K"+(i+1)+"{ ");
            for(int j=0; j<clusters.get(i).size();j++)
                System.out.print(clusters.get(i).get(j).toString()+" ");
            System.out.println("}");
        }

    }



    private  void initializeMeans(List<UCCHistogram> input) {

        // for each histo
        // find avg distance to all other histos
        // get "clusters.size()" histos with  highest values
        // add them to meansList
        List<Double> distlist = new ArrayList<>();
        UCCHistogram meanHist = UCCHistogram.calculateMean(input);

        for(int i=0; i<input.size(); i++){
            distlist.add(i, input.get(i).calculateDiff(meanHist));
        }
        Set<Double> valueset = new HashSet<>();
        valueset.addAll(distlist);
        List<Double> valuelist = new ArrayList<>();
        valuelist.addAll(valueset);
  //      Collections.sort(valuelist);
        Collections.sort(valuelist ,Collections.reverseOrder());
        for(int j=0; j<clusters.size(); j++){
            double minval = valuelist.get(j);
            meansList.add(input.get(distlist.indexOf(minval)));
        }
    }

    public void setClusterCount(int clusterCount) {
        this.clusterCount = clusterCount;
    }


    public static class UCCHistogram {
        private ColumnCombination ucc;

        private double min;
        private double max;
        private double avg;
        private double minDist;
        private double maxDist;
        private double medianDist;
        private double count;
        private double randomness;

        private double minfactor = 0; //5;
        private double maxfactor = 0; //5;
        private double avgfactor = 1.5; //3;
        private double minDistfactor = 0; //2;
        private double maxDistfactor = 0; //2;
        private double medianDistfactor = 0; //2;
        private double countfactor = 1; //0.1;
        private double randomnessfactor = 1.5; //1;



        public UCCHistogram(ColumnCombination ucc, double min, double max, double avg, double minDist, double maxDist, double medianDist, double count, double randomness) {
            this.ucc = ucc;
            this.min = min;
            this.max = max;
            this.avg = avg;
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.medianDist = medianDist;
            this.count = count;
            this.randomness = randomness;
        }


        public UCCHistogram clone(){
            return new UCCHistogram(ucc, min, max, avg, minDist, maxDist, medianDist, count, randomness);
        }


        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        public double getAvg() {
            return avg;
        }

        public double getMinDist() {
            return minDist;
        }

        public double getMaxDist() {
            return maxDist;
        }

        public double getMedianDist() {
            return medianDist;
        }

        public double getCount() { return count;
        }
        public double getRandomness() {
            return randomness;
        }

        public ColumnCombination getUCC() {
            return ucc;
        }

        public void setValues(double min, double max, double avg, double minDist, double maxDist, double medianDist, double count, double randomness){
            this.min = min;
            this.max = max;
            this.avg = avg;
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.medianDist = medianDist;
            this.count = count;
            this.randomness = randomness;
        }


        public HashMap<String, Double> getValues( ){
            HashMap<String, Double> values = new HashMap<>();
    //        values.put("Minimum Uniqueness", this.min);
    //        values.put("Maximum Uniqueness", this.max) ;
            values.put("Average Uniqueness", this.avg) ;
    //        values.put("Minimum Distance", this.minDist);
    //        values.put("Maximum Distance", this.maxDist) ;
    //        values.put("Median Distance", this.medianDist) ;
            values.put("Number of Columns", this.count) ;
            values.put("Randomness", this.randomness) ;

            return values;
        }


        public String toString(){
            String format = "{ min: %f, max: %f, avg: %f, minDist: %f, maxDist: %f, medianDist: %f, count: %f, randomness: %f }";
            return String.format(format, min, max, avg, minDist, maxDist, medianDist, count, randomness);
        }


        public double calculateDiff(UCCHistogram other) {
            //Calculate de difference between a histogram and the mean of a cluster here. The different factors can be weighted as necessary.
            return (Math.abs(this.min-other.getMin()) *minfactor
                    +Math.abs(this.max-other.getMax()) *maxfactor
                    +Math.abs(this.avg-other.getAvg()) *avgfactor
                    +Math.abs(this.minDist-other.getMinDist()) *minDistfactor
                    +Math.abs(this.maxDist-other.getMaxDist()) *maxDistfactor
                    +Math.abs(this.medianDist-other.getMedianDist()) *medianDistfactor
                    + Math.abs(this.count-other.getCount()) * countfactor
                    + Math.abs(this.randomness-other.getRandomness()) * randomnessfactor
                    ) /8;
        }

        public static UCCHistogram calculateMean(List<UCCHistogram> input){
            double tempMin, tempMax, tempAvg, tempMinDist, tempMaxDist, tempMeanDist, tempCount, tempRandomness;
            tempMin = tempMax = tempAvg = tempMinDist = tempMaxDist = tempMeanDist = tempCount = tempRandomness = 0d;

            for(UCCHistogram h : input){
                tempMin += h.getMin();
                tempMax += h.getMax();
                tempAvg += h.getAvg();
                tempMinDist += h.getMinDist();
                tempMaxDist += h.getMaxDist();
                tempMeanDist += h.getMedianDist();
                tempCount += h.getCount();
                tempRandomness += h.getRandomness();
            }
            return new UCCHistogram(
                    null,
                    tempMin/input.size(),
                    tempMax/input.size(),
                    tempAvg/input.size(),
                    tempMinDist/input.size(),
                    tempMaxDist/input.size(),
                    tempMeanDist/input.size(),
                    tempCount/input.size(),
                    tempRandomness/input.size());
        }

    }
}
