package hello.tutorial.service;

import hello.tutorial.dto.UserDto;
import hello.tutorial.entity.Authority;
import hello.tutorial.entity.User;
import hello.tutorial.repository.UserRepository;
import hello.tutorial.util.SecurityUtil;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserDto signup(UserDto userDto) {
		if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
			throw new RuntimeException("이미 가입되어 있는 유저 입니다.");
		}

		Authority authority = Authority.builder()
				.authorityName("ROLE_USER")
				.build();

		User user = User.builder()
				.username(userDto.getUsername())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.nickname(userDto.getNickname())
				.authorities(Collections.singleton(authority))
				.activated(true)
				.build();

		return UserDto.from(userRepository.save(user));
	}

	public UserDto getUserWithAuthorities(String username) {
		return UserDto
				.from(userRepository.findOneWithAuthoritiesByUsername(username)
						.orElse(null));
	}

	public UserDto getMyUserWithAuthorities() {

		return UserDto
				.from(SecurityUtil
						.getCurrentUsername()
						.flatMap(userRepository::findOneWithAuthoritiesByUsername)
						.orElse(null));
	}
}
