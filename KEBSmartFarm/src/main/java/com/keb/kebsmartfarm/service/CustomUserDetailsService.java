package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.entity.User;
import com.keb.kebsmartfarm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(Error.USER_DOES_NOT_MACTH, userId)));
    }

    private UserDetails createUserDetails(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getAuthority().toString());
        return new org.springframework.security.core.userdetails.User(
                // 여기서 넣어줘야 하는 건 유일하게 멤버를 구분할 수 있는 memberseqnum
                String.valueOf(user.getUserSeqNum()),
                user.getUserPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

}
