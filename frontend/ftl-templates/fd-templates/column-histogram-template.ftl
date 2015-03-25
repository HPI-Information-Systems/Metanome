<!DOCTYPE html>
<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Value', 'Count'],
          <#list columnInformation.histogram.histogramData?keys as key>
		  	['${key}', ${columnInformation.histogram.histogramData[key]}],
          </#list>
          ]);

        var options = {
          title: 'Values histogram for column: "${columnInformation.columnName}"',
          legend: { position: 'none' },
          //histogram: {bucketSize: 1},
        };

        var chart = new google.visualization.Histogram(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
    </script>
  </head>
  <body>
  	<h1>Column histogram for: "${columnInformation.columnName}"</h1>
    <div id="chart_div" style="width: 900px; height: 500px;"></div>
  	<span><a href="../${tableName}.html">Back to table</a></span>
  </body>
</html>