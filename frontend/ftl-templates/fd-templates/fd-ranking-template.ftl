<!DOCTYPE html>
<html>
  <head>

	<style>
		.tooltip {
			display: none;
		}
	</style>

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["table"]});
      google.setOnLoadCallback(drawTable);

      function drawTable() {
        var data = new google.visualization.DataTable();
        
        data.addColumn('string', 'Functional Dependency');
        data.addColumn('string', 'Determinant');
        data.addColumn('string', 'Dependant');
		data.addColumn('string', 'Additional Dependant Columns');
        data.addColumn('number', 'Determinant Size Ratio');
        data.addColumn('number', 'Dependant Size Ratio');
        data.addColumn('number', 'Determinant Constancy Ratio');
        data.addColumn('number', 'Dependant Constancy Ratio');                        
        data.addColumn('number', 'Coverage');
        data.addColumn('number', 'Information Gain in Cells');
        data.addColumn('number', 'Information Gain in Bytes');
        data.addColumn('number', 'Pollution');
        data.addColumn('string', 'Minimal Pollution Column');

        data.addRows([
      	  <#list ranking?keys as fd>
		  	     ['${fd}<div class="tooltip">"${ranking[fd].rankingTooltip}"</div>',
              '${ranking[fd].determinant}<div class="tooltip">"${ranking[fd].determinantTooltip}"</div>',
              '${ranking[fd].dependant}<div class="tooltip">"${ranking[fd].dependantTooltip}"</div>',
			  '${ranking[fd].additionalColumns}',
              ${ranking[fd].fdRank.determinantSizeRatio},
              ${ranking[fd].fdRank.dependantSizeRatio},
              ${ranking[fd].fdRank.determinantConstancyRatio},
              ${ranking[fd].fdRank.dependantConstancyRatio},
              ${ranking[fd].fdRank.coverage},
              ${ranking[fd].fdRank.informationGainCells},
              ${ranking[fd].fdRank.informationGainBytes},
              ${ranking[fd].fdRank.pollution},
              '${ranking[fd].fdRank.minPollutionColumn}'],
          </#list>
        ]);

        var table = new google.visualization.Table(document.getElementById('table_div'));
        
        var options = {
          showRowNumber: false,
          allowHtml: true,
		  sort: 'event'
        };        

        table.draw(data, options);

		createTitles();

		google.visualization.events.addListener(table, 'sort', function(event) {
		    options.sortColumn = event.column;
		    options.sortAscending = event.ascending;
		    data.sort([{column: event.column, desc: !event.ascending}]);
		    table.draw(data, options);
            createTitles();
		});

      }

	  function createTitles(){
        $('.google-visualization-table-td').each(function(index, value) {
			if($(value).children().length){
				$(value).prop('title', $(value).children().first().text());
			}
		});
	  }
    </script>
  </head>
  <body>
    <h1>Ranking overview</h1>        
    <div id="table_div"></div>
  </body>
</html>
