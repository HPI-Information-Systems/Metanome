<!DOCTYPE html>
<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["table"]});
      google.setOnLoadCallback(drawTable);

      function drawTable() {
        var data = new google.visualization.DataTable();
        data.addColumn('number', 'Index');
        data.addColumn('string', 'Name');
        data.addColumn('string', 'Type');
        data.addColumn('number', 'Uniqueness Rate');     
        data.addColumn('boolean', 'Unique?');
        data.addColumn('number', 'Null value Rate');
        data.addColumn('string', 'Histogram Link');
        data.addRows([
      	  <#list columnInformationList as column>
            [${column.columnIndex}, 
            '${column.columnName}', 
            '${column.columnType}', 
            ${column.uniquenessRate}, 
            ${column.isUniqueColumn()?c},
            ${column.nullRate},
            '<a href="columns/${column.slugifiedColumnName}.html">Histogram</a>'],
      	  </#list>
        ]);

        data.setColumnProperty(6, {allowHtml: true});

        var table = new google.visualization.Table(document.getElementById('table_div'));

        table.draw(data, {showRowNumber: false, allowHtml: true});
      }

    </script>
  </head>
  <body>
    <h1>${tableName}</h1>
    <span>Columns: ${columnCount}</span>
    <span>Rows: ${rowCount}</span>    
    <div id="table_div"></div>
  </body>
</html>
