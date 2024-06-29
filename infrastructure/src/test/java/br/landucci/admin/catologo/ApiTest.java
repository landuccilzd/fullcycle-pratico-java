package br.landucci.admin.catologo;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public interface ApiTest {

    JwtRequestPostProcessor ADMIN_JWT = jwt().authorities(new SimpleGrantedAuthority("ROLE_CODEFLIX_ADMIN"));
    JwtRequestPostProcessor CATEGORIES_JWT = jwt().authorities(new SimpleGrantedAuthority("ROLE_CODEFLIX_CATEGORIES"));
    JwtRequestPostProcessor GENRES_JWT = jwt().authorities(new SimpleGrantedAuthority("ROLE_CODEFLIX_GENRES"));
    JwtRequestPostProcessor CASTMEMBERS_JWT = jwt().authorities(new SimpleGrantedAuthority("ROLE_CODEFLIX_CASTMEMBERS"));
    JwtRequestPostProcessor VIDEOS_JWT = jwt().authorities(new SimpleGrantedAuthority("ROLE_CODEFLIX_VIDEOS"));
}
