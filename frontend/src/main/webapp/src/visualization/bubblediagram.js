

//  ---------------------------------------- Add Bubble Diagram ---------------------------------------
function initializeClusterDiagram(target_id,second_target_id){
    var bubbleplot = dimple.newSvg(target_id, 570, 400);

    /**
     * @return {boolean}
     */
    function UrlExists(url) {
      var http = new XMLHttpRequest();
      http.open('HEAD', url, false);
      http.send();
      return http.status != 404;
    }
    var data_url = "UCCResultAnalyzer/UCCClusters.json";
    if (!UrlExists(data_url)) {
      data_url = "http://localhost:8888/src/visualization/UCCResultAnalyzer/UCCClusters.json";
    }

    d3.json(data_url, function (data) {
      var clusterChart = new dimple.chart(bubbleplot, data);
      clusterChart.setBounds(50, 50, 500, 300);
      var x = clusterChart.addMeasureAxis("x", "Average Uniqueness");
      var y = clusterChart.addMeasureAxis("y", "Average Number of Columns");
      var z = clusterChart.addMeasureAxis("z", "Size");
      var color = clusterChart.addColorAxis("Randomness", ["green", "yellow", "red"]);

      x.overrideMin = 0;
      x.overrideMax = 1;

      color.overrideMin = 0;
      color.overrideMax = 1;


      s = clusterChart.addSeries(["Randomness", "ClusterNr"], dimple.plot.bubble);

              //Add Handler for click event
      s.addEventHandler("click", onClick);

      //clusterChart.addLegend(70, 60, 510, 20, "right");
      clusterChart.draw();






          // Event to handle mouse click on bubble
    function onClick(e) {
        //alert(e.seriesValue[1]);
        initializeUCCDiagram(second_target_id, e.seriesValue[1]);

    };







    // -----------------------------------  Add Custom Legend ----------------------------------------
    //Gradient for color description
    var gradient = bubbleplot
      .append("linearGradient")
        .attr("id", "gradient")
        .attr("x1", "0%")
        .attr("y1", "0%")
        .attr("x2", "100%")
        .attr("y2", "100%")
        .attr("spreadMethod", "pad")
        .attr("gradientTransform", "rotate(-45)")

     gradient.append("bubbleplot:stop")
        .attr("offset", "0%")
        .attr("stop-color", "green")
        .attr("stop-opacity", 1);
    gradient.append("bubbleplot:stop")
        .attr("offset", "35%")
        .attr("stop-color", "yellow")
        .attr("stop-opacity", 1);
    gradient.append("bubbleplot:stop")
        .attr("offset", "80%")
        .attr("stop-color", "red")
        .attr("stop-opacity", 1);


    //rectangle containing gradient
    bubbleplot.append("rect")
        .attr("x", 480 )
        .attr("y", 30)
        .attr("width", 50)
        .attr("height", 15)
        .style("fill",  "url(#gradient)");


    //Left border of range
    bubbleplot.append("text")
        .attr("x", 470 )
        .attr("y", 40)
        .attr("text-anchor", "left")
        .style("font-size", "10px")
        .text("0");
    //Right border of range
    bubbleplot.append("text")
        .attr("x", 535 )
        .attr("y", 40)
        .attr("text-anchor", "left")
        .style("font-size", "10px")
        .text("1");
    //Description
    bubbleplot.append("text")
        .attr("x", 470 )
        .attr("y", 25)
        .attr("text-anchor", "left")
        .style("font-size", "12px")
        .text("Randomness:");

    // ------------------------------ END LEGEND -----------------------------------


    });
}

















