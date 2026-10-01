/* Small helpers used by the JSP pages (no inline scripts, so a strict CSP is possible later) */
(function () {
  "use strict";

  // confirm before any form/button that carries data-confirm
  document.addEventListener("submit", function (e) {
    var msg = e.target.getAttribute("data-confirm");
    if (msg && !window.confirm(msg)) { e.preventDefault(); }
  });

  // DataTables on every table.data-table (search, sort, paging - as in the PHP admin pages)
  if (window.jQuery && jQuery.fn.DataTable) {
    jQuery("table.data-table").each(function () {
      jQuery(this).DataTable({ pageLength: 10, order: [], columnDefs: [{ targets: "no-sort", orderable: false }] });
    });
  }

  // password match check (registration / change password)
  document.querySelectorAll("form[data-match]").forEach(function (form) {
    form.addEventListener("submit", function (e) {
      var pair = form.getAttribute("data-match").split(",");
      var a = form.querySelector("[name=" + pair[0] + "]");
      var b = form.querySelector("[name=" + pair[1] + "]");
      if (a && b && a.value !== b.value) {
        e.preventDefault();
        window.alert("Password and Confirm Password do not match.");
        b.focus();
      }
    });
  });

  // live availability check (replaces check_avability.php): <input data-check="email" data-target="#msg">
  document.querySelectorAll("input[data-check]").forEach(function (input) {
    var target = document.querySelector(input.getAttribute("data-target"));
    if (!target) { return; }
    input.addEventListener("blur", function () {
      var v = input.value.trim();
      if (!v) { target.textContent = ""; return; }
      var base = document.body.getAttribute("data-ctx") || "";
      fetch(base + "/ajax/check?type=" + encodeURIComponent(input.getAttribute("data-check")) +
            "&value=" + encodeURIComponent(v), { credentials: "same-origin" })
        .then(function (r) { return r.json(); })
        .then(function (d) {
          target.textContent = d.message || "";
          target.className = "form-text " + (d.ok ? "text-success" : "text-danger");
        })
        .catch(function () { target.textContent = ""; });
    });
  });

  // issue-book page: show student / book details when the field loses focus
  var pairs = [["#studentCode", "student", "#studentInfo"], ["#isbn", "book", "#bookInfo"]];
  pairs.forEach(function (p) {
    var el = document.querySelector(p[0]);
    var box = document.querySelector(p[2]);
    if (!el || !box) { return; }
    var load = function () {
      var v = el.value.trim();
      if (!v) { box.innerHTML = ""; return; }
      var base = document.body.getAttribute("data-ctx") || "";
      fetch(base + "/ajax/check?type=" + p[1] + "&value=" + encodeURIComponent(v), { credentials: "same-origin" })
        .then(function (r) { return r.json(); })
        .then(function (d) {
          box.textContent = d.message || "";
          box.className = "form-text " + (d.ok ? "text-success" : "text-danger");
        });
    };
    el.addEventListener("change", load);
    el.addEventListener("blur", load);
  });
})();
