# simple-authentication-methods

[![Build Status](https://travis-ci.org/neat-solutions-lab/simple-authentication-methods.svg?branch=master)](https://travis-ci.org/neat-solutions-lab/simple-authentication-methods)

## Introduction
Easy and quick to use, authentication library for simple, Spring MVC based back-end services leveraging Spring Security framework and 
Spring Boot platform.

It equips the developers with few annotations which, if applied on top of the Spring 
[@Configuration](https://docs.spring.io/spring/docs/5.1.3.RELEASE/javadoc-api/org/springframework/context/annotation/Configuration.html) 
bean, provide simple though complete authentication layer.

The library is written in Kotlin language, however examples are provided how to use it either from Kotlin or Java language.

The small portion of the codebase is in the Java language, but in fact it is not genuine part of the library itself
but rather repackaged pieces of the [ASM](https://asm.ow2.io/) project. This repackaging have been done to prevent 
explicit dependency on the [ASM](https://asm.ow2.io/) library. The only rationale behind such approach was to stay 
coherent with Spring Framework which also repackages some pieces of the [ASM](https://asm.ow2.io/) library 
remaining independent from the actual releases of it. Then, the simple-authentication-methods library repackages 
a few additional ASM classes which where omitted by Spring Framework but turned out to be required by the library. 

## Provided authentication methods
The authentication methods provided by the simple-authentication-methods library are build on the top of 
Spring Security framework.

The library is designed to work in simple scenarios where the list of all authenticated users (or tokens) 
can be harmlessly hold in the program memory.

In general, the library provides one annotation meant to enable authentication methods in a selective way and
then there are two dedicated annotations to control the attributes of two specific authentication methods.
 
The authentication methods handled by the library are described below.

### Basic HTTP Authentication method
The Spring Security framework provides implementation of Basic HTTP Authentication method on its own, so that
the simple-authentication-methods library does not reinvent the wheel and strongly depends on this 
implementation.

The library focuses on reducing the amount of boilerplate required to employ this method in simple
usage scenarios. To take the advantage of this authentication method, the library provides annotations.
One annotation is used to enable the internal mechanism which integrates the library with the
underlying Spring Security implementation of the Basic HTTP Authentication method. Second annotation is
used mainly to point out the source of the authenticated users list. In particular, the list of authenticated users can
be acquired form a file or environment variables or can be hardcoded in the source code in the of the attributes passed 
to the annotation. 

### Bear type token
This authentication method is entirely implemented by the library. It takes the form of a custom servlet filter which (if
enabled) is injected into Spring Security managed Filters Chain. The filter comes into play when it recognizes that 
the request carries Bear type token, which means it contains the `Authorization` header with the `Bear <actual-token-value>` 
value.

The value hold in `<actual-token-value>` is literally compared with the list of authenticated tokens hold by the 
library in the program memory (actually these tokens are hold in the Map data structure, so that the search time is not 
significantly affected by the number of available tokens). If the matching token is found then the request is 
considered to be authenticated.

The source of the list of authenticated tokens is configured with the annotation attributes. Essentially the list of
the authenticated tokens can be obtained from a file or from environment variables or from a hardcoded array of tokens
 which is passed to the related annotation through one of the attributes.

To stick with the concept of the Spring Security imposed principals and authorities, 
each token is mapped to the user name and the list of granted roles 
(for quick reference check out usage examples from _Usage examples_ section, 
for more detailed information head over to the _Usage documentation_ section).

## Authenticated user information
The information about an authenticated user is provided to the developer in an usual, Spring Security defined way.
No matter what authentication method was used, the user name and the roles of the user are finally available in
[SecurityContextHolder](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/core/context/SecurityContextHolder.html) via 
[User](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/core/userdetails/User.html) object which implements 
[UserDetails](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/core/userdetails/UserDetails.html) interface:

```java
org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) 
    org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
String username = user.getUsername();
java.util.Collection<? extends GrantedAuthority> = user.getAuthorities();
```

Note that the library controls only values returned by the [User.getUsername()](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/core/userdetails/User.html#getUsername--)
and [User.getAuthorities()](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/core/userdetails/User.html#getAuthorities--)
methods.


Values being returned by the remaining methods of the 
[User](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/core/userdetails/User.html)
object are not determined by the simple-authentication-methods library. 

For details, how tokens and Basic HTTP Authentication method based requests are mapped to the user names and roles see the
_Usage documentation_ section.

## Authorization

First of all, the simple-authentication-methods library aims at simple scenarios, where highly granular 
authorization is not a concern. It rather addresses scenarios where all authenticated users are automatically 
considered as authorized as well.

Having said that, it is still possible to leverage some authorization instruments available in Spring 
Security framework.  

### Integration with request level authorization

Spring Security allows the developer to use ant patterns to define mappings between selected application paths and
the user's authorities (or simply roles) which are necessary to gain access to these paths.
Here is the link to this concept on Spring Security documentation page:
[request level authorization in Spring Security](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/reference/htmlsingle/#jc-authorize-requests).

Due to the main functional assumptions and because the simple-authentication-methods library sits on higher
abstraction level, the above mechanism is partially hidden from the library users.

Anyway, the library provides a way to define and delegate these access rules to the underlying authorization mechanisms.
For details, please refer to the description of 
[@EnableSimpleAuthenticationMethods](src/main/kotlin/nsl/sam/core/annotation/EnableSimpleAuthenticationMethods.kt) annotation and its 
`authorizations` attribute in the _Usage documentation_ section. 
 
### Other authorization methods

Another authentication methods provided by the Spring Security, should stay transparent to the 
simple-authentication-methods library and can be used independently. 

You only have to remember that whenever these methods refer to the roles (or in other words user's authorities), 
the names of the users and the roles come from the sources controlled by the simple-authentication-methods library. For details,
how to provide the list of users and their roles, refer 
to [SimpleBasicAuthentication](src\main\kotlin\nsl\sam\method\basicauth\annotation\SimpleBasicAuthentication.kt) and 
[SimpleTokenAuthentication](src\main\kotlin\nsl\sam\method\token\annotation\SimpleTokenAuthentication.kt) 
annotations descriptions in the _Usage documentation_ section.

### Session management

The library doesn't use a session to store any authentication related data. 
Each request is separately fully authenticated.

## Requirements

### Compilation requirements

NOTE: JDK 11+ is required only to compile but not to use the library.
It is because some integration tests utilize JDK 11 specific HTTP client. 
Then, the requirements regarding the library compilation environment are as follows:

- JDK 11 + 
- Maven 3.5 +
- git
- Kotlin 1.3 (Maven will automatically find and download the plugin with embedded Kotlin compiler)

### Usage requirements

The requirements imposed on the application which can use the library:

- JDK 1.8+
- Spring Boot 2.1+

## Compilation and installation procedure

Assuming that your development environment adheres to the above compilation requirements, to compile and install 
the library locally follow these steps:

```bash
git clone https://github.com/neat-solutions-lab/simple-authentication-methods.git
cd simple-authentication-methods
mvn install
```

If, for some reason, you want to skip the test phase, replace the last command with this one:
```bash
mvn -DskipTests install
``` 

## Usage examples

For the quick usage reference please head over to the prepared examples. They are really short, self explaining 
pieces of a code, much less convoluted than a bit long, rather not engaging documentation pages.

The simple examples of the usage of the simple-authentication-methods library with Spring Boot based HTTP services can be 
found through this two links:
- [kotlin-sam-example](https://github.com/neat-solutions-lab/kotlin-sam-example)
- [java-sam-example](https://github.com/neat-solutions-lab/java-sam-example)

These examples show how to associate different authentication polices with different HTTP end-points and how
to obtain the list of credentials (users, tokens, roles) from different source types.

## Adding library to your project

This section depicts how to incorporate the simple-authentication-methods library into development projects 
managed by either Maven or Gradle, which probably are the two most popular project management tools. 

### Maven managed project

Assuming you have compiled and installed the library according to the procedure presented in 
the _Compilation and installation procedure_ section, to include the simple-authentication-methods library into 
your application add this dependencies definition to your `pom.xml` file:

```xml
<project ...>
    ...
	<dependencies>
        ...
		<dependency>
			<groupId>com.github.neat-solutions-lab</groupId>
			<artifactId>simple-authentication-methods</artifactId>
			<version>1.0.0</version>
		</dependency>
        ...
	</dependencies>
	...
</project>
```

### Gradle managed project

Assuming you have compiled and installed the library according to the procedure presented in 
the _Compilation and installation procedure_ section, to include the simple-authentication-methods library into 
your application apply these changes to your `gradle.build` file:

```groovy
...
repositories {
    // by default Gradle dosn't include artifacts form local Maven repository, so we have to add it
    mavenLocal()
    ...
}

dependencies {
    ...
    implementation('com.github.neat-solutions-lab:simple-authentication-methods:1.0.0')
    ...
}
...
```


## API documentation

(For simple usage examples please check out two example projects referred to in the _Usage examples_ section.)

### Main assumptions

The goal of the simple-authentication-methods library is to simplify and reduce the amount of the code which is necessary
to add an authentication layer on the top of simple, not demanding in that area, Spring MVC based services developed with 
Spring Boot platform. The library itself leverages the Spring Security framework but picks from it only the parts required
for simple scenarios and tries to hide the implementation details behind few simple annotations.

### Available annotations description

As a result of the above assumption the library provides three annotations and associated attributes.
There is one annotation to control which authentication methods are to be enabled and there is one 
annotation related to each authentication method so that there is a way to configure details about these specific methods. 

The available annotations and attributes are described in the following subsections.

**NOTE:** Most presented annotations attributes are meant for slightly more advanced scenarios, and it is not important
to know them all. For simple usage examples please check out two example projects referred to in the _Usage examples_ section. 

#### EnableSimpleAuthenticationMethods annotation

Link to the source code: [@EnableSimpleAuthenticationMethods](src/main/kotlin/nsl/sam/core/annotation/EnableSimpleAuthenticationMethods.kt)

This annotation can be applied only on the top of any 
[@Configuration](https://docs.spring.io/spring/docs/5.1.3.RELEASE/javadoc-api/org/springframework/context/annotation/Configuration.html) bean 
(meta annotations which itself are annotated with [@Configuration](https://docs.spring.io/spring/docs/5.1.3.RELEASE/javadoc-api/org/springframework/context/annotation/Configuration.html) 
also do count, so for example a class annotated with the 
[@SpringBootApplication](https://docs.spring.io/spring-boot/docs/2.1.1.BUILD-SNAPSHOT/api/org/springframework/boot/autoconfigure/SpringBootApplication.html) 
can be further annotated with the [@EnableSimpleAuthenticationMethods](nsl/sam/core/annotation/EnableSimpleAuthenticationMethods.kt) annotation).

This annotation is the main "entry point" to the simple-authorization-methods library. 
It enables and configures the shared properties of the authentication methods.
Most notably, with the help of this annotation, one can associate selected authentication methods with the 
application paths matching the declared ant pattern. If the matching ant pattern is not explicitly declared then
each request will have to be authenticated.

To establish different authentication polices for different application paths, the annotation can be applied multiple 
times. The only requirement is that one [@Configuraion](https://docs.spring.io/spring/docs/5.1.3.RELEASE/javadoc-api/org/springframework/context/annotation/Configuration.html)
bean can be associated with only one 
[@EnableSimpleAuthenticationMethods](src/main/kotlin/nsl/sam/core/annotation/EnableSimpleAuthenticationMethods.kt) 
annotation. Therefore, sometimes it can be required to introduce the dedicated 
[@Configuration](https://docs.spring.io/spring/docs/5.1.3.RELEASE/javadoc-api/org/springframework/context/annotation/Configuration.html) 
beans.

If the annotation is used multiple times then the order in which the authentication rules are processed can matter. 
Therefore, to impose explicit order on the rules defined by different annotations, the `order` attribute can be used 
(see the descriptions below). 

The attributes of this annotation are enlisted below:

**Attribute name:** `methods` <br/>
**Attribute type:** `Array<AuthenticationMethod>` <br/>
**Default value:** `[AuthenticationMethod.SIMPLE_BASIC_AUTH, AuthenticationMethod.SIMPLE_TOKEN]`<br/>
**Prevalence level:** Basic attribute, meant for common scenarios.<br/>
**Description:** Array of [AuthenticationMethod](src/main/kotlin/nsl/sam/core/annotation/AuthenticationMethod.kt) 
enum type values which indicate what authentication methods are to be enabled.

**Attribute name:** `match` <br/>
**Attribute type:** `String` <br/>
**Default value:** (empty string) <br/>
**Prevalence level:** Basic attribute, meant for common scenarios. <br/>
**Description:** Defines the ant pattern that the path part of the request has to match in order to be processed by the 
authentication methods enabled by this annotation. The format of the match pattern has to adhere to so called ant 
matcher used in the Spring Security 
[antMatcher](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#antMatcher-java.lang.String-).

**Attribute name:** `order` <br/>
**Attribute type:**  `Int` <br/>
**Default value:** `-1` <br/>
**Prevalence level:** Can be useful in the case of multiple usage of 
[@EnableSimpleAuthenticationMethods](src/main/kotlin/nsl/sam/core/annotation/EnableSimpleAuthenticationMethods.kt) 
annotation.<br/>
**Description:** Defines the order in which the authentication rules imposed by this annotation will be processed by 
the internal mechanisms. It can be useful if the annotation is applied multiple times (multiple usage of this 
annotation is possible thanks to the `match` attribute). 

**Attribute name:** `forceHttps` <br/>
**Attribute type:** `Boolean` <br/>
**Default value:** `false` <br/>
**Prevalence level:** For specific scenarios.<br/>
**Description:** If set to the true value, only requests received via HTTPS channel are accepted. Otherwise, the client is 
redirected to the HTTPS channel. In the case of redirection, the port number of the secure channel has to be determined. 
If the port number is different from the default one, the `portMapping` attribute can be used to provide 
information about the secure channel's port number.

**Attribute name:** `portMapping` <br/>
**Attribute type:** `Array<KClass<out PortsMapping>>` <br/>
**Default value:** `[]` (empty array) <br/>
**Prevalence level:** For specific scenarios.<br/>
**Description:** Helps to determine the port number to which the client is redirected if the `foreceHttps` attribute is set 
to true and the request comes in through non secure HTTP protocol.
It is an array of KClass(es) of the [PortsMapping](src/main/kotlin/nsl/sam/core/annotation/attrtypes/PortsMapping.kt) 
interface instances. The interface defines only one function which should return an instance of `Pair<Int, Int>`. 
The first `Int` in this Pair indicates the port number of
the insecure channel and the second `Int` determines the port number of the secure channel to which the
client will be redirected.
                         
**Attribute name:** `debug` <br/>
**Attribute type:** `Boolean` <br/>
**Default value:** `false`<br/>
**Prevalence level:** For development time debugging actions.<br/>
**Description:** This attribute is directly passed to the underlying 
[@EnableWebSecurity](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/config/annotation/web/configuration/EnableWebSecurity.html)
Spring Security annotation. If set to true, Spring Security prints out some additional debugging messages.

**Attribute name:** `localAnonymousFallback` <br/>
**Attribute type:** `Boolean` <br/>
**Default value:** `false`<br/>
**Prevalence level:** For specific scenarios.<br/>
**Description:** If this attribute is set to true then all requests are authenticated as aN anonymous user if the following
conditions are met:
- There is no even one user available in THE sources configured by either
[@SimpleBasicAuthentication](src\main\kotlin\nsl\sam\method\basicauth\annotation\SimpleBasicAuthentication.kt) or
[@SimpleTokenAuthentication](src\main\kotlin\nsl\sam\method\token\annotation\SimpleTokenAuthentication.kt) 
annotations (depending on which authentication method or methods are enabled).
- The Spring Boot backed application is explicitly configured to listen on the local loopback interface
(the value of the _server.address_ application property is set to _127.0.0.1_ or to _localhost_).

**Attribute name:** `authorizations` <br/>
**Attribute type:** `String` <br/>
**Default value:** (empty string) <br/>
**Prevalence level:** Beyond the basic scenarios.<br/>
**Description:** This attribute can be used to optionally define authorization rules, that is to specify the roles the user has to have in order to be authorized. 
The value of this attribute takes the form of the chain of the methods being added to the Spring Security
[org.springframework.security.config.annotation.web.builders.HttpSecurity#authorizeRequests](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#authorizeRequests--)
method as it is described here:
[https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/reference/htmlsingle/#jc-authorize-requests](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/reference/htmlsingle/#jc-authorize-requests)

Example usage of authorizations attribute:

```
authorizations="antMatchers('/user-area/**').hasRole('USER').antMatchers('/admin-area/**').hasRole('ADMIN')"
```

**Attribute name:** `authenticationEntryPointFactory` <br/>
**Attribute type:** `Array<KClass<out AuthenticationEntryPointFactory>>` <br/>
**Default value:** `[]` (empty array) <br/>
**Prevalence level:** For specific scenarios.<br/>
**Description:** Allows the developer to customize the response sent in the case of the failed authentication.
         
Even though the attribute is declared as an array, only the first element in the array
is taken into account.
         
The type of the element in the array is KClass of [AuthenticationEntryPointFactory](src/main/kotlin/nsl/sam/core/entrypoint/factory/AuthenticationEntryPointFactory.kt). 
This factory is responsible for creating regular Spring Security [org.springframework.security.web.AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
instances.
         
The [org.springframework.security.web.AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
created by the provided factory is used to send a response to the client in the following cases:
- Neither of the enabled authentication methods is able to decide if the request is authenticated or not and
the anonymous authentication is not set up.
- The HTTP Basic Auth based configuration method recognizes invalid credentials and there is no
custom [org.springframework.security.web.AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html) associated with the HTTP
Basic Auth method (such customization can be done with the
[@SimpleBasicAuthentication](src/main/kotlin/nsl/sam/method/basicauth/annotation/SimpleBasicAuthentication.kt) annotation).         
- The token based authentication method recognizes the invalid token and there is no
custom [org.springframework.security.web.AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html) 
associated with the token based authentication method (such customization can be done with the
[@SimpleTokenAuthentication](src/main/kotlin/nsl/sam/method/token/annotation/SimpleTokenAuthentication.kt) annotation).
         

Example usage of the [@EnableSimpleAuthenticationMethods](src/main/kotlin/nsl/sam/core/annotation/EnableSimpleAuthenticationMethods.kt) 
annotation in Kotlin language:
```kotlin
import org.springframework.context.annotation.Configuration
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.annotation.AuthenticationMethod

@Configuration
@EnableSimpleAuthenticationMethods(methods=[AuthenticationMethod.SIMPLE_BASIC_AUTH], match="/protected-area/**")
class AuthenticationConfiguration
```

Example usage of the [@EnableSimpleAuthenticationMethods](src/main/kotlin/nsl/sam/core/annotation/EnableSimpleAuthenticationMethods.kt) 
in Java language:
```java
import org.springframework.context.annotation.Configuration;
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods;
import nsl.sam.core.annotation.AuthenticationMethod;

@Configuration
@EnableSimpleAuthenticationMethods(methods = {AuthenticationMethod.SIMPLE_BASIC_AUTH}, match="/protected-area/**")
class AuthenticationConfiguration{}
```

#### SimpleBasicAuthentication annotation

Link to the source code: [@SimpleBasicAuthentication](src/main/kotlin/nsl/sam/method/basicauth/annotation/SimpleBasicAuthentication.kt)

The [@SimpleBasicAuthentication](src/main/kotlin/nsl/sam/method/basicauth/annotation/SimpleBasicAuthentication.kt)
annotation, if used, always accompanies the 
[@EnableSimpleAuthenticaitonMethods](src/main/kotlin/nsl/sam/core/annotation/EnableSimpleAuthenticationMethods.kt) 
annotation. It is used to point out the source of the authenticated users' list and optionally to customize the 
[AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
used in the case when the received HTTP Basic Auth credentials cannot be authenticated.

There are three different types of the source of the users list but in the end there is always only one source which is selected and used. The rule is that the first found source wins.
 
All types of the users sources provide the list of the users in the same format. Each user
and the associated attributes are encoded in one line of the text. Individual text line takes the following format:
 
```
<user-name>:{<pass-enc-alg>}<password> [<role1> <role2> .... <roleN>]
```
Where the meaning of the placeholders is as follows.:

`<user-name>` is the name of the user.

`<pass-enc-alg>` is the symbol of the algorithm used to encode the consecutive password
(It is required from Spring Security 5 to enclose this information. 
More about it is [here](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/reference/htmlsingle/#core-services-password-encoding).).

`<password>` is the password encoded with the algorithm determined by _&lt;pass-enc-alg&gt;_.

`[<role1> <role2> .... <roleN>]` is the optional list of the user roles (or authorities in Spring Security lingo).

An example of the legitimate line which encodes the user with the name **demo-user**, and the password **demo-password**, 
and the role **USER** is here:

```
demo-user:{noop}demo-password USER
```
Note that in the above example the indicated password encoding algorithm is `{noop}` which effectively means that no 
encoding algorithm is used.

The available types of users source are enlisted below in the order they are looked for:

- _File based users list._ The file containing the text lines with users can be referred to in a few ways.
First, the the path determined by the `passwordsFilePath` attribute value is checked (if it is set).
Second, the `passwordsFilePathProperty` attribute is read and if it is set, its value is
considered to be the name of the application property which in turn contains the path to the file. 
Last, the value of the `nsl.sam.passwords-file` application property is read and if it is defined the
assumption is that its value determines the path to the file.

- _Environment variables based user list._ The list of the users can be read from the environment variables.
The `usersEnvPrefix` attribute declares the name prefix of the environment variables whose values are
treated as individual lines with the encoded user, password and roles.

- _Annotation's attribute based user list._ Finally, the list of the text lines with the users is read from the
 array hold in the `users` attribute.

All possible attributes of the 
[@SimpleBasicAuthentication](src/main/kotlin/nsl/sam/method/basicauth/annotation/SimpleBasicAuthentication.kt) 
annotation are presented below:

**Attribute name:** `passwordsFilePath` <br/>
**Attribute type:** `String` <br/>
**Default value:** (empty string) <br/>
**Prevalence level:** Basic attribute, meant for common scenarios.<br/>
**Description:** Path to the file with the users and passwords.

**Attribute name:** `passwordsFilePropertyName` <br/>
**Attribute type:** `String` <br/>
**Default value:** (empty string) <br/>
**Prevalence level:** Basic attribute, meant for common scenarios.<br/>
**Description:** The name of the application property which holds the path to the file with the users and passwords.

**Attribute name:** `detectPasswordsFileChanges` <br/>
**Attribute type:** `Boolean` <br/>
**Default value:** `false` <br/>
**Prevalence level:** Beyond the basic scenarios.<br/>
**Description:** If set to true, the file with the users and passwords will be monitored for changes 
(with one second frequency). In the case when the file changes, it is reread in order to refresh the list of 
the authenticated users.

**Attribute name:** `usersEnvPrefix` <br/>
**Attribute type:** `String` <br/>
**Default value:** (empty string) <br/>
**Prevalence level:** Beyond the basic scenarios.<br/>
**Description:** The name prefix of the environment variables which hold the text line with the encoded user, password and roles.
Each environment variable, whose name begins with this prefix, defines one user and the associated attributes 
(according to the encoding rules presented earlier).

**Attribute name:** `users` <br/>
**Attribute type:** `Array<String>` <br/>
**Default value:** `[]`(empty array) <br/>
**Prevalence level:** Basic attribute, meant for common scenarios.<br/>
**Description:** Allows the developer to hardcode the list of the users into the source code. Each item of this array is
treated as the text line which adheres to the format described in this documentation and which defines
the user, password and roles (authorities).

**Attribute name:** `authenticationEntryPointFactory` <br/>
**Attribute type:** `Array<KClass<out AuthenticationEntryPointFactory>>` <br/>
**Default value:** `[]`(empty array) <br/>
**Prevalence level:** For advanced scenarios.<br/>
**Description:** This attribute can be used to replace the 
[AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html) 
being used with HTTP Basic Auth with
the custom one. This attribute takes the form of an array but only the first item in the array is relevant.
Additional items, if present, are ignored. If the array is not empty then the 
[AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
created by the provided factory will serve unauthenticated requests adhering to the HTTP Basic Auth authentication
schema.

Example usage of [@SimpleBasicAuthentication](src/main/kotlin/nsl/sam/method/basicauth/annotation/SimpleBasicAuthentication.kt)
annotation from Kotlin language (note the usage of 
[@EnableSimpleAuthenticationMethods](src/main/kotlin/nsl/sam/core/annotation/EnableSimpleAuthenticationMethods.kt) 
annotation, and the way the 
[@SimpleBasicAuthentication](src/main/kotlin/nsl/sam/method/basicauth/annotation/SimpleBasicAuthentication.kt)
clarifies details related to the enabled authentication method):

```kotlin
import org.springframework.context.annotation.Configuration
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication

@Configuration
@EnableSimpleAuthenticationMethods(methods = [AuthenticationMethod.SIMPLE_BASIC_AUTH], match="/file-user-info/**")
@SimpleBasicAuthentication(passwordsFilePath = "config/passwords.conf")
class FileCredentialsConfiguration
```

Example of the same configuration done in Java language:

```java
import org.springframework.context.annotation.Configuration;
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods;
import nsl.sam.core.annotation.AuthenticationMethod;
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication;

@Configuration
@EnableSimpleAuthenticationMethods(methods = {AuthenticationMethod.SIMPLE_BASIC_AUTH}, match="/file-user-info/**")
@SimpleBasicAuthentication(passwordsFilePath = "config/passwords.conf")
class FileCredentialsConfiguration{}
```

#### SimpleTokenAuthentication annotations

Link to the source code: 
[@SimpleTokenAuthentication](src/main/kotlin/nsl/sam/method/token/annotation/SimpleTokenAuthentication.kt)

The [@SimpleTokenAuthentication](src/main/kotlin/nsl/sam/method/token/annotation/SimpleTokenAuthentication.kt) 
annotation, if used, always accompanies the 
[@EnableSimpleAuthenticationMethods](src/main/kotlin/nsl/sam/core/annotation/EnableSimpleAuthenticationMethods.kt) 
annotation. It is meant to point out the source of the authenticated tokens and optionally to customize the 
[AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html)
used by the token based authentication mechanism implemented by the simple-authentication-methods library.

There are three different types of token sources but only one type can be selected and used at the same time.
The different source types are searched for in defined order, and the first found active source type wins.
Whereby, as active is considered the source which can provide at least one valid token.

All types of the tokens sources can provide the list of the legitimate tokens in the same format. Each token and the
associated attributes are encoded in one text line of the following format.:

```
<token-value> <user-name> [<role1> <role2> ... <rolneN>]
```

Where the meaning of the individual placeholders is as follows.:

`<token-value>` is the exact value of the authenticated token.

`<user-name>` is the user name the token is mapped to.

`[<role1> <role2> ... <rolneN>]` is the optional list of the user roles which the token is associated with
 (or granted authorities in Spring Security lingo)
  
The list of the available types of the tokens sources is enlisted below in the order they are looked for:

- _File based tokens list_. The list of the authenticated tokens is read from a file. This file itself can be located
in a few ways. First, the path determined by the value of the `tokensFilePath` attribute is checked. Next, the
`tokensFilePathProperty` attribute is read. The value of this attribute is interpreted as the name of the 
application property which holds the path to the file. Finally, the application property `nsl.sam.tokens-file` is read. 
The value of this property is assumed to be the path to the file with the tokens.
 
- _Environment variables based tokens list._ The list of the tokens can be read from the environment variables.
The `tokensEnvPrefix` attribute declares the name prefix of the environment variables whose values are
interpreted as the individual lines with the encoded tokens and associated attributes.
 
- _Annotation's attribute based tokens list._ If no found elsewhere, the list of the text lines with the tokens
is taken from the `tokens` attribute. This attribute is an array of String(s), and each item is considered as
a separate text line with the encoded token and associated attributes.

All possible attributes of the [@SimpleTokenAuthentication] annotation are presented below:

**Attribute name:** `tokensFilePath` <br/>
**Attribute type:** `String` <br/>
**Default value:** (empty string) <br/>
**Prevalence level:** Basic attribute, meant for common scenarios.<br/>
**Description:** The path to the file with tokens.

**Attribute name:** `tokensFilePropertyName` <br/>
**Attribute type:** `String` <br/>
**Default value:** (empty string) <br/>
**Prevalence level:** Basic attribute, meant for common scenarios.<br/>
**Description:** The name of the application property which holds the path to the file with the tokens.

**Attribute name:** `detectTokensFileChanges` <br/>
**Attribute type:** `Boolean` <br/>
**Default value:** `false` <br/>
**Prevalence level:** Beyond the basic scenarios.<br/>
**Description:** If set to true, the tokens file will be monitored for changes (with one second rate). 
If a change is detected then the list of the authenticated tokens used by a server is reloaded.

**Attribute name:** `tokensEnvPrefix` <br/>
**Attribute type:** `String` <br/>
**Default value:** (empty string) <br/>
**Prevalence level:** Beyond the basic scenarios.<br/>
**Description:** The name prefix of the environment variables which hold the text line with the encoded token. 
Each environment variable, whose name begins with this prefix, defines one legitimate token and the associated attributes
(see the annotation level documentation).

**Attribute name:** `tokens` <br/>
**Attribute type:** `Array<String>` <br/>
**Default value:** `[]`(empty array) <br/>
**Prevalence level:** Basic attribute, meant for common scenarios.<br/>
**Description:**  Allows the developer to hardcode the list of the authenticated tokens. 
Each item in the array is interpreted as the text line with the encoded token and the associated attributes
(the user name the token is mapped to, and optionally the roles assigned to this user).
                         
**Attribute name:** `authenticationEntryPointFactory` <br/>
**Attribute type:** `Array<KClass<out AuthenticationEntryPointFactory>>` <br/>
**Default value:** `[]`(empty array) <br/>
**Prevalence level:** For advanced scenarios.<br/>
**Description:** This attribute can be used to set custom 
[AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html) 
being used by the token based authentication method when an unauthenticated token is recognized. 
This attribute takes the form of an array but only the first item in the array is relevant. Additional items, 
if present, are ignored.
If the array is not empty then the [AuthenticationEntryPoint](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/AuthenticationEntryPoint.html) created by the provided factory will handle the requests containing the unauthenticated tokens.

Example usage of 
[@SimpleTokenAuthentication](src/main/kotlin/nsl/sam/method/token/annotation/SimpleTokenAuthentication.kt)
annotation from Kotlin language (note the usage of 
[@EnableSimpleAuthenticationMethods](src/main/kotlin/nsl/sam/core/annotation/EnableSimpleAuthenticationMethods.kt) 
annotation, and the way the 
[@SimpleTokenAuthentication](src/main/kotlin/nsl/sam/method/token/annotation/SimpleTokenAuthentication.kt)
clarifies details related to the enabled authentication method):

```kotlin
import org.springframework.context.annotation.Configuration
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.core.annotation.AuthenticationMethod
import nsl.sam.method.token.annotation.SimpleTokenAuthentication

@Configuration
@EnableSimpleAuthenticationMethods(methods = [AuthenticationMethod.SIMPLE_TOKEN], match="/file-user-info/**")
@SimpleTokenAuthentication(tokensFilePath = "config/tokens.conf")
class FileCredentialsConfiguration
```

Example of the same configuration done in Java language:

```java
import org.springframework.context.annotation.Configuration;
import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods;
import nsl.sam.core.annotation.AuthenticationMethod;
import nsl.sam.method.token.annotation.SimpleTokenAuthentication;

@Configuration
@EnableSimpleAuthenticationMethods(methods = {AuthenticationMethod.SIMPLE_TOKEN}, match="/file-user-info/**")
@SimpleTokenAuthentication(tokensFilePath = "config/tokens.conf")
class FileCredentialsConfiguration{}
```


### Custom authorization error response (advanced usage) 

There is an assumption that the whole application, regardless of the used authentication method or the requested path, 
is handled by the same instance of Spring Security 
[AccessDeniedHandler](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/access/AccessDeniedHandler.html).

If not configured differently, the default handler used internally by Spring Security is left in place.

However, the developer can provide customized implementation of 
[AccessDeniedHandler](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/access/AccessDeniedHandler.html).
In order to use the custom 
[AccessDeniedHandler](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/access/AccessDeniedHandler.html)
it is sufficient to provide it in the application context, that is to define a bean of [AccessDeniedHandler](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/access/AccessDeniedHandler.html) type.

NOTE: This way of replacing the 
[AccessDeniedHandler](https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/api/org/springframework/security/web/access/AccessDeniedHandler.html)
is specific to the simple-authentication-methods library. It would not
work with the plain Spring Security or Spring Boot application.
