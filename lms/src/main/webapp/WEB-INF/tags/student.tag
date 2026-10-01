<%@ tag description="Layout for the student portal (user_header.php)" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="subtitle" required="false" %>
<%@ attribute name="active" required="false" description="dashboard | books | issued" %>
<%@ attribute name="tables" required="false" type="java.lang.Boolean" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><c:out value="${title}"/> | Public Library</title>
  <link rel="stylesheet" href="${ctx}/vendor/bootstrap/bootstrap.min.css">
  <link rel="stylesheet" href="${ctx}/vendor/fontawesome/css/font-awesome.min.css">
  <c:if test="${tables}"><link rel="stylesheet" href="${ctx}/vendor/datatables/dataTables.bootstrap5.min.css"></c:if>
  <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body data-ctx="${ctx}">
<section class="menu-section">
  <div class="container d-flex justify-content-between align-items-center">
    <a class="navbar-brand" href="${ctx}/student/dashboard"><img src="${ctx}/images/logo.png" alt="Public Library"></a>
    <div>
      <span class="me-3 text-white d-none d-md-inline"><i class="fa fa-user"></i> <c:out value="${sessionScope.studentName}"/> (<c:out value="${sessionScope.studentCode}"/>)</span>
      <a href="${ctx}/logout" class="btn btn-logout"><i class="fa fa-sign-out"></i> LOG ME OUT</a>
    </div>
  </div>
</section>
<nav class="navbar navbar-expand-lg main-nav">
  <div class="container">
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav" aria-controls="mainNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="mainNav">
      <ul class="navbar-nav">
        <li class="nav-item"><a class="nav-link ${active == 'dashboard' ? 'active' : ''}" href="${ctx}/student/dashboard">Dashboard</a></li>
        <li class="nav-item"><a class="nav-link ${active == 'books' ? 'active' : ''}" href="${ctx}/student/books">Book List</a></li>
        <li class="nav-item"><a class="nav-link ${active == 'issued' ? 'active' : ''}" href="${ctx}/student/issued">Issued Book</a></li>
      </ul>
    </div>
  </div>
</nav>
<div class="container my-4">
  <div class="header-line">
    <img src="${ctx}/images/logo1.png" alt="" style="height:40px;width:auto;margin-right:8px">
    <span style="color:#000"><c:out value="${title}"/></span>
    <c:if test="${not empty subtitle}"><span class="accent"><c:out value="${subtitle}"/></span></c:if>
  </div>
  <t:alerts/>
  <jsp:doBody/>
</div>
<t:footer/>
<script src="${ctx}/vendor/jquery/jquery.min.js"></script>
<script src="${ctx}/vendor/bootstrap/bootstrap.bundle.min.js"></script>
<c:if test="${tables}">
  <script src="${ctx}/vendor/datatables/dataTables.min.js"></script>
  <script src="${ctx}/vendor/datatables/dataTables.bootstrap5.min.js"></script>
</c:if>
<script src="${ctx}/js/app.js"></script>
</body>
</html>
