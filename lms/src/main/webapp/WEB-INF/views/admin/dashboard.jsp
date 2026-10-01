<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Admin" subtitle="Dashboard" active="dashboard">
  <div class="row g-4">
    <div class="col-md-4 col-sm-6">
      <div class="alert alert-primary back-widget-set text-center">
        <i class="fa fa-book fa-5x"></i>
        <h4>${stats.books}</h4>Books Listed
        <div class="footer1"><a class="btn btn-primary" href="${ctx}/admin/books">View Details</a></div>
      </div>
    </div>
    <div class="col-md-4 col-sm-6">
      <div class="alert alert-info back-widget-set text-center">
        <i class="fa fa-users fa-5x"></i>
        <h4>${stats.students}</h4>Users Listed
        <div class="footer1"><a class="btn btn-info" href="${ctx}/admin/students">View Details</a></div>
      </div>
    </div>
    <div class="col-md-4 col-sm-6">
      <div class="alert alert-success back-widget-set text-center">
        <i class="fa fa-book fa-5x"></i>
        <h4>${stats.issues}</h4>Books Issued
        <div class="footer1"><a class="btn btn-success" href="${ctx}/admin/issues">View Details</a></div>
      </div>
    </div>
    <div class="col-md-4 col-sm-6">
      <div class="alert alert-danger back-widget-set text-center">
        <i class="fa fa-book fa-5x"></i>
        <h4>${stats.authors}</h4>Authors Listed
        <div class="footer1"><a class="btn btn-danger" href="${ctx}/admin/authors">View Details</a></div>
      </div>
    </div>
    <div class="col-md-4 col-sm-6">
      <div class="alert alert-warning back-widget-set text-center">
        <i class="fa fa-book fa-5x"></i>
        <h4>${stats.categories}</h4>Categories Listed
        <div class="footer1"><a class="btn btn-warning" href="${ctx}/admin/categories">View Details</a></div>
      </div>
    </div>
    <div class="col-md-4 col-sm-6">
      <div class="alert alert-secondary back-widget-set text-center">
        <i class="fa fa-money fa-5x"></i>
        <h4>Rs. ${stats.totalFine}</h4>Total Fine Collection
      </div>
    </div>
  </div>
</t:admin>
