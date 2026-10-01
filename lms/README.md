# Public Library - Library Management System (Java / JSP / Servlet / JDBC)

Conversion of the PHP project `library_project` to a Java web application.

| Layer      | Technology                                                        |
|------------|-------------------------------------------------------------------|
| View       | JSP + JSTL/EL + tag files, Bootstrap 5, Font Awesome 4, DataTables |
| Controller | Jakarta Servlets (Servlet 6 / Tomcat 10.1+), 2 filters, 1 listener |
| Service    | `com.library.service` (validation + business rules + transactions) |
| DAO        | `com.library.dao` (JDBC, **PreparedStatement only**)                |
| Model      | `com.library.model` beans                                          |
| Database   | MySQL / MariaDB, **same schema and data** as `library_project.sql`  |

## Quick start

1. Import `database/library_project.sql` into MySQL (it creates the `library_project` database).
2. Edit `src/main/resources/db.properties` if your MySQL user/password is not `root` / empty.
3. `mvn clean package` -> `target/library.war` -> copy to Tomcat 10.1 `webapps/`.
4. Open <http://localhost:8080/library/>

Full IDE instructions (Eclipse, IntelliJ, VS Code): `docs/SETUP_GUIDE.md`
File-by-file PHP -> Java mapping: `docs/CONVERSION_MAP.md`

## Logins from the original database

| Role    | E-mail                    | Password | Notes                                       |
|---------|---------------------------|----------|---------------------------------------------|
| Admin   | utsavsah75@gmail.com      | 123      | change it at *Change Password* after login  |
| Student | utsavsah75@gmail.com      | 12345    | student SID001 (other students: see table `students`) |

Passwords in the old database are plain text. They still work, and each one is replaced by a
PBKDF2 hash the first time that person logs in. New passwords are always stored hashed.

## Project layout

```
pom.xml
database/library_project.sql
docs/
src/main/java/com/library/
    controller/   one servlet per screen group (see docs/CONVERSION_MAP.md)
    filter/       SecurityFilter (UTF-8, headers, CSRF)  AuthFilter (login required for /admin/* and /student/*)
    listener/     AppInitListener (upload folder + DB check at start-up)
    service/      BookService IssueService StudentService CatalogService AdminService MailService
    dao/          AdminDAO StudentDAO BookDAO CategoryDAO AuthorDAO IssueDAO StatsDAO
    model/        Admin Student Book Category Author IssueRecord DashboardStats
    util/         DBConnection AppConfig PasswordUtil Validator FileStorage JsonUtil exceptions
src/main/resources/   db.properties  app.properties
src/main/webapp/
    index.jsp  css/  js/  images/  vendor/ (Bootstrap, jQuery, Font Awesome, DataTables - no CDN needed)
    WEB-INF/web.xml  WEB-INF/tags/ (layouts)  WEB-INF/views/{public,admin,student,common}/*.jsp
    WEB-INF/sample-uploads/   the book covers from your uploads/ folder
```
JSPs live under `WEB-INF/views`, so they can only be reached through a servlet.

## Settings (`src/main/resources/app.properties`)

| Key | Default | Meaning |
|-----|---------|---------|
| `upload.dir` | `<user home>/library_uploads` | where covers / photos are stored (outside the WAR, survives redeploy) |
| `loan.days` | 14 | days until a book is due |
| `fine.per.day` | 5 | used only to *suggest* the fine on the return page |
| `admin.registration.enabled` | false | `admin_reg.php` was open to everyone; now closed once an admin exists |
| `mail.enabled` + `mail.*` | false | SMTP for the verification e-mail |
| `app.base.url` | `http://localhost:8080/library` | link inside the verification e-mail |

Every key can also come from an environment variable: `LIB_` + key in capitals, dots become `_`
(`LIB_MAIL_PASSWORD`), and `LIB_DB_URL`, `LIB_DB_USER`, `LIB_DB_PASSWORD` for the database.
With `mail.enabled=false` the verification link is written to the Tomcat log and an admin can
activate the student from *Admin > Students*.
