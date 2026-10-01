<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<t:public title="About Us" hero="false">
  <h2 class="text-center" style="color:#2c7ad6">OUR TEAM</h2>
  <div class="team-wrap">
    <div class="team-card"><img src="${ctx}/images/team/bhawana.jpg" alt="Bhawana Sharma"><p class="name">Bhawana Sharma</p><p>Roll No = 10</p></div>
    <div class="team-card"><img src="${ctx}/images/team/pravha.jpg" alt="Prabha Tamang"><p class="name">Prabha Tamang</p><p>Roll No = 31</p></div>
    <div class="team-card"><img src="${ctx}/images/team/ranjit.jpg" alt="Ranjit Kumar Sah"><p class="name">Ranjit Kumar Sah</p><p>Roll No = 36</p></div>
    <div class="team-card"><img src="${ctx}/images/team/utsav.jpg" alt="Utsav Prasad Sah"><p class="name">Utsav Prasad Sah</p><p>Roll No = 48</p></div>
  </div>
</t:public>
