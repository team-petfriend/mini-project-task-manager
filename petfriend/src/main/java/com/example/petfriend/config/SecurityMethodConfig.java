package com.example.petfriend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/** 스프링 설정 클래스 */
@Configuration
/** 스프링 시큐리에서 메서드 보안 기능을 활성화
* @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter 같은
* 어노테이션 기반 보안 기능 사용 가능하게 만들어주고, 메서드 단위에서 접근 권한을 세밍하게 제어가 가능하게 만들어준다.
* */
@EnableMethodSecurity(prePostEnabled = true)

public class SecurityMethodConfig { }
