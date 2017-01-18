# CyberSecurityBase-Project

### Introduction

This is my project for the Cybersecurity Base MOOC at the University of Helsinki. This web application is a deliberately insecure social networking platform, with functionality resembling that of Reddit or Hacker News. 

The logic of the site is fairly simple; users enter the site, and make posts. These posts can be edited at a later time by the user, or an admin, and can also be deleted by the admin. Admin privileges can be given by an admin to any user, but cannot be revoked. There are thus three tiers of users: anonymous browsers, who are limited to browsing the main page, creating new accounts, and logging into the site; users, who are allowed to make posts, delete them, and edit them, and admins, who can do all of the above, as well as delete users and grant admin privileges to other users. Of course, there are several problems that this application has with this logic, and this project will seek to point out and rectify these identified problems. 

To install this application, clone into the repo, and run the program on a local server with maven, by entering the directory through Command Prompt or Terminal, and running the command `mvn spring-boot:run`. There are two users created when the application initializes for testing purposes, so to do any testing, login as **Theo** with the password *saysHi*, or as **Bubba** with the password *GumpShrimp*. For admin access, login as **Admin** with the password *admin*. Or, if you would prefer, there is also functionality to create your own account.

### Notes about Website

It should be noted that this application was created primarily as a security testing apparatus, so there may be a few errors in the business logic here and there. I have not counted errors in business logic as errors in the security configuration, but still keep in mind that there are several errors that I have not yet bothered to discuss. For starters, admins are allowed to delete any user from the web app, including themselves. This lends itseld to several possible erros. If I do not get around to fixing this error, please keep in mind that the business logic of the application is not as robust as it could possible be, and use caution and judgement while testing the application.

### Identification and Description of Vulnerabilies

Since I built this application using Thymeleaf and Spring Security, both quite security minded frameworks, creating security vulnerabilities in my application was sometimes more difficult than making a secure application. You might notice this while looking through the source code, as I give the exact location of where the vulnerability was introduced. For example, in the Security Configuration file, on line 24, I disabled CSRF protection, thus opening up the security vulnerability with one extra line of code. 

* A8 - Cross Site Request Forgery
The first insecurity is a lack of [CSRF](https://www.owasp.org/index.php/Top_10_2013-A8-Cross-Site_Request_Forgery_(CSRF)) tokens on the application, leading to a problem in places such as the password changing page of the application. This vulnerability can be noticed Since the application does not even ask you to enter your last password, this is an ideal attack vector for any malicious agents. To test out this vulnerability, I have enclosed an HTML file, which contains a form that acts on the web application, thus showing the vulnerability in action. If you have Maven configured differently, simply change the URL on which the form acts. 

* A3 - Cross Site Scripting
The second insecurity is the usage of unsigned text rather than signed text for displaying user- entered information. This allows users to embed HTML into the page (try entering `<script> alert("Hello World"); </script>` into one of the new post fields), creating an [XSS](https://www.owasp.org/index.php/Top_10_2013-A3-Cross-Site_Scripting_(XSS)) vulnerability, again opening up an attack vector.

* A4 - Insecure Direct Object References
This insecurity lies in the fact that the post editing pages are referred to directly by URL, with the url being http://localhost:8080/edit/{the number of the post}. This means that, if a user correctly guesses the number of a post, he or she can edit any post that they would like, not just the ones that they are allowed to. Also, because of the way that the site is set up, it is impossible to determine who edited the post, or even whether the post was edited at all. To test out this vulnerability, try changing the number that comes after the URL extension `/change`, such as `/change/0`. It should be noted, however, that only authenticated users are allowed to access the editing page, so you must log in before you try to test this vulnerability.

* A7 - Missing Function Level Access Control
This insecurity exists because any authenticated user is allowed to access the admin page. While only admin level users have a hyperlink to the admin page, any user can access the admin page by simply changing the URL extension to `\admin`. 

In later versions, I will be introducing [insecure direct object references](https://www.owasp.org/index.php/Top_10_2013-A4-Insecure_Direct_Object_References), [unvalidated redirects and forwards](https://www.owasp.org/index.php/Top_10_2013-A10-Unvalidated_Redirects_and_Forwards), and, perhaps, [injection](https://www.owasp.org/index.php/Top_10_2013-A1-Injection) attack vectors.

### Fixing these Vulnerabilities

Fixing most of these vulnerabilities is a cinch because of Spring Security. Most of these problems can be rectified with a few lines of code in the Security Configuration file.
* CSRF
   
   To fix the CSRF problem, enable CSRF tokens in the application. This can be done by commenting out this line: `http.csrf().disable();` from the SecurityConfiguration.java file, and by manually adding CSRF tokens, by adding this line of HTML: `<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>` to each of the POST forms in this application.

* XSS 

   To fix the XSS vulnerability, simply change the `th:utext` attributes in the main template to `th:text` attributes instead.   This treats the inputted text as text, instead of allowing scripting in the input fields.
   
* Insecure Direct Object References

	There are a few ways in which this vulnerability can be solved. The first solution is to check the authentication of the user every time they try to enter the edit post page, so that only the user that created the post, or admins, are allowed to edit a post. The second way to solve this problem is to use a hash when referring to the post, so that it is difficult for an unauthenticated user to guess the URL for a post that they are not authorized to view. However, this solution is difficult, and is not as secure as the previous one, as anyone with access to this link can access this web page, so this method is not recommended. 

* Missing Function Level Access Control

	To rectify this vulnerability, one simply needs to change the security configuration in the SecurityConfiguration.java file. Remove `"/admin", "/admin/**"` from the antMatchers method, and then create a new method `"antMatchers("/admin", "\admin/**").hasRole("ADMIN")` below. This will check to see if the authenticated user is an admin level user, and only allow them to access the admin page. 