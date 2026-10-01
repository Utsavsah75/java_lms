package com.library.dao;

import com.library.model.Admin;
import com.library.util.DBConnection;
import com.library.util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Table admin_login  (PHP: admin_page.php, Admin_loginserver.php, change_pwd.php). */
public class AdminDAO extends BaseDAO {

    private static Admin map(ResultSet rs) throws SQLException {
        Admin a = new Admin();
        a.setId(rs.getInt("id"));
        a.setName(rs.getString("name"));
        a.setAddress(rs.getString("address"));
        a.setContact(rs.getLong("contact"));
        a.setGender(rs.getString("gender"));
        a.setEmail(rs.getString("email"));
        return a;
    }

    /** Returns the admin when e-mail + password are right. Old plain-text passwords are upgraded to a hash. */
    public Optional<Admin> authenticate(String email, String password) {
        String sql = "SELECT * FROM admin_login WHERE email = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String stored = rs.getString("password");
                    if (PasswordUtil.verify(password, stored)) {
                        Admin a = map(rs);
                        if (PasswordUtil.needsUpgrade(stored)) {
                            updatePassword(c, a.getId(), password);
                        }
                        return Optional.of(a);
                    }
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw wrap("Admin login", e);
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM admin_login WHERE email = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw wrap("Admin lookup", e);
        }
    }

    public int count() {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM admin_login");
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw wrap("Admin count", e);
        }
    }

    public boolean insert(Admin a, String plainPassword) {
        String sql = "INSERT INTO admin_login (name, address, contact, gender, email, password) VALUES (?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getName());
            ps.setString(2, a.getAddress());
            ps.setLong(3, a.getContact());
            ps.setString(4, a.getGender());
            ps.setString(5, a.getEmail());
            ps.setString(6, PasswordUtil.hash(plainPassword));
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw wrap("Admin insert", e);
        }
    }

    /** Checks the old password and stores the new one. Returns false when the old password is wrong. */
    public boolean changePassword(int adminId, String oldPassword, String newPassword) {
        String sql = "SELECT password FROM admin_login WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || !PasswordUtil.verify(oldPassword, rs.getString(1))) {
                    return false;
                }
            }
            updatePassword(c, adminId, newPassword);
            return true;
        } catch (SQLException e) {
            throw wrap("Admin change password", e);
        }
    }

    private void updatePassword(Connection c, int id, String plain) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("UPDATE admin_login SET password = ? WHERE id = ?")) {
            ps.setString(1, PasswordUtil.hash(plain));
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
}
