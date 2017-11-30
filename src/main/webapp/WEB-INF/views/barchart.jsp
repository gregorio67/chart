<html>
  <head>
    <!-- script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script-->
    <script type="text/javascript" src="/resources/js/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/resources/js/google/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawVisualization);

      function drawVisualization() {
    	  
    	  var jsonData = $.ajax({
    		  url : "http://localhost:8080/barchart.do",
    		  dataType : "json",
    		  async : false
    	  }).responseText;
        // Some raw data (not necessarily accurate)

    var options = {
      title : 'Company Performace',
      curveType: 'function',
      legend: { position: 'bottom' }
    };

    var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
    chart.draw(jsonData, options);
  }
    </script>
  </head>
  <body>
    <div id="chart_div" style="width: 1200px; height: 500px;"></div>
  </body>
</html>