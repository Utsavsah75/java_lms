<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Registered" subtitle="Students" active="student" tables="true">
  <div class="mb-3"><a class="btn btn-info" href="${ctx}/admin/students/add"><i class="fa fa-user-plus"></i> Add Student</a></div>
  <div class="panel-box">
    <div class="panel-head">Students Listing</div>
    <div class="panel-body table-responsive">
      <table class="table table-striped table-bordered table-hover data-table align-middle">
        <thead><tr><th>#</th><th class="no-sort">Photo</th><th>Student Code</th><th>Name</th><th>E-mail</th><th>Mobile</th><th>Reg. Date</th><th>Status</th><th class="no-sort">Action</th></tr></thead>
        <tbody>
        <c:forEach var="s" items="${students}" varStatus="st">
          <tr>
            <td>${st.count}</td>
            <td><c:if test="${not empty s.photo}"><img class="student-thumb" src="${ctx}/uploads/<c:out value='${s.photo}'/>" alt="" onerror="this.style.visibility='hidden'"></c:if></td>
            <td><c:out value="${s.studentCode}"/></td>
            <td><c:out value="${s.name}"/></td>
            <td><c:out value="${s.email}"/></td>
            <td><c:out value="${s.phone}"/></td>
            <td><fmt:formatDate value="${s.regDate}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td>
              <form method="post" action="${ctx}/admin/students/status" class="d-inline">
                <input type="hidden" name="_csrf" value="${csrfToken}"><input type="hidden" name="id" value="${s.id}">
                <input type="hidden" name="action" value="${s.active ? 'block' : 'activate'}">
                <c:choose>
                  <c:when test="${s.active}"><button class="btn btn-success btn-sm" title="Click to block">Active</button></c:when>
                  <c:otherwise><button class="btn btn-danger btn-sm" title="Click to activate">Blocked</button></c:otherwise>
                </c:choose>
              </form>
            </td>
            <td class="action-col">
              <a class="btn btn-secondary btn-sm" href="${ctx}/admin/students/history?code=<c:out value='${s.studentCode}'/>"><i class="fa fa-history"></i> Details</a>
              <a class="btn btn-primary btn-sm" href="${ctx}/admin/students/edit?id=${s.id}"><i class="fa fa-edit"></i> Edit</a>
              <form method="post" action="${ctx}/admin/students/delete" class="d-inline" data-confirm="Delete student ${fn:escapeXml(s.name)}?">
                <input type="hidden" name="_csrf" value="${csrfToken}"><input type="hidden" name="id" value="${s.id}">
                <button class="btn btn-danger btn-sm"><i class="fa fa-trash"></i></button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <c:if test="${empty students}"><p class="text-muted mb-0">No record found.</p></c:if>
    </div>
  </div>
</t:admin>
