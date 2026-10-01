<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Add" subtitle="Book" active="book">
  <div class="row"><div class="col-md-8 offset-md-2">
    <div class="panel-box">
      <div class="panel-head">Book Info</div>
      <div class="panel-body">
        <form method="post" action="${ctx}/admin/books/add" enctype="multipart/form-data" class="row g-3">
          <input type="hidden" name="_csrf" value="${csrfToken}">
          <div class="col-12"><label class="form-label" for="bookname">Book Name</label>
            <input class="form-control" type="text" id="bookname" name="bookname" maxlength="255" autocomplete="off" required value="<c:out value='${param.bookname}'/>"></div>
          <div class="col-md-6"><label class="form-label" for="category">Category</label>
            <select class="form-select" id="category" name="category" required>
              <option value="">Select Category</option>
              <c:forEach var="c" items="${categories}">
                <c:if test="${c.active}"><option value="${c.id}" ${param.category == c.id ? 'selected' : ''}><c:out value="${c.name}"/></option></c:if>
              </c:forEach>
            </select></div>
          <div class="col-md-6"><label class="form-label" for="author">Author</label>
            <select class="form-select" id="author" name="author" required>
              <option value="">Select Author</option>
              <c:forEach var="a" items="${authors}"><option value="${a.id}" ${param.author == a.id ? 'selected' : ''}><c:out value="${a.name}"/></option></c:forEach>
            </select></div>
          <div class="col-md-6"><label class="form-label" for="isbnno">ISBN / Book ID</label>
            <input class="form-control" type="text" id="isbnno" name="isbnno" maxlength="25" autocomplete="off" required
                   value="<c:out value='${param.isbnno}'/>" data-check="isbn" data-target="#isbnMsg">
            <div id="isbnMsg" class="form-text">An ISBN must be unique. Issuing and returning use it to find the book.</div></div>
          <div class="col-md-3"><label class="form-label" for="price">Price</label>
            <input class="form-control" type="number" step="0.01" min="0" id="price" name="price" autocomplete="off" required value="<c:out value='${param.price}'/>"></div>
          <div class="col-md-3"><label class="form-label" for="quantity">Quantity</label>
            <input class="form-control" type="number" min="1" id="quantity" name="quantity" autocomplete="off" required value="<c:out value='${param.quantity}'/>"></div>
          <div class="col-12"><label class="form-label" for="bookpic">Book Picture</label>
            <input class="form-control" type="file" id="bookpic" name="bookpic" accept="image/png,image/jpeg,image/gif" required>
            <div class="form-text">jpg, png or gif, up to 2 MB.</div></div>
          <div class="col-12">
            <button type="submit" name="add" class="btn btn-info"><i class="fa fa-save"></i> Add</button>
            <a class="btn btn-outline-secondary" href="${ctx}/admin/books">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  </div></div>
</t:admin>
