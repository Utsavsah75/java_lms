package com.library.service;

import com.library.util.AppConfig;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Sends the verification e-mail (PHP: PHPMailer in user_form.php). Credentials come from app.properties / env only. */
public class MailService {

    private static final Logger LOG = Logger.getLogger(MailService.class.getName());

    public boolean isEnabled() {
        return AppConfig.getBoolean("mail.enabled", false);
    }

    /** Returns true when the mail was handed to the SMTP server. */
    public boolean sendVerification(String name, String toEmail, String code) {
        String link = AppConfig.get("app.base.url", "http://localhost:8080/library") + "/verify-email?code=" + code;
        if (!isEnabled()) {
            LOG.info("Mail disabled. Verification link for " + toEmail + " : " + link);
            return false;
        }
        String user = AppConfig.get("mail.username", "");
        String pass = AppConfig.get("mail.password", "");
        Properties p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", AppConfig.get("mail.smtp.host", "smtp.gmail.com"));
        p.put("mail.smtp.port", AppConfig.get("mail.smtp.port", "587"));
        p.put("mail.smtp.connectiontimeout", "10000");
        p.put("mail.smtp.timeout", "10000");
        Session session = Session.getInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });
        try {
            MimeMessage m = new MimeMessage(session);
            m.setFrom(new InternetAddress(AppConfig.get("mail.from", user)));
            m.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            m.setSubject("Email Verification from Public Library Management");
            String safeName = name.replace("<", "&lt;").replace(">", "&gt;");
            m.setContent("<h2>You have registered with Public Library Management</h2>"
                    + "<p>Dear " + safeName + ",</p>"
                    + "<p>Thanks for registration. Your verification code is <b>" + code + "</b>.</p>"
                    + "<p><a href='" + link + "' style='background:#f8f9fa;border:1px solid #007bff;padding:8px 12px;"
                    + "border-radius:4px;color:#007bff;font-weight:bold;text-decoration:none'>Click here to verify your email</a></p>"
                    + "<p>Regards,<br>Public Library Management</p>", "text/html; charset=UTF-8");
            Transport.send(m);
            return true;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Could not send verification mail to " + toEmail, e);
            return false;
        }
    }
}
