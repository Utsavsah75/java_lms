# Setup guide: Eclipse / IntelliJ / VS Code + Apache Tomcat 10.1 + MySQL

## 0. Install once
* **JDK 17 or newer** (`java -version`)
* **Apache Tomcat 10.1.x** (must be 10 or newer: the code uses `jakarta.servlet`, not `javax.servlet`)
* **MySQL 8** (or MariaDB 10.4+; XAMPP's MySQL works)
* **Maven 3.8+** (Eclipse and IntelliJ include one)

## 1. Create the database
```
mysql -u root -p < database/library_project.sql
```
(or phpMyAdmin -> Import). It creates `library_project` with the same tables and sample data as before.

## 2. Configure
`src/main/resources/db.properties`
```
db.url=jdbc:mysql://localhost:3306/library_project?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL
db.username=root
db.password=
```
Optional: `app.properties` (upload folder, loan days, fine per day, e-mail).

## 3A. Command line (works everywhere)
```
mvn clean package
copy target\library.war  <TOMCAT>\webapps\        (Linux/macOS: cp target/library.war $CATALINA_HOME/webapps/)
<TOMCAT>\bin\startup.bat                            (Linux/macOS: bin/startup.sh)
```
Open http://localhost:8080/library/

## 3B. Eclipse (Eclipse IDE for Enterprise Java and Web Developers)
1. *File > Import > Maven > Existing Maven Projects* -> choose the project folder -> Finish.
2. *Window > Preferences > Server > Runtime Environments > Add > Apache Tomcat v10.1* -> point to your Tomcat folder.
3. Right click project > *Properties > Project Facets*: Dynamic Web Module **6.0**, Java 17, runtime = Tomcat 10.1.
4. Right click project > *Run As > Run on Server* > Tomcat v10.1 > Finish.
5. Open http://localhost:8080/library/ (the context path is `library`; change it in *Server > Modules* if you want).

## 3C. IntelliJ IDEA
Ultimate: *Open* the folder (pom.xml), *Run > Edit Configurations > + Tomcat Server > Local*, choose Tomcat 10.1, *Deployment > + Artifact > library:war exploded*, application context `/library`, Run.
Community: install the free **Smart Tomcat** plugin, *Run > Edit Configurations > + Smart Tomcat*, Tomcat Server = your Tomcat 10.1, Deployment Directory = `src/main/webapp`, Context Path = `/library`.

## 3D. VS Code
Install *Extension Pack for Java* and *Community Server Connectors* (or *Tomcat for Java*). Open the folder, run `mvn clean package` in the terminal, right click `target/library.war` > *Run on Server* (or add Tomcat 10.1 in the Servers view and drop the WAR on it).

## 4. First login
* Admin: http://localhost:8080/library/admin-login -> `utsavsah75@gmail.com` / `123` (change the password afterwards)
* Student: http://localhost:8080/library/ -> `utsavsah75@gmail.com` / `12345`

Walk-through to test everything: add category -> add author -> add book (with cover) -> Admin > Students > add student -> Issue Books -> Manage Issue Books > Return (enter fine) -> check Dashboard numbers.

## 5. Uploaded pictures
Covers and student photos are written to `upload.dir` (default `<your user folder>/library_uploads`) and served at `/library/uploads/<file>`. The first start copies the sample covers from your old `uploads/` folder there. Student photo names stored in the old database that do not exist in that folder simply show no picture.

## 6. Verification e-mail (optional)
In `app.properties` set `mail.enabled=true`, `mail.username`, `mail.password` (a Gmail *App password*), `mail.from`, and `app.base.url` to the public address of the site. Without it the link is logged and the admin can activate students manually.

## 7. Troubleshooting
| Symptom | Fix |
|---|---|
| `HTTP 404` for every page | Tomcat is 9 or older; use 10.1. Or the WAR name differs from `/library`. |
| `ClassNotFoundException: javax.servlet...` | A Tomcat 9 / javax library is on the classpath; use only the dependencies in `pom.xml`. |
| `DATABASE NOT REACHABLE` in `catalina.out` | MySQL is not running or `db.properties` is wrong. |
| `The absolute uri: jakarta.tags.core cannot be resolved` | The JSTL dependency did not download: run `mvn clean package` again with internet access. |
| "Invalid or missing security token" | The page was open longer than the session (30 min); reload it. |
| Pictures do not show | Check `upload.dir` exists and is writable by the Tomcat user. |
