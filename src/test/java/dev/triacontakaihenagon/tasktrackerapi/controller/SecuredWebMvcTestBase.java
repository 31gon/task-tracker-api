package dev.triacontakaihenagon.tasktrackerapi.controller;

import dev.triacontakaihenagon.tasktrackerapi.security.JwtAuthEntryPoint;
import dev.triacontakaihenagon.tasktrackerapi.security.JwtAuthFilter;
import dev.triacontakaihenagon.tasktrackerapi.security.JwtService;
import dev.triacontakaihenagon.tasktrackerapi.security.SecurityConfig;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Import({SecurityConfig.class, JwtAuthFilter.class, JwtAuthEntryPoint.class})
abstract class SecuredWebMvcTestBase {

    @MockitoBean protected JwtService jwtService;
    @MockitoBean protected UserDetailsService userDetailsService;
    @MockitoBean protected AuthenticationManager authenticationManager;
}