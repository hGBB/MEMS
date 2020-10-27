package de.unipa.hams.hamer.conf.prod;

import de.unipa.hams.hamer.persistence.api.NodeRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MonitoringAuthenticationFilter extends OncePerRequestFilter {

  private final NodeRepository nodeRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
    String token = request.getHeader("Node-Authorization");
    if (token != null) {
      token = token.replace("Bearer ", "");
      var creds = token.split("\\s");
      var node = nodeRepository.findById(Long.parseLong(creds[0])).orElseThrow(() -> new AccessDeniedException("Fuck off"));
      if (!passwordEncoder.matches(creds[1], node.getUuid())) {
        throw new AccessDeniedException("UUID invalid!");
      }
      List<SimpleGrantedAuthority> gas = new ArrayList<>();
      gas.add(new SimpleGrantedAuthority("ROLE_NODE"));
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(node.getId(), null, gas);
      auth.eraseCredentials();
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    filterChain.doFilter(request, response);
  }
}
