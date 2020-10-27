package de.unipa.hams.hamer.conf.prod;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(1001)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MonitoringSecurtiyConfiguration extends WebSecurityConfigurerAdapter {

  private final MonitoringAuthenticationFilter monitoringAuthenticationFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.antMatcher("/mon")
      .authorizeRequests()
      .antMatchers(HttpMethod.POST).permitAll()
      .anyRequest().authenticated()
      .and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .addFilterAfter(monitoringAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
