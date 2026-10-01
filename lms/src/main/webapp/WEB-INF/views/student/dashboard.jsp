<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:student title="Student" subtitle="Dashboard" active="dashboard">
  <div class="row g-4">
    <div class="col-md-4 col-sm-6">
      <div class="alert alert-primary back-widget-set text-center">
        <i class="fa fa-book fa-5x"></i>
        <h4>${stats.books}</h4>Books Listed
        <div class="footer1"><a class="btn btn-primary" href="${ctx}/student/books">View Details</a></div>
      </div>
    </div>
    <div class="col-md-4 col-sm-6">
      <div class="alert alert-warning back-widget-set text-center">
        <i class="fa fa-recycle fa-5x"></i>
        <h4>${stats.notReturned}</h4>Books Not Returned Yet
        <div class="footer1"><a class="btn btn-warning" href="${ctx}/student/issued">View Details</a></div>
      </div>
    </div>
    <div class="col-md-4 col-sm-6">
      <div class="alert alert-success back-widget-set text-center">
        <i class="fa fa-history fa-5x"></i>
        <h4>${stats.issues}</h4>Books Borrowed So Far
        <div class="footer1"><a class="btn btn-success" href="${ctx}/student/issued">View Details</a></div>
      </div>
    </div>
  </div>
</t:student>
