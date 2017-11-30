<html>
  <head>
 	<link href="/resources/css/c3/c3.min.css" rel="stylesheet">
    <!-- script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script-->
    <script type="text/javascript" src="/resources/js/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/resources/js/c3/c3.min.js"></script>
    <script type="text/javascript" src="/resources/js/c3/d3.v3.min.js" charset="utf-8"></script>

    <script type="text/javascript">
	    function draw_chart() {
	    //	  var jsonData = $.ajax({
	    //		  url : "http://localhost:8080/showData.do",
	    //		  dataType : "json",
	    //		  async : false
	    //	  }).responseText;
	    	  
	    //	alert(jsonData);
	        var chart = c3.generate({
	            bindto: '#chart_div',
	            data: {
	            	url : "http://localhost:8080/showData.do",
	            	mimeType : "json"
	            },
	            //data: {
	            //  columns: [
	            //    ['data1', 30, 200, 100, 400, 150, 250],
	            //    ['data2', 50, 20, 10, 40, 15, 25]
	            //  ],
	            //  axes: {
	            //    data2: 'y2'
	            //  }
	            //},
	            axis: {
	              y: {
	                label: { // ADD
	                  text: 'Y Label',
	                  position: 'outer-middle'
	                }
	              }
	              //y2: {
	              //  show: true,
	              //  label: { // ADD
	              //    text: 'Y2 Label',
	              //    position: 'outer-middle'
	              //  }
	             // }
	            }
	        });
	        //chart.load(jsonData);    	
    }
    </script>
  </head>
  <body onload="draw_chart()">
    <div id="chart_div" style="width: 1200px; height: 500px;"></div>
  </body>
</html>
