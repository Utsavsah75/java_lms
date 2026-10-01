package com.library.controller;

import com.library.dao.IssueDAO;
import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.service.StudentService;
import com.library.util.BusinessException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * reg_students.php, add.php + add_user_data.php, edit_reg_students.php / newuser_update.php,
 * all_member.php, confirm_member.php, students_detail.php
 */
@WebServlet(name = "StudentAdminServlet", urlPatterns = {"/admin/students", "/admin/students/add", "/admin/students/edit",
        "/admin/students/status", "/admin/students/delete", "/admin/students/history"})
@MultipartConfig(maxFileSize = 2 * 1024 * 1024, maxRequestSize = 3 * 1024 * 1024)
public class StudentAdminServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final StudentDAO studentDAO = new StudentDAO();
    private final IssueDAO issueDAO = new IssueDAO();
    private final StudentService service = new StudentService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        switch (req.getServletPath()) {
            case "/admin/students":
                req.setAttribute("q", param(req, "q"));
                req.setAttribute("students", studentDAO.findAll(param(req, "q")));
                view(req, resp, "admin/students");
                break;
            case "/admin/students/add":
                view(req, resp, "admin/student-add");
                break;
            case "/admin/students/edit":
                Student s = studentDAO.findById(intParam(req, "id"));
                if (s == null) {
                    error(req, "Student not found.");
                    redirect(req, resp, "/admin/students");
                    return;
                }
                req.setAttribute("student", s);
                view(req, resp, "admin/student-edit");
                break;
            case "/admin/students/history":
                Student h = studentDAO.findByCode(param(req, "code"));
                if (h == null) {
                    error(req, "Student not found.");
                    redirect(req, resp, "/admin/students");
                    return;
                }
                req.setAttribute("student", h);
                req.setAttribute("issues", issueDAO.findByStudent(h.getStudentCode()));
                view(req, resp, "admin/student-history");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int id = intParam(req, "id");
        switch (req.getServletPath()) {
            case "/admin/students/add":
                try {
                    String code = service.addByAdmin(param(req, "fullname"), param(req, "address"), param(req, "phone"),
                            param(req, "gender"), param(req, "email"), req.getParameter("password"), req.getPart("photo"));
                    success(req, "New student added successfully with student code " + code + ".");
                    redirect(req, resp, "/admin/students");
                } catch (BusinessException e) {
                    req.setAttribute("flashError", e.getMessage());
                    view(req, resp, "admin/student-add");
                }
                break;
            case "/admin/students/edit":
                try {
                    service.update(id, param(req, "fullname"), param(req, "address"), param(req, "phone"), param(req, "gender"),
                            param(req, "email"), param(req, "status"));
                    success(req, "Student details updated successfully.");
                    redirect(req, resp, "/admin/students");
                } catch (BusinessException e) {
                    req.setAttribute("student", studentDAO.findById(id));
                    req.setAttribute("flashError", e.getMessage());
                    view(req, resp, "admin/student-edit");
                }
                break;
            case "/admin/students/status":
                boolean activate = "activate".equals(param(req, "action"));
                try {
                    service.setActive(id, activate);
                    success(req, activate ? "Student status activated successfully at id " + id + "."
                            : "Student status blocked successfully at id " + id + ".");
                } catch (BusinessException e) {
                    error(req, e.getMessage());
                }
                redirect(req, resp, "/admin/students");
                break;
            case "/admin/students/delete":
                try {
                    service.delete(id);
                    success(req, "Student deleted successfully.");
                } catch (BusinessException e) {
                    error(req, e.getMessage());
                }
                redirect(req, resp, "/admin/students");
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
}
