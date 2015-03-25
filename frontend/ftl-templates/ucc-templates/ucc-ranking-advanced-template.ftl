<!DOCTYPE html>
<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["table"]});
      google.setOnLoadCallback(drawTable);

      function drawTable() {
        var data = new google.visualization.DataTable();
        data.addColumn('number', 'Rank');
        data.addColumn('string', 'UCC');
        data.addColumn('number', 'Length');
        data.addColumn('number', 'Maximal Uniqueness');
        data.addColumn('number', 'Minimal Uniqueness');
        data.addColumn('number', 'Randomness');
        data.addColumn('string', 'Occurences');
        data.addColumn('number', 'Average Occurence');
        data.addRows([
      	  <#list uccRanking as ranking>
            [${ranking.rank},
            '${ranking.ucc}',
            ${ranking.length},
            ${ranking.min},
            ${ranking.max},
            ${ranking.value},
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
  <input type=button onClick="location.href= '${tableName}-UCC-Ranking.html'" value='Show less Information'>

  <br>
  <br>

  <h1>${tableName}</h1>
  <div id="table_div"></div>
  </body>
</html>
