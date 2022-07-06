package hello.tutorial.config;

import java.util.Collection;
import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.ofNullable(SecurityContextHolder.getContext())
				.map(SecurityContext::getAuthentication)
				.map(authentication -> {
					Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
					boolean isUser = auth.contains(new SimpleGrantedAuthority("ROLE_USER"));
					if (isUser) {
						return authentication.getName();
					}
					return null;
				});
	}
}
