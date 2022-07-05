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
					for (GrantedAuthority grantedAuthority : auth) {
						System.out.println("grantedAuthority = " + grantedAuthority);
					}
					boolean isUser = auth.contains(new SimpleGrantedAuthority("ROLE_USER"));
					if (!isUser) {
						System.out.println("널");
						return null;
					}
					System.out.println("널아님");
					return authentication.getName();
				});
	}
}
