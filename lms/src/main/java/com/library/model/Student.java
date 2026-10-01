package com.library.model;

/** Library member = row of table students. */
public class Student implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String studentCode;
    private String name;
    private String address;
    private String phone;
    private String gender;
    private String photo;
    private String email;
    private int verified;
    private java.util.Date regDate;
    private java.util.Date updationDate;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getVerified() { return verified; }
    public void setVerified(int verified) { this.verified = verified; }

    public java.util.Date getRegDate() { return regDate; }
    public void setRegDate(java.util.Date regDate) { this.regDate = regDate; }

    public java.util.Date getUpdationDate() { return updationDate; }
    public void setUpdationDate(java.util.Date updationDate) { this.updationDate = updationDate; }

    /** verified = 1 means the account is active (email verified or activated by an admin). */
    public boolean isActive() { return verified == 1; }

}
