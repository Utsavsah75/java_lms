<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Issue" subtitle="Book" active="issue">
  <div class="row"><div class="col-md-8 offset-md-2">
    <div class="panel-box">
      <div class="panel-head">Issue a New Book</div>
      <div class="panel-body">
        <form method="post" action="${ctx}/admin/issue" class="row g-3">
          <input type="hidden" name="_csrf" value="${csrfToken}">
          <div class="col-12"><label class="form-label" for="studentCode">Student Code</label>
            <select class="form-select" id="studentCode" name="studentCode" required>
              <option value="">Select student</option>
              <c:forEach var="s" items="${students}"><option value="${s.studentCode}" ${param.studentCode == s.studentCode ? 'selected' : ''}><c:out value="${s.studentCode}"/> - <c:out value="${s.name}"/></option></c:forEach>
            </select>
            <div id="studentInfo" class="form-text"></div></div>
          <div class="col-12"><label class="form-label" for="isbn">Book ISBN / Book ID</label>
            <select class="form-select" id="isbn" name="isbn" required>
              <option value="">Select book</option>
              <c:forEach var="b" items="${books}">
                <option value="${b.isbn}" ${param.isbn == b.isbn ? 'selected' : ''} ${b.copiesAvailable == 0 ? 'class="text-danger"' : ''}><c:out value="${b.isbn}"/> - <c:out value="${b.name}"/> (${b.copiesAvailable} available)</option>
              </c:forEach>
            </select>
            <div id="bookInfo" class="form-text"></div></div>
          <div class="col-12">
            <button type="submit" name="issue" class="btn btn-info"><i class="fa fa-book"></i> Issue Book</button>
            <a class="btn btn-outline-secondary" href="${ctx}/admin/issues">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  </div></div>
</t:admin>
