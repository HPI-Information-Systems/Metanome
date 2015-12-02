var UCC_CLUSTER_CHART = null


function predicatBy(prop) {
  return function (a, b) {
    if (a[prop] > b[prop]) {
      return 1;
    } else if (a[prop] < b[prop]) {
      return -1;
    }
    return 0;
  }
}


function initializeUCCDiagram(target_id, cluster_nr) {
  if (UCC_CLUSTER_CHART != null) UCC_CLUSTER_CHART.remove()

  UCC_CLUSTER_CHART = dimple.newSvg(target_id, 570, 400);

  /**
   * @return {boolean}
   */
  function UrlExists(url) {
    var http = new XMLHttpRequest();
    http.open('HEAD', url, false);
    http.send();
    return http.status != 404;
  }

  var data_url = "UCCResultAnalyzer/UCCData.json";
  if (!UrlExists(data_url)) {
    data_url = "http://localhost:8888/src/visualization/UCCResultAnalyzer/UCCData.json";
  }

  d3.json(data_url, function (data) {
    // If a cluster is specified, only select data from this cluster
    if (cluster_nr != null) data = dimple.filterData(data, "ClusterNr", cluster_nr.toString())

    var myChart = new dimple.chart(UCC_CLUSTER_CHART, data);
    myChart.setBounds(50, 50, 500, 300);
    var x = myChart.addMeasureAxis("x", "Average Uniqueness");
    var y = myChart.addMeasureAxis("y", "Number of Columns");
    var color = myChart.addColorAxis("Randomness", ["green", "yellow", "red"]);

    if (cluster_nr == null) {
      x.overrideMin = 0;
      x.overrideMax = 1;

    } else {
      data.sort(predicatBy("Average Uniqueness"));
      x.overrideMin = Math.max((data[0]["Average Uniqueness"]) - 0.05, 0);

      data.sort(predicatBy("Number of Columns"));
      y.overrideMin = Math.max((data[0]["Number of Columns"]) - 0.05, 0);
    }

    color.overrideMin = 0;
    color.overrideMax = 1;

    var s = myChart.addSeries(["Randomness", "UCCid"], dimple.plot.bubble);
    s.aggregate = dimple.aggregateMethod.avg;
    s.addEventHandler("click", onClick);
    myChart.draw();


    function onClick(e) {
      var url = 'UCChistogram.html?id=' + e.seriesValue[1];
      var w = window.open(
        url,
        '_blank',  //'UCC Histogram View',
        'width=640px,height=480px,resizable=1,' +
        'toolbar=0,menubar=0,scrollbars=0');
    }


//Add Custom Legend
    //Gradient for color description
    var gradient = UCC_CLUSTER_CHART
      .append("linearGradient")
      .attr("id", "gradient")
      .attr("x1", "0%")
      .attr("y1", "0%")
      .attr("x2", "100%")
      .attr("y2", "100%")
      .attr("spreadMethod", "pad")
      .attr("gradientTransform", "rotate(-45)")

    gradient.append("UCC_CLUSTER_CHART:stop")
      .attr("offset", "0%")
      .attr("stop-color", "green")
      .attr("stop-opacity", 1);
    gradient.append("UCC_CLUSTER_CHART:stop")
      .attr("offset", "35%")
      .attr("stop-color", "yellow")
      .attr("stop-opacity", 1);
    gradient.append("UCC_CLUSTER_CHART:stop")
      .attr("offset", "80%")
      .attr("stop-color", "red")
      .attr("stop-opacity", 1);


    //rectangle containing gradient
    UCC_CLUSTER_CHART.append("rect")
      .attr("x", 480)
      .attr("y", 30)
      .attr("width", 50)
      .attr("height", 15)
      .style("fill", "url(#gradient)");


    //Left border of range
    UCC_CLUSTER_CHART.append("text")
      .attr("x", 470)
      .attr("y", 40)
      .attr("text-anchor", "left")
      .style("font-size", "10px")
      .text("0");
    //Right border of range
    UCC_CLUSTER_CHART.append("text")
      .attr("x", 535)
      .attr("y", 40)
      .attr("text-anchor", "left")
      .style("font-size", "10px")
      .text("1");
    //Description
    UCC_CLUSTER_CHART.append("text")
      .attr("x", 470)
      .attr("y", 25)
      .attr("text-anchor", "left")
      .style("font-size", "12px")
      .text("Randomness:");


  });
}
