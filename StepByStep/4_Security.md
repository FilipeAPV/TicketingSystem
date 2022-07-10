# 1. Development Plan

- Authentication

  - Apply Spring Security and Bean Validation Dependencies

    - Define configuration class

  - Create:

    - Customized login form with possibility to register

    - HTML registration form

  - Data validation

  - Auditing

    - BaseEntity configuration (@Created..., @LastModified...)

  - Persist data to the DB

  - Send user to login page again

# 2. Apply Spring Security Dependency

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

# 2.1 Define configuration class

- Create a class inside a newly created **security** package. Ex:

```
@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .mvcMatchers("/","/public/**").permitAll()
                .mvcMatchers("/css/**","/fontawesome/**","/img/**","/js/**").permitAll()
                .mvcMatchers("/**").authenticated()
            .and()
                .formLogin().loginPage("/public/login").defaultSuccessUrl("/dashboard")
                .failureUrl("/public/login?error=true")
            .and()
                .logout().logoutSuccessUrl("/public/login?logout=true").invalidateHttpSession(true)
            .and()
                .httpBasic();
        return httpSecurity.build();
    }
}
```

1. Spring Security acts as a **filter** between user requests and resources on the server.

2. Order **matters**:

   1. Does the request match "/" or "/public/\*\*" ?

      1. If yes, allow access and **STOP** evaluation (not more rules are used)

3. With the code above we define:

   1. Any request to the paths **"/"** and **"/public/**"\*\* should be allowed to everyone.

   2. Same for the paths regarding folders inside **/resources/static** like **css**

   3. Any other request that doesn't meet the above criteria is restricted to **authenticated** users.

      1. If the user is not authenticated, it will be automatically redirected to the login page: **"/public/login"**

         1. If the user authenticates successfully, it will be redirected to **"/dashboard"**

         2. Else, it will be redirected to **"/public/login?error=true"**
