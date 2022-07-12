# User needs to login and logout

## Development process

- Add role field to all users (default of ROLE_USER)

  - I've decided to apply a ROLE to each user by default because I could not create an UsernamePasswordAuthenticationToken without it.
  - This will also allow me to provide access to certain options by ROLE.

- Credential verification & password hashing

  - We'll apply custom credential verification

- Dashboard Presentation

- Logout

## Implementation

- Password hashing

  1. We need to create a @Bean of the Class PasswordEncoder.

  2. This @Bean will be used to **Hash** and **compare** raw passwords to hashed ones.
  3. Hashing algorithm is bcrypt.

  4. ```@Bean
     public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
     }
     ```

- Credential verification

  1.  We informed Spring Security (4_Security.md) of the path of the login page:

      1. `.formLogin().loginPage("/public/login").defaultSuccessUrl("/dashboard")`

  2.  In the login form, we send the required information to that path:

      1. ```
         <form id="contact-form" th:action="@{/public/login}" method="POST" class="tm-bg-color-6 tm-contact-form">
             <div class="form-group">
                 <input type="email" name="username" id="username" class="form-control" placeholder="Email" required="" />
             </div>
             <div class="form-group">
                 <input type="password" name="password" id="password" class="form-control" placeholder="Password" required="" />
             </div>
         ```

      2. Because Spring Security takes care of it, we don't need to write any logic regarding this @PostMapping inside a @Controller.

  3.  Now, we are going to create a Class that **implements AuthenticationProvider**.

      1.  This Interface has methods that will perform security verifications and that have access to the data inserted by the user inside the login form.

      2.  ```
          /*
          The authentication object as input parameter will have the username and password entered in the login page.
          */
          @Override
          public Authentication authenticate(Authentication authentication) throws AuthenticationException {
              String email = authentication.getName();
              String psw = authentication.getCredentials().toString();

              User user = userRepository.findByEmail(email);
              if (user != null && user.getId() > 0 && passwordEncoder.matches(psw, user.getPassword())) {
                  return new UsernamePasswordAuthenticationToken(
                          user.getFirstName(), null, getGrantedAuthorities(user)
                  );
              } else {
                  throw new BadCredentialsException("Invalid Credentials!");
              }

          }

          private List<GrantedAuthority> getGrantedAuthorities(User user) {
              List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
              grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
              return grantedAuthorities;
          }
          ```

          1.  Receive username and password from Login form
          2.  If user exists and has the same password as the one inserted in the form (verification raw and bcrypt)
              1. A new UsernamePasswordAuthenticationToken will be created with:
                 1. The user first name
                 2. Null instead of the actual password for security reasons
                 3. A list containing the user role.

- Dashboard Presentation

  1.  As previously mentioned, we informed Spring Security (4_Security.md) of the path of the login page and also the path to follow in case of successful or unsuccessful credentials evaluation.

      1. `.formLogin().loginPage("/public/login").defaultSuccessUrl("/dashboard").failureUrl("/public/login?error=true")`

- Logout

1. Spring Security is aware of the path that it needs to follow when the user decides to logout:

   1. `.logout().logoutSuccessUrl("/public/login?logout=true").invalidateHttpSession(true)`

2. We provide the option to the user as a link to the path "/logout":

   1. `<a th:href="@{/public/logout}">Logout</a>`

3. Write the @Controller that will handle the GET request to the above mentioned path:

   1. ```
       @GetMapping("/logout")
       public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
           Authentication auth = SecurityContextHolder.getContext().getAuthentication();
           if (auth != null){
               new SecurityContextLogoutHandler().logout(request, response, auth);
           }
           return "redirect:/public/login?logout=true";
       }
      ```

   2. This code here is above my Spring Security skills, so I cannot provide a detailed explanation. If in the future I'm required to get more information about it, then I'll dig deeper.
