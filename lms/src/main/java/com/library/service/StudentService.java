package com.library.service;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.util.BusinessException;
import com.library.util.FileStorage;
import com.library.util.Validator;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Set;

/** Student (member) registration and admin maintenance (PHP: user_form.php, add_user_data.php, reg_students.php ...). */
public class StudentService {

    private static final Set<String> GENDERS = Set.of("male", "female", "other");
    private static final SecureRandom RANDOM = new SecureRandom();

    private final StudentDAO studentDAO = new StudentDAO();
    private final MailService mailService = new MailService();

    /** Outcome shown to the person who registered. */
    public enum RegistrationResult { MAIL_SENT, PENDING_ACTIVATION }

    /** Public self-registration: account starts un-verified and a verification mail is sent. */
    public RegistrationResult register(String name, String address, String phone, String gender, String email,
            String password, String confirm, Part photo) throws BusinessException, IOException {
        Student s = validate(name, address, phone, gender, email, 0);
        checkPassword(password, confirm);
        String file = FileStorage.saveImage(photo);
        String code = HexFormat.of().formatHex(randomBytes(4));
        s.setPhoto(file == null ? "" : file);
        s.setVerified(0);
        synchronized (StudentService.class) {
            s.setStudentCode(studentDAO.nextStudentCode());
            try {
                studentDAO.insert(s, password, code);
            } catch (RuntimeException e) {
                FileStorage.deleteQuietly(file);
                throw e;
            }
        }
        return mailService.sendVerification(s.getName(), s.getEmail(), code)
                ? RegistrationResult.MAIL_SENT : RegistrationResult.PENDING_ACTIVATION;
    }

    /** Admin adds a student directly: the account is active at once. */
    public String addByAdmin(String name, String address, String phone, String gender, String email, String password,
            Part photo) throws BusinessException, IOException {
        Student s = validate(name, address, phone, gender, email, 0);
        checkPassword(password, password);
        String file = FileStorage.saveImage(photo);
        s.setPhoto(file == null ? "" : file);
        s.setVerified(1);
        synchronized (StudentService.class) {
            s.setStudentCode(studentDAO.nextStudentCode());
            try {
                studentDAO.insert(s, password, HexFormat.of().formatHex(randomBytes(4)));
            } catch (RuntimeException e) {
                FileStorage.deleteQuietly(file);
                throw e;
            }
        }
        return s.getStudentCode();
    }

    public void update(int id, String name, String address, String phone, String gender, String email, String status)
            throws BusinessException {
        if (studentDAO.findById(id) == null) {
            throw new BusinessException("Student not found.");
        }
        Student s = validate(name, address, phone, gender, email, id);
        s.setId(id);
        s.setVerified("0".equals(status) ? 0 : 1);
        studentDAO.update(s);
    }

    public void setActive(int id, boolean active) throws BusinessException {
        if (studentDAO.findById(id) == null) {
            throw new BusinessException("Student not found.");
        }
        studentDAO.setVerified(id, active ? 1 : 0);
    }

    public void delete(int id) throws BusinessException {
        Student s = studentDAO.findById(id);
        if (s == null) {
            throw new BusinessException("Student not found.");
        }
        int open = studentDAO.countActiveIssues(s.getStudentCode());
        if (open > 0) {
            throw new BusinessException("Unable to delete the student: " + open + " book(s) have not been returned yet.");
        }
        studentDAO.delete(id);
        if (s.getPhoto() != null && s.getPhoto().matches("[0-9a-f]{32}\\.(jpg|jpeg|png|gif)")) {
            FileStorage.deleteQuietly(s.getPhoto());
        }
    }

    private Student validate(String name, String address, String phone, String gender, String email, int exceptId)
            throws BusinessException {
        name = Validator.trim(name).replaceAll("\\s+", " ");
        address = Validator.trim(address);
        phone = Validator.trim(phone);
        gender = Validator.trim(gender);
        email = Validator.trim(email).toLowerCase();
        if (name.length() < 2 || !Validator.maxLen(name, 255)) {
            throw new BusinessException("Please enter the full name (at least 2 characters).");
        }
        if (address.isEmpty() || !Validator.maxLen(address, 100)) {
            throw new BusinessException("Address is required (maximum 100 characters).");
        }
        if (!Validator.isPhone(phone)) {
            throw new BusinessException("Enter a valid mobile number (digits only, 7 to 15 digits).");
        }
        if (!GENDERS.contains(gender.toLowerCase())) {
            throw new BusinessException("Please select the gender.");
        }
        if (!Validator.isEmail(email)) {
            throw new BusinessException("Enter a valid e-mail address.");
        }
        if (studentDAO.emailExists(email, exceptId)) {
            throw new BusinessException("An account already exists for this e-mail address.");
        }
        if (studentDAO.phoneExists(phone, exceptId)) {
            throw new BusinessException("This mobile number is already registered.");
        }
        Student s = new Student();
        s.setName(name);
        s.setAddress(address);
        s.setPhone(phone);
        s.setGender(gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase());
        s.setEmail(email);
        return s;
    }

    private static void checkPassword(String password, String confirm) throws BusinessException {
        if (!Validator.isStrongEnough(password)) {
            throw new BusinessException("Password must be at least 6 characters long.");
        }
        if (!password.equals(confirm)) {
            throw new BusinessException("Password and Confirm Password do not match.");
        }
    }

    private static byte[] randomBytes(int n) {
        byte[] b = new byte[n];
        RANDOM.nextBytes(b);
        return b;
    }
}
