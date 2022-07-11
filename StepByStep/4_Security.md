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

# 2.2 Data validation

I've used the Jakarta Bean Validation project to validate our user fields. It's important to note that client side validations, via HTML attribute `required=""` are easily circumvented.

- The **default validations** are pretty straight forward to apply:

```
@NotBlank(message = "Email cannot be blank")
@Email(message = "Please provide a valid email address")
private String email;
```

- **Custom validations** require some more code. Example of a custom validation that compares two fields for equality, used for the email and password fields.

  - Create annotation @FieldsValueMatch:

  ```
  @Constraint(validatedBy = FieldsValueMatchValidator.class) // Class that holds the logic
  @Target({ElementType.TYPE}) // Applies to Class, interfaces, etc
  @Retention(RetentionPolicy.RUNTIME) // Validation happens at runtime
  public @interface FieldsValueMatch {
      Class<?>[] groups() default {};
      Class<? extends Payload>[] payload() default {};

      String message() default "Field values don't match!";

      String field();

      String fieldMatch();

      @Target({ ElementType.TYPE })
      @Retention(RetentionPolicy.RUNTIME)
      @interface List {
          FieldsValueMatch[] value();
      }
  }
  ```

  - Create validation logic

    ```
    public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {
    // We'll accept the entire POJO and perform the validation on two of it's fields

        private String field;
        private String fieldMatch;

        @Override
        public void initialize(FieldsValueMatch constraintAnnotation) {
            this.field = constraintAnnotation.field();
            this.fieldMatch = constraintAnnotation.fieldMatch();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            Object fieldValue = new BeanWrapperImpl(value)
                    .getPropertyValue(field);
            Object fieldMatchValue = new BeanWrapperImpl(value)
                    .getPropertyValue(fieldMatch);
            if (fieldValue != null) {
                return fieldValue.equals(fieldMatchValue);
            } else {
                return fieldMatchValue == null;
            }
        }

    }
    ```

    - Annotate the User entity with @FieldsValueMatch

    ```
    @FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords must match"
        ),
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "confirmEmail",
                message = "Email addresses must match"
        )
    })
    public class User extends BaseEntity{
    ```
