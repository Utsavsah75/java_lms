<%@ tag description="Bootstrap alerts for flash messages" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${not empty flashSuccess}">
  <div class="alert alert-success alert-dismissible fade show" role="alert">
    <i class="fa fa-check-circle"></i> <c:out value="${flashSuccess}"/>
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
  </div>
</c:if>
<c:if test="${not empty flashError}">
  <div class="alert alert-danger alert-dismissible fade show" role="alert">
    <i class="fa fa-exclamation-triangle"></i> <c:out value="${flashError}"/>
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
  </div>
</c:if>
