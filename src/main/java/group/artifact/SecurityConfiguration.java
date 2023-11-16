package group.artifact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import group.artifact.views.LoginView;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends VaadinWebSecurity {
    @Autowired
    private SidAuthenticationFilter sidAuthenticationFilter;

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // configure ignoring security filters for static resources
        web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/images/**", "GET"));
    }

    /*
     * blocks requests to all views and endpoints by default
     * except: internal request or public views annotated with @AnonymousAllowed
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // perform sid check, otherwise continue with default auth
        http.addFilterBefore(sidAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        super.configure(http); // apply default vaadin configuration

        setLoginView(http, LoginView.class); // page for denied users
    }
}
