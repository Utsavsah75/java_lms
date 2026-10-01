package com.library.service;

import com.library.dao.AdminDAO;
import com.library.model.Admin;
import com.library.util.AppConfig;
import com.library.util.BusinessException;
import com.library.util.Validator;

/** Administrator accounts (PHP: admin_reg.php + Admin_loginserver.php, change_pwd.php). */
public class AdminService {

    private final AdminDAO adminDAO = new AdminDAO();

    /** Registration is open only when enabled in app.properties or when there is no administrator yet. */
    public boolean registrationOpen() {
        return AppConfig.getBoolean("admin.registration.enabled", false) || adminDAO.count() == 0;
    }

    public void register(String name, String email, String gender, String phone, String address, String password)
            throws BusinessException {
        if (!registrationOpen()) {
            throw new BusinessException("Administrator registration is disabled.");
        }
        name = Validator.trim(name);
        email = Validator.trim(email).toLowerCase();
        phone = Validator.trim(phone);
        address = Validator.trim(address);
        gender = Validator.trim(gender);
        if (name.length() < 2 || !Validator.maxLen(name, 50)) {
            throw new BusinessException("Enter the full name (2 to 50 characters).");
        }
        if (!Validator.isEmail(email)) {
            throw new BusinessException("Enter a valid e-mail address.");
        }
        if (!Validator.isPhone(phone) || phone.length() > 18) {
            throw new BusinessException("Enter a valid contact number (digits only).");
        }
        if (address.isEmpty() || !Validator.maxLen(address, 50)) {
            throw new BusinessException("Address is required (maximum 50 characters).");
        }
        if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female") && !gender.equalsIgnoreCase("other")) {
            throw new BusinessException("Please select the gender.");
        }
        if (!Validator.isStrongEnough(password)) {
            throw new BusinessException("Password must be at least 6 characters long.");
        }
        if (adminDAO.emailExists(email)) {
            throw new BusinessException("An administrator with this e-mail already exists.");
        }
        Admin a = new Admin();
        a.setName(name);
        a.setEmail(email);
        a.setGender(gender.toLowerCase());
        a.setContact(Long.parseLong(phone));
        a.setAddress(address);
        adminDAO.insert(a, password);
    }

    public void changePassword(int adminId, String oldPw, String newPw, String confirm) throws BusinessException {
        if (Validator.isBlank(oldPw)) {
            throw new BusinessException("Enter your current password.");
        }
        if (!Validator.isStrongEnough(newPw)) {
            throw new BusinessException("New password must be at least 6 characters long.");
        }
        if (!newPw.equals(confirm)) {
            throw new BusinessException("New password and confirm password do not match.");
        }
        if (!adminDAO.changePassword(adminId, oldPw, newPw)) {
            throw new BusinessException("Old password is incorrect.");
        }
    }
}
