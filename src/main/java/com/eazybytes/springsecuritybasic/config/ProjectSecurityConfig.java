package com.eazybytes.springsecuritybasic.config;

import com.eazybytes.springsecuritybasic.filter.AuthoritiesLoggingAfterFilter;
import com.eazybytes.springsecuritybasic.filter.RequestValidationBeforeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .configurationSource(
            request -> {
              CorsConfiguration config = new CorsConfiguration();
              config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
              config.setAllowedMethods(Collections.singletonList("*"));
              config.setAllowCredentials(true);
              config.setAllowedHeaders(Collections.singletonList("*"));
              config.setMaxAge(3600L);
              return config;
            });
    http.csrf()
        .ignoringAntMatchers("/*")
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .and()
        .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers("/myAccount")
        .hasRole("USER")
        .antMatchers("/myBalance")
        .hasAnyRole("USER", "ADMIN")
        .antMatchers("/myLoans")
        .hasRole("ROOT")
        .antMatchers("/myCards")
        .authenticated()
        .antMatchers("/notices")
        .permitAll()
        .antMatchers("/contact")
        .permitAll()
        .and()
        .formLogin()
        .and()
        .httpBasic();
  }

  /*@Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.inMemoryAuthentication().withUser("admin").password("12345").authorities("admin").and()
              .withUser("user").password("12345").authorities("read").and()
              .passwordEncoder(NoOpPasswordEncoder.getInstance());
  }*/

  /* @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
      UserDetails user1 = User.withUsername("admin").password("12345").authorities("admin").build();
      UserDetails user2 = User.withUsername("user").password("12345").authorities("read").build();
      userDetailsManager.createUser(user1);
      userDetailsManager.createUser(user2);
      auth.userDetailsService(userDetailsManager);
  }*/

  /*@Bean
  public UserDetailsService userDetailsService(DataSource dataSource) {
      return new JdbcUserDetailsManager(dataSource);
  }*/

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
