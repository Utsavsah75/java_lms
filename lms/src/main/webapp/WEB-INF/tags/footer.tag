<%@ tag description="Animated wave footer (footer.html)" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<footer class="site-footer">
  <div class="footer-waves">
    <div class="wave" id="wave1"></div><div class="wave" id="wave2"></div>
    <div class="wave" id="wave3"></div><div class="wave" id="wave4"></div>
  </div>
  <h3 class="mt-2">Stay Connected</h3>
  <p class="mt-3">
    <a href="#"><img class="footer-icons" src="${ctx}/images/facebook.png" alt="facebook"></a>
    <a href="#"><img class="footer-icons" src="${ctx}/images/instagram.png" alt="instagram"></a>
    <a href="#"><img class="footer-icons" src="${ctx}/images/github.png" alt="github"></a>
    <a href="#"><img class="footer-icons" src="${ctx}/images/twiter.png" alt="twitter"></a>
  </p>
  <h2>Contact Address</h2>
  <p><a href="#" class="fa fa-phone"> +977-98012345678</a></p>
  <p><a href="#" class="fa fa-envelope"> public@gmail.com</a></p>
  <div class="developed">Developed By : Utsav Prasad Sah</div>
</footer>
