<!DOCTYPE html>
<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["table"]});
      google.setOnLoadCallback(drawTable);

      function drawTable() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'UCC');
        data.addColumn('number', 'Length');
        data.addColumn('string', 'Occurences');
        data.addColumn('number', 'Average Occurence');
        data.addRows([
      	  <#list uccRanking as ranking>
            ['${ranking.ucc}',
            ${ranking.length},
            '${ranking.occurences}',
            ${ranking.avgOccurence}],
      	  </#list>
        ]);

        var table = new google.visualization.Table(document.getElementById('table_div'));

        table.draw(data, {showRowNumber: false, allowHtml: true});
      }

    </script>
  </head>
  <body>
  <input type=button onClick="location.href= '${tableName}-UCC-Ranking-Advanced.html'" value='Load additional Information'>

  <br>
  <br>
  <!-- COLUMN LINKS, DONT REMOVE THIS COMMENT -->


  <h1>${tableName}</h1>
  <div id="table_div"></div>
  </body>
</html>
