# PHP -> Java conversion map

Format for every converted page: **Original PHP functionality -> Servlet / class -> JSP -> SQL -> URL**.
All SQL runs through `PreparedStatement` inside a DAO. JSPs contain no Java code and no SQL.

## 1. Technology mapping

| PHP | Java |
|-----|------|
| `<?php ... ?>` in pages | JSP + JSTL/EL (`<c:forEach>`, `${book.name}`, `<c:out>` for escaping) |
| `conn.php` / `mysqli_connect` | `util/DBConnection` (settings in `db.properties`) |
| `mysqli_query` with string-built SQL | DAO classes with `PreparedStatement` |
| `$_SESSION['AdminLogin_successful']` | `HttpSession` attribute `adminId` |
| `$_SESSION['login_successful']` | `HttpSession` attributes `studentId`, `studentCode`, `studentName` |
| `check_admin.php` (include at top of page) | `filter/AuthFilter` on `/admin/*` and `/student/*` |
| `header('location: ...')` | `response.sendRedirect(...)` |
| `$_SESSION['msg']` + `echo` | flash messages (`BaseServlet.success()/error()`) shown as Bootstrap alerts |
| `include 'admin_header.php'` / `footer.html` | tag files `WEB-INF/tags/admin.tag`, `student.tag`, `public.tag`, `footer.tag` |
| `move_uploaded_file` | `util/FileStorage` (`@MultipartConfig`, image type check, random file name) |
| PHPMailer | `service/MailService` (Jakarta Mail) |
| `alert('...')` / SweetAlert redirects | Bootstrap alerts after a redirect |

## 2. Database

Tables are unchanged: `admin_login, students, books, bookcategory, bookauthor, issue_book` (and the unused `books_with_id`).
`issue_book.book_id` holds the book **ISBN** (it is a foreign key to `books.isbnno`), `issue_book.studentcode` holds the student code.

| Business rule | How it is calculated |
|---|---|
| Copies available | `books.quantity` minus rows in `issue_book` with `retrunstatus` NULL/0 |
| Returned? | `issue_book.retrunstatus = 1` (the column `returndate` has `ON UPDATE CURRENT_TIMESTAMP`, so it is not trusted alone) |
| Due date | issue date + `loan.days` (there is no due-date column in your schema) |
| Suggested fine | late days x `fine.per.day`; the admin can type any fine, as before |
| `books.issued` flag | kept in step (1 when no copy is left) |

No schema change is required. Passwords are re-hashed in place on first login (column is `varchar(255)`).

## 3. Converted pages

### Public pages
| PHP | Servlet (URL) | JSP | SQL |
|---|---|---|---|
| `index.php` (student login card) | `HomeServlet` `/home` (`index.jsp` forwards here) | `public/home.jsp` | - |
| `user_login.php` | `LoginServlet` POST `/login` | - | `StudentDAO.authenticate`: `SELECT * FROM students WHERE email=?` |
| `admin_login.php`, `admin_page.php` | `AdminLoginServlet` `/admin-login` | `public/admin-login.jsp` | `AdminDAO.authenticate`: `SELECT * FROM admin_login WHERE email=?` |
| `admin_reg.php`, `Admin_loginserver.php` | `AdminRegisterServlet` `/admin-register` | `public/admin-register.jsp` | `INSERT INTO admin_login ...` |
| `user_form.php`, `registation.php` | `RegisterServlet` `/register` | `public/register.jsp` | `INSERT INTO students ...` (verified = 0) |
| `verify_email.php` | `VerifyEmailServlet` `/verify-email?code=` | - | `UPDATE students SET verified=1 WHERE id=?` |
| `logout.php` | `LogoutServlet` `/logout` | - | - |
| `About.html` | `AboutServlet` `/about` | `public/about.jsp` | - |
| `check_avability.php`, `check_student.php`, `check_book.php` | `AjaxCheckServlet` `/ajax/check?type=&value=` | `js/app.js` | COUNT / SELECT by e-mail, phone, ISBN, student code |
| direct links to `uploads/x.jpg` | `UploadServlet` `/uploads/*` | - | - |

### Administrator pages (all need an admin session)
| PHP | Servlet (URL) | JSP | SQL |
|---|---|---|---|
| `dashboard.php` | `DashboardServlet` `/admin/dashboard` | `admin/dashboard.jsp` | `StatsDAO.adminStats`: COUNT(*) of books, students, issue_book, bookauthor, bookcategory; `SUM(fine)` |
| `add_category.php` | `CategoryServlet` POST `/admin/categories/add` | `admin/category-form.jsp` | `INSERT INTO bookcategory (CategoryName, Status)` |
| `manage_category.php` | `CategoryServlet` `/admin/categories`, POST `/delete`, POST `/status` | `admin/categories.jsp` | `SELECT ... FROM bookcategory`; `DELETE`; `UPDATE ... SET Status=?` |
| `edit_category.php` | `CategoryServlet` `/admin/categories/edit` | `admin/category-form.jsp` | `UPDATE bookcategory SET CategoryName=?, Status=? WHERE id=?` |
| `add_author.php` | `AuthorServlet` POST `/admin/authors/add` | `admin/author-form.jsp` | `INSERT INTO bookauthor (AuthorName)` |
| `manage_author.php` | `AuthorServlet` `/admin/authors`, POST `/delete` | `admin/authors.jsp` | `SELECT`, `DELETE FROM bookauthor` |
| `edit_author.php` | `AuthorServlet` `/admin/authors/edit` | `admin/author-form.jsp` | `UPDATE bookauthor SET AuthorName=?` |
| `add_book.php` | `BookServlet` `/admin/books/add` | `admin/book-add.jsp` | `INSERT INTO books (BookName, catid, author, isbnno, quantity, bookprice, bookimg)` |
| `manage_book.php`, `delete_book.php` | `BookServlet` `/admin/books`, POST `/delete` | `admin/books.jsp` | `books JOIN bookcategory JOIN bookauthor`; `DELETE FROM books` |
| `edit_book.php` | `BookServlet` `/admin/books/edit` | `admin/book-edit.jsp` | `UPDATE books SET BookName, catid, author, bookprice, quantity` |
| `change_image.php` | `BookServlet` `/admin/books/image` | `admin/book-image.jsp` | `UPDATE books SET bookimg=?` |
| `issue_book.php` | `IssueServlet` `/admin/issue` | `admin/issue-form.jsp` | transaction: lock book row, check stock + duplicate, `INSERT INTO issue_book (studentcode, book_id)` |
| `manage_issue_book.php` | `IssueServlet` `/admin/issues` (`?from=&to=`) | `admin/issues.jsp` | `issue_book JOIN students JOIN books` |
| `edit_issue_book.php` | `IssueServlet` `/admin/issues/return` | `admin/issue-return.jsp` | transaction: `UPDATE issue_book SET returndate=NOW(), fine=?, retrunstatus=1` |
| `reg_students.php` | `StudentAdminServlet` `/admin/students`, POST `/status` | `admin/students.jsp` | `SELECT * FROM students`; `UPDATE students SET verified=?` |
| `add.php`, `add_user_data.php` | `StudentAdminServlet` `/admin/students/add` | `admin/student-add.jsp` | `INSERT INTO students ...` (verified = 1) |
| `edit_reg_students.php`, `newuser_update.php`, `addnewuser_server.php` | `StudentAdminServlet` `/admin/students/edit` | `admin/student-edit.jsp` | `UPDATE students SET ...` |
| `all_member.php`, `confirm_member.php`, `confirm_student.php` | `StudentAdminServlet` POST `/admin/students/delete` | `admin/students.jsp` | `DELETE FROM students WHERE id=?` |
| `students_detail.php` | `StudentAdminServlet` `/admin/students/history?code=` | `admin/student-history.jsp` | issue_book of one student |
| `change_pwd.php` | `AdminPasswordServlet` `/admin/change-password` | `admin/change-password.jsp` | `UPDATE admin_login SET password=?` |
| `admin_header.php` | - | `WEB-INF/tags/admin.tag` | - |

### Student portal (needs a student session)
| PHP | Servlet (URL) | JSP | SQL |
|---|---|---|---|
| `user_dashboard.php` | `StudentPortalServlet` `/student/dashboard` | `student/dashboard.jsp` | COUNT of books and of this student's issues |
| `book_list.php` | `StudentPortalServlet` `/student/books` | `student/books.jsp` | `books JOIN bookcategory JOIN bookauthor` |
| `borrow_book.php` (and the "Issued Book" menu link) | `StudentPortalServlet` `/student/issued` | `student/issued.jsp` | this student's `issue_book` rows |
| `user_header.php` | - | `WEB-INF/tags/student.tag` | - |

## 4. PHP files that were NOT converted

None of these are reachable from a working page or they query tables that do not exist in `library_project.sql`.

| Files | Why |
|---|---|
| `update_book.php`, `update_member.php`, `book_page.php`, `add_book_data.php`, `add_book_record.php`, `admin_bookserver.php`, `all_books.php`, `display2.php`, `Untitled-1.php` | older version of the project: use table `add_book` and columns `Book Id`, `Quantity` that no longer exist |
| `display1.php` | uses table `register` (does not exist) |
| `issue_update.php`, `update_issue_book.php`, `issuebook_server.php`, `issuebook_list.php`, `borrowbook_server.php`, `returnbook_server.php`, `return.php`, `return_list.php`, `manageissueddublicate.php`, `pagination.php` | older issue/return flow built on `std_id`, `due_date`, `Books_with_id`, `Books.available` - none of these columns exist; replaced by the new issue/return screens |
| `conformuser_server.php`, `registation.php` (second copy), `select.php`, `getuser.php`, `students_detailTest.php`, `image.php`, `exit.php`, `menu_dashboard.php`, `email_index.php`, `sendmail.php`, `message.php`, `password_hash.php`, `teo.cpp`, `*.html` scratch pages | tests, empty files or duplicates |
| menu links `gall.php`, `About.php`, `My_Profile.php`, `change_password.php` (student), `issued_book.php` | the target files do not exist in the project, so no feature was invented for them (the student "Issued Book" link is served by `/student/issued`) |
| `PHPMailer/`, `composer.*` | replaced by Jakarta Mail |

## 5. Behaviour changes you should know about

* **Availability is enforced.** The PHP code never checked stock. Now a book cannot be issued when all copies are out ("Book is currently unavailable."), nor twice to the same student.
* **Safe deletes.** A category/author/book/student cannot be deleted while books or loans depend on it (the PHP deleted silently, and the foreign keys would cascade-delete borrowing records).
* **Edit Book** now has a Quantity field (the PHP form could not change it).
* **Passwords** are hashed (see README). **CSRF token** on every POST. Sessions end after 30 minutes.
* **Admin registration** is closed once an admin exists (`admin.registration.enabled`).
* **"Remember me"** stores only the e-mail address, never the password (the PHP prefilled the password).
* Student phone numbers and e-mails must be unique; the registration form checks them live.
* The e-mail account passwords that were written inside `user_form.php` and `sendmail.php` are **not** copied. Put your own in `app.properties` or environment variables, and revoke the old Gmail app passwords because they are in the zip you shared.
