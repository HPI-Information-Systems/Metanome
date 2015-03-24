package de.metanome.backend.result_postprocessing.result_analyzer.ucc;

import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Marie on 03.12.2014.
 */
public class ClusterPlotter extends ApplicationFrame {

    private List<Double> columnVariance;
    private List<List<ColumnCombination>> clusters;
    private List<String> namesList;

    public ClusterPlotter(List<Double> columnVariance, List<List<ColumnCombination>> clusters, List<String> namesList) {
        super("TEST");
        this.clusters = clusters;
        this.columnVariance = columnVariance;
        this.namesList = namesList;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(clusters.size(),1 , 20, 20);
        //layout.setColumns(1);
        //layout.setRows(0);
        panel.setLayout(layout);
        plot(panel);

        //panel.setPreferredSize(new Dimension(200, 200));
        JScrollPane scroll = new JScrollPane(panel);
        setContentPane(scroll);
    }

    private void plot(JPanel panel) {

        for(List<ColumnCombination> cluster : clusters) {
            JPanel clusterPanel = new JPanel();
            GridLayout layout = new GridLayout();
            layout.setColumns(5);
            layout.setRows(cluster.size()/3);
            clusterPanel.setLayout(layout);
            //clusterPanel.setPreferredSize();

            for(ColumnCombination ucc : cluster) {
                List<Integer> indices = ucc.getColumnIndices();
                Collections.sort(indices, new UniqueValueComparator(this.columnVariance));
                ChartPanel chart = createChart(indices);
                chart.setPreferredSize(new Dimension(30,30));
                //chart.setMaximumSize(new Dimension(50,50));
                chart.revalidate();
                clusterPanel.add(chart);
            }
            panel.add(clusterPanel);
        }

    }

    private ChartPanel createChart(List<Integer> indices) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int i = 0; i < indices.size(); i++) {
            dataset.addValue(this.columnVariance.get(indices.get(i)), "", namesList.get(indices.get(i)));//String.valueOf(indices.get(i)));
        }

        JFreeChart chart = ChartFactory.createBarChart("", "", "", (CategoryDataset) dataset, PlotOrientation.VERTICAL, false, false, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setSize(new Dimension(20,20));
        return chartPanel;
    }

    private class UniqueValueComparator implements Comparator<Integer> {

        private List<Double> uniqueValues;

        public UniqueValueComparator(List<Double> uniqueValues) {
            this.uniqueValues = uniqueValues;
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            Double uniqueCount1 = this.uniqueValues.get(o1);
            Double uniqueCount2 = this.uniqueValues.get(o2);
            if(uniqueCount1 < uniqueCount2) {
                return -1;
            }
            if(uniqueCount1 > uniqueCount2) {
                return 1;
            }
            return 0;
        }
    }
}

