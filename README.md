# CyberSecurityBase-Project

### Introduction

This is my project for the Cybersecurity Base MOOC at the University of Helsinki. This web application is a deliberately insecure social networking platform, with functionality resembling that of Reddit or Hacker News.

To install this application, clone into the repo, and run the program on a local server with maven. There are two users created when the application initializes for testing purposes, so to do any testing, login as **Theo** with the password *saysHi*, or as **Bubba** with the password *GumpShrimp*.

### Description of Vulnerabilies

Since I built this application using Thymeleaf and Spring Security, creating security vulnerabilities in my application was sometimes more difficult than making a secure application. You might notice this while looking through the source code, as I give the exact location of where the vulnerability was introduced. For example, in the Security Configuration file, on line 24, I disabled CSRF protection, thus opening up the security vulnerability with one extra line of code. 

The first insecurity is a lack of [CSRF](https://www.owasp.org/index.php/Top_10_2013-A8-Cross-Site_Request_Forgery_(CSRF)) tokens on the application, leading to a problem in places such as the password changing page of the application. Since the application does not even ask you to enter your last password, this is an ideal attack vector for any malicious agents. 

The second insecurity is the usage of unsigned text rather than signed text for displaying user- entered information. This allows users to embed HTML into the page (try entering `<script> alert("Hello World"); </script>` into one of the new post fields), creating an [XSS](https://www.owasp.org/index.php/Top_10_2013-A3-Cross-Site_Scripting_(XSS)) vulnerability, again opening up an attack vector.

In later versions, I will be introducing [insecure direct object references](https://www.owasp.org/index.php/Top_10_2013-A4-Insecure_Direct_Object_References), [unvalidated redirects and forwards](https://www.owasp.org/index.php/Top_10_2013-A10-Unvalidated_Redirects_and_Forwards), and, perhaps, [injection](https://www.owasp.org/index.php/Top_10_2013-A1-Injection) attack vectors.

### Identification of Vulnerabilities


### Fixing these Vulnerabilities

Fixing most of these vulnerabilities is a cinch because of Spring Security. Most of these problems can be rectified with a few lines of code in the Security Configuration file.
* CSRF
   
   To fix the CSRF problem, enable CSRF tokens in the application. This can be done by commenting out this line: `http.csrf().disable();` from the SecurityConfiguration.java file, and by manually adding CSRF tokens, by adding this line of HTML: `<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>` to each of the POST forms in this application.
* XSS 

   To fix the XSS vulnerability, simply change the `th:utext` attributes in the main template to `th:text` attributes instead.   This treats the inputted text as text, instead of allowing scripting in the input fields.
