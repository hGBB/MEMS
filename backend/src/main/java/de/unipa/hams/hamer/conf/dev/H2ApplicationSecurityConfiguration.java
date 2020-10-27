package de.unipa.hams.hamer.conf.dev;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Order(80)
@Configuration
@Qualifier("h2ApplicationSecurity")
@Profile("h2")
public class H2ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("h2-console/**").permitAll()
      .and().headers().frameOptions().disable()
      .and().csrf().disable();
  }
}
