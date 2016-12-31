# CyberSecurityBase-Project

### Introduction

This is my project for the Cybersecurity Base MOOC at the University of Helsinki. This web application is a deliberately insecure social networking platform, with functionality resembling that of Reddit or Hacker News. 

The logic of the site is fairly simple; users enter the site, and make posts. These posts can be edited at a later time by the user, or an admin, and can also be deleted by the admin. Admin privileges can be given by an admin to any user, but cannot be revoked. There are thus three tiers of users: anonymous browsers, who are limited to the main page, creating new accounts, and logging into the site, users, who are allowed to make posts, delete them, and edit them, and admins, who can do all of the above, and delete users and grant admin privileges. Of course, there are several problems that this application has with this logic, and this project will seek to point out and rectify these identified problems. 

To install this application, clone into the repo, and run the program on a local server with maven, by entering the directory through Command Prompt or Terminal, and running the command `mvn spring-boot:run`. There are two users created when the application initializes for testing purposes, so to do any testing, login as **Theo** with the password *saysHi*, or as **Bubba** with the password *GumpShrimp*. For admin access, login as **Admin** with the password *admin*. Or, if you would prefer, there is also functionality to create your own account.

### Notes about Website

It should be noted that this application was created primarily as a security testing apparatus, so there may be a few errors in the business logic here and there. Currently, the most glaring issue is that upon server reset, the application does not redirect from whatever page you were on to the login/ main page, often creating null pointer exceptions. Until I get around to fixing this, a good fix is to manually go back to the main page from whatever error page you are on.

### Identification and Description of Vulnerabilies

Since I built this application using Thymeleaf and Spring Security, both quite security minded frameworks, creating security vulnerabilities in my application was sometimes more difficult than making a secure application. You might notice this while looking through the source code, as I give the exact location of where the vulnerability was introduced. For example, in the Security Configuration file, on line 24, I disabled CSRF protection, thus opening up the security vulnerability with one extra line of code. 

The first insecurity is a lack of [CSRF](https://www.owasp.org/index.php/Top_10_2013-A8-Cross-Site_Request_Forgery_(CSRF)) tokens on the application, leading to a problem in places such as the password changing page of the application. This vulnerability can be noticed Since the application does not even ask you to enter your last password, this is an ideal attack vector for any malicious agents. 

The second insecurity is the usage of unsigned text rather than signed text for displaying user- entered information. This allows users to embed HTML into the page (try entering `<script> alert("Hello World"); </script>` into one of the new post fields), creating an [XSS](https://www.owasp.org/index.php/Top_10_2013-A3-Cross-Site_Scripting_(XSS)) vulnerability, again opening up an attack vector.

In later versions, I will be introducing [insecure direct object references](https://www.owasp.org/index.php/Top_10_2013-A4-Insecure_Direct_Object_References), [unvalidated redirects and forwards](https://www.owasp.org/index.php/Top_10_2013-A10-Unvalidated_Redirects_and_Forwards), and, perhaps, [injection](https://www.owasp.org/index.php/Top_10_2013-A1-Injection) attack vectors.

### Fixing these Vulnerabilities

Fixing most of these vulnerabilities is a cinch because of Spring Security. Most of these problems can be rectified with a few lines of code in the Security Configuration file.
* CSRF
   
   To fix the CSRF problem, enable CSRF tokens in the application. This can be done by commenting out this line: `http.csrf().disable();` from the SecurityConfiguration.java file, and by manually adding CSRF tokens, by adding this line of HTML: `<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>` to each of the POST forms in this application.
* XSS 

   To fix the XSS vulnerability, simply change the `th:utext` attributes in the main template to `th:text` attributes instead.   This treats the inputted text as text, instead of allowing scripting in the input fields.
   
