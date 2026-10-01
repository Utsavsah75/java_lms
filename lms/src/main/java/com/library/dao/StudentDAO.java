package com.library.dao;

import com.library.model.Student;
import com.library.util.DBConnection;
import com.library.util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Table students = library members  (PHP: reg_students.php, all_member.php, user_login.php, user_form.php ...). */
public class StudentDAO extends BaseDAO {

    /** Result of a login attempt. */
    public enum LoginResult { OK, BAD_CREDENTIALS, NOT_ACTIVE }

    public static final class LoginOutcome {
        public final LoginResult result;
        public final Student student;

        LoginOutcome(LoginResult result, Student student) {
            this.result = result;
            this.student = student;
        }
    }

    private static Student map(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setStudentCode(rs.getString("studentcode"));
        s.setName(rs.getString("std_name"));
        s.setAddress(rs.getString("address"));
        s.setPhone(rs.getString("phone"));
        s.setGender(rs.getString("gender"));
        s.setPhoto(rs.getString("photo"));
        s.setEmail(rs.getString("email"));
        s.setVerified(rs.getInt("verified"));
        s.setRegDate(date(rs, "regdate"));
        s.setUpdationDate(date(rs, "updationdate"));
        return s;
    }

    public LoginOutcome authenticate(String email, String password) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT * FROM students WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String stored = rs.getString("password");
                    if (!PasswordUtil.verify(password, stored)) {
                        continue;
                    }
                    Student s = map(rs);
                    if (!s.isActive()) {
                        return new LoginOutcome(LoginResult.NOT_ACTIVE, s);
                    }
                    if (PasswordUtil.needsUpgrade(stored)) {
                        setPassword(c, s.getId(), password);
                    }
                    return new LoginOutcome(LoginResult.OK, s);
                }
            }
            return new LoginOutcome(LoginResult.BAD_CREDENTIALS, null);
        } catch (SQLException e) {
            throw wrap("Student login", e);
        }
    }

    public List<Student> findAll(String q) {
        boolean filter = q != null && !q.isBlank();
        String sql = "SELECT * FROM students"
                + (filter ? " WHERE studentcode LIKE ? OR std_name LIKE ? OR email LIKE ? OR phone LIKE ?" : "")
                + " ORDER BY id";
        List<Student> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (filter) {
                String like = "%" + q.trim() + "%";
                for (int i = 1; i <= 4; i++) {
                    ps.setString(i, like);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            throw wrap("Student list", e);
        }
    }

    /** Active students only - used by the Issue Book drop-down. */
    public List<Student> findActive() {
        List<Student> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT * FROM students WHERE verified = 1 ORDER BY studentcode");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw wrap("Active student list", e);
        }
    }

    public Student findById(int id) {
        return findOne("SELECT * FROM students WHERE id = ?", Integer.toString(id), true);
    }

    public Student findByCode(String code) {
        return findOne("SELECT * FROM students WHERE studentcode = ?", code, false);
    }

    private Student findOne(String sql, String value, boolean numeric) {
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (numeric) {
                ps.setInt(1, Integer.parseInt(value));
            } else {
                ps.setString(1, value);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw wrap("Student lookup", e);
        }
    }

    public boolean emailExists(String email, int exceptId) {
        return exists("SELECT COUNT(*) FROM students WHERE LOWER(email) = LOWER(?) AND id <> ?", email, exceptId);
    }

    public boolean phoneExists(String phone, int exceptId) {
        return exists("SELECT COUNT(*) FROM students WHERE phone = ? AND id <> ?", phone, exceptId);
    }

    public boolean codeExists(String code) {
        return exists("SELECT COUNT(*) FROM students WHERE studentcode = ? AND id <> ?", code, 0);
    }

    private boolean exists(String sql, String value, int exceptId) {
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setInt(2, exceptId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw wrap("Student duplicate check", e);
        }
    }

    /** Next free code SID001, SID002 ... (based on the highest existing number, so deletes never cause clashes). */
    public String nextStudentCode() {
        String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(studentcode, 4) AS UNSIGNED)), 0) FROM students WHERE studentcode LIKE 'SID%'";
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            int n = rs.next() ? rs.getInt(1) : 0;
            return String.format("SID%03d", n + 1);
        } catch (SQLException e) {
            throw wrap("Student code", e);
        }
    }

    public boolean insert(Student s, String plainPassword, String verificationCode) {
        String sql = "INSERT INTO students (studentcode, std_name, address, phone, gender, photo, email, password, "
                + "verification_code, verified) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getStudentCode());
            ps.setString(2, s.getName());
            ps.setString(3, s.getAddress());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getGender());
            ps.setString(6, s.getPhoto());
            ps.setString(7, s.getEmail());
            ps.setString(8, PasswordUtil.hash(plainPassword));
            ps.setString(9, verificationCode);
            ps.setInt(10, s.getVerified());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Student insert", e);
        }
    }

    public boolean update(Student s) {
        String sql = "UPDATE students SET std_name = ?, address = ?, phone = ?, gender = ?, email = ?, verified = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getAddress());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getGender());
            ps.setString(5, s.getEmail());
            ps.setInt(6, s.getVerified());
            ps.setInt(7, s.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Student update", e);
        }
    }

    public boolean updatePhoto(int id, String photo) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("UPDATE students SET photo = ? WHERE id = ?")) {
            ps.setString(1, photo);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Student photo update", e);
        }
    }

    public boolean setVerified(int id, int verified) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("UPDATE students SET verified = ? WHERE id = ?")) {
            ps.setInt(1, verified);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Student status", e);
        }
    }

    /** verify_email.php : 1 = verified now, 2 = already verified, 0 = unknown code. */
    public int verifyByCode(String code) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT id, verified FROM students WHERE verification_code = ? LIMIT 1")) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return 0;
                }
                if (rs.getInt("verified") == 1) {
                    return 2;
                }
                setVerified(rs.getInt("id"), 1);
                return 1;
            }
        } catch (SQLException e) {
            throw wrap("Email verification", e);
        }
    }

    public boolean delete(int id) {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("DELETE FROM students WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Student delete", e);
        }
    }

    public int countActiveIssues(String studentCode) {
        String sql = "SELECT COUNT(*) FROM issue_book WHERE studentcode = ? AND (retrunstatus IS NULL OR retrunstatus = 0)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, studentCode);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw wrap("Student issue count", e);
        }
    }

    private void setPassword(Connection c, int id, String plain) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("UPDATE students SET password = ? WHERE id = ?")) {
            ps.setString(1, PasswordUtil.hash(plain));
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
}
