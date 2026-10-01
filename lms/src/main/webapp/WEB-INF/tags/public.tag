<%@ tag description="Layout for pages visitors see before login (index.php, admin_login.php, user_form.php ...)" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="hero" required="false" type="java.lang.Boolean" description="true = big slider with a card on the right" %>
<%@ attribute name="heading" required="false" %>
<%@ attribute name="sub" required="false" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><c:out value="${empty title ? 'Library Management System' : title}"/> | Public Library</title>
  <link rel="stylesheet" href="${ctx}/vendor/bootstrap/bootstrap.min.css">
  <link rel="stylesheet" href="${ctx}/vendor/fontawesome/css/font-awesome.min.css">
  <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body data-ctx="${ctx}">
<div id="background">
  <div id="top">
    <a href="#" class="fa fa-envelope"> public@gmail.com</a>
    <a href="#" class="fa fa-phone"> +977-98012345678</a>
    <a href="#" class="fa fa-twitter social"></a>
    <a href="#" class="fa fa-instagram social"></a>
    <a href="#" class="fa fa-facebook social"></a>
  </div>
  <div id="menu">
    <div id="logo"><b style="color:#2c7ad6">PUBLIC</b>LIBRARY</div>
    <div id="menu1">
      <ul>
        <a href="${ctx}/home"><li class="fa fa-home"> Home</li></a>
        <a href="${ctx}/admin-login"><li class="fa fa-user"> Admin Login</li></a>
        <a href="${ctx}/register"><li class="fa fa-registered"> Register</li></a>
        <a href="${ctx}/about"><li>About Us</li></a>
      </ul>
    </div>
  </div>

  <c:choose>
    <c:when test="${hero}">
      <div id="slider">
        <div id="x">WELCOME TO <b style="color:#2c7ad6">PUBLIC</b>LIBRARY
          <p>YOU CAN READ SOME&nbsp;</p>
        </div>
        <div id="y">
          <div id="l">
            <t:alerts/>
            <h1><c:out value="${heading}"/></h1>
            <h3><c:out value="${sub}"/></h3>
            <jsp:doBody/>
          </div>
        </div>
      </div>
    </c:when>
    <c:otherwise>
      <div class="public-page">
        <div class="container">
          <div class="public-card">
            <t:alerts/>
            <jsp:doBody/>
          </div>
        </div>
      </div>
    </c:otherwise>
  </c:choose>
</div>
<t:footer/>
<script src="${ctx}/vendor/jquery/jquery.min.js"></script>
<script src="${ctx}/vendor/bootstrap/bootstrap.bundle.min.js"></script>
<script src="${ctx}/js/app.js"></script>
</body>
</html>
