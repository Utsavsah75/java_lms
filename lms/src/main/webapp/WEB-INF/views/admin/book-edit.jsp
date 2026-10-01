<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:admin title="Update" subtitle="Book" active="book">
  <div class="row"><div class="col-md-8 offset-md-2">
    <div class="panel-box">
      <div class="panel-head">Book Info</div>
      <div class="panel-body">
        <form method="post" action="${ctx}/admin/books/edit" class="row g-3">
          <input type="hidden" name="_csrf" value="${csrfToken}">
          <input type="hidden" name="id" value="${book.id}">
          <div class="col-md-3 text-center">
            <img class="book-cover" src="${ctx}/uploads/<c:out value='${book.image}'/>" alt="Book cover" onerror="this.style.visibility='hidden'">
            <div class="mt-2"><a href="${ctx}/admin/books/image?id=${book.id}">Change image</a></div>
          </div>
          <div class="col-md-9 row g-3 m-0 p-0">
            <div class="col-12"><label class="form-label" for="bookname">Book Name</label>
              <input class="form-control" type="text" id="bookname" name="bookname" maxlength="255" required value="<c:out value='${empty param.bookname ? book.name : param.bookname}'/>"></div>
            <div class="col-md-6"><label class="form-label" for="catid">Category</label>
              <select class="form-select" id="catid" name="catid" required>
                <c:set var="selCat" value="${empty param.catid ? book.categoryId : param.catid}"/>
                <c:forEach var="c" items="${categories}">
                  <c:if test="${c.active or c.id == book.categoryId}"><option value="${c.id}" ${selCat == c.id ? 'selected' : ''}><c:out value="${c.name}"/></option></c:if>
                </c:forEach>
              </select></div>
            <div class="col-md-6"><label class="form-label" for="authorid">Author</label>
              <select class="form-select" id="authorid" name="authorid" required>
                <c:set var="selAut" value="${empty param.authorid ? book.authorId : param.authorid}"/>
                <c:forEach var="a" items="${authors}"><option value="${a.id}" ${selAut == a.id ? 'selected' : ''}><c:out value="${a.name}"/></option></c:forEach>
              </select></div>
            <div class="col-md-4"><label class="form-label" for="isbn">ISBN / Book ID</label>
              <input class="form-control" type="text" id="isbn" value="<c:out value='${book.isbn}'/>" readonly></div>
            <div class="col-md-4"><label class="form-label" for="bookprice">Price</label>
              <input class="form-control" type="number" step="0.01" min="0" id="bookprice" name="bookprice" required value="<c:out value='${empty param.bookprice ? book.price : param.bookprice}'/>"></div>
            <div class="col-md-4"><label class="form-label" for="quantity">Quantity</label>
              <input class="form-control" type="number" min="1" id="quantity" name="quantity" required value="<c:out value='${empty param.quantity ? book.quantity : param.quantity}'/>">
              <div class="form-text">${book.issuedCount} copy(ies) currently issued.</div></div>
          </div>
          <div class="col-12">
            <button type="submit" name="submit" class="btn btn-info"><i class="fa fa-save"></i> Update</button>
            <a class="btn btn-outline-secondary" href="${ctx}/admin/books">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  </div></div>
</t:admin>
