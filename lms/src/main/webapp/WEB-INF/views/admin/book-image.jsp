<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Change" subtitle="Book Image" active="book">
  <div class="row"><div class="col-md-6 offset-md-3">
    <div class="panel-box">
      <div class="panel-head">Book Image</div>
      <div class="panel-body text-center">
        <h5><c:out value="${book.name}"/></h5>
        <img class="book-cover my-3" src="${ctx}/uploads/<c:out value='${book.image}'/>" alt="Current cover" onerror="this.style.visibility='hidden'">
        <form method="post" action="${ctx}/admin/books/image" enctype="multipart/form-data" class="text-start">
          <input type="hidden" name="_csrf" value="${csrfToken}">
          <input type="hidden" name="id" value="${book.id}">
          <div class="mb-3"><label class="form-label" for="bookpic">New picture</label>
            <input class="form-control" type="file" id="bookpic" name="bookpic" accept="image/png,image/jpeg,image/gif" required>
            <div class="form-text">jpg, jpeg, png or gif, up to 2 MB.</div></div>
          <button type="submit" name="update" class="btn btn-info"><i class="fa fa-upload"></i> Update</button>
          <a class="btn btn-outline-secondary" href="${ctx}/admin/books">Cancel</a>
        </form>
      </div>
    </div>
  </div></div>
</t:admin>
