<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body ng-controller="moniteringCtrl"  class="pad">
	<c:url value="/logout" var="logoutUrl" />
	<!-- csrt for log out-->
	<form action="${logoutUrl}" method="post" id="logoutForm">
	  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
 	<script>
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>

 	<!------------------------     navigation   ------------------------>
    <form class="navbar-form navbar-right" >
	    <span style="color: gray;" ><h5> ${username} �� �ݰ����ϴ�. 
        <a href = "javascript:formSubmit()"> �α׾ƿ� </a> </h5></span>
    </form>
</html>
