package com.library.model;

/** Administrator row of table admin_login (password hash is never exposed). */
public class Admin implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String address;
    private long contact;
    private String gender;
    private String email;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public long getContact() { return contact; }
    public void setContact(long contact) { this.contact = contact; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

}
