<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>
</head>
<a href="<c:url value="/"/>">Home</a>
<body>
	<h1>Item List</h1>


	<P>The allergens are </P>
		<c:forEach var="allergen" items="${allergens}" >
			<li><c:out value="${ allergen.name }"/>:<c:out value="${ allergen.value }"/> 
	</c:forEach>
	
</body>
</html>