<html>
  <head>
    <!-- script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script-->
    <script type="text/javascript" src="/resources/js/google/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawVisualization);


      function drawVisualization() {
        // Some raw data (not necessarily accurate)
        var data = google.visualization.arrayToDataTable([
         ['Month', 'Bolivia', 'Ecuador', 'Madagascar', 'Papua New Guinea', 'Rwanda', 'Average'],
         ['증권번호조회',  165,      938,         522,             998,           450,      614.6],
         ['가입설계(저축성보험)',  135,      1120,        599,             1268,          288,      682],
         ['가입설계(보장성보험)',  157,      1167,        587,             807,           397,      623],
         ['상품설명서-보장내용',  139,      1110,        615,             968,           215,      609.4],
         ['계약사항 특약조회',  136,      691,         629,             1026,          366,      569.6],
         ['휴면보험금 미지급내역조회',  136,      691,         629,             1026,          366,      569.6],
         ['계약상세조회',  136,      691,         629,             1026,          366,      569.6],
         ['생존보험금상세조회',  136,      691,         629,             1026,          366,      569.6],
         ['보험계약대출 처리내역조회',  136,      691,         629,             1026,          366,      569.6],
         ['배당금 지급내역조회',  136,      691,         629,             1026,          366,      569.6],
         ['보험계약대출 수납내역조회',  136,      691,         629,             1026,          366,      569.6],
         ['해약금조회',  136,      691,         629,             1026,          366,      569.6],
         ['인출 신청 대상조회',  136,      691,         629,             1026,          366,      569.6],
         ['보험계약대출 가능금액조회',  136,      691,         629,             1026,          366,      569.6],
         ['보험계약대출 원리금조회',  136,      691,         629,             1026,          366,      569.6],
         ['사고보험금 지급내역조회',  136,      691,         629,             1026,          366,      569.6],
         ['사고보험금 접수사항조회',  136,      691,         629,             1026,          366,      569.6],
         ['인터페이스 리로드',  136,      691,         629,             1026,          366,      569.6],
         ['변액보험 수익률 조회	',  136,      691,         629,             1026,          366,      569.6],
         ['펀드가입내역조회',  136,      691,         629,             1026,          366,      569.6],
         ['보험료 계산',  136,      691,         629,             1026,          366,      569.6],
         ['예상납입보험료조회',  136,      691,         629,             1026,          366,      569.6],
         ['소득공제용 보험료 납입내역 조회',  136,      691,         629,             1026,          366,      569.6],
         ['부활 대상건 조회',  136,      691,         629,             1026,          366,      569.6],
         ['사고보험금 진행사항조회',  136,      691,         629,             1026,          366,      569.6]         
      ]);

    var options = {
      title : 'Monthly Coffee Production by Country',
      vAxis: {title: 'Cups'},
      hAxis: {title: 'Month'},
      seriesType: 'bars',
      series: {5: {type: 'line'}}
    };

    var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
    chart.draw(data, options);
  }
    </script>
  </head>
  <body>
    <div id="chart_div" style="width: 1200px; height: 500px;"></div>
  </body>
</html>
