package group.artifact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import group.artifact.views.LoginView;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends VaadinWebSecurity {
    @Autowired
    private SidAuthenticationFilter sidAuthenticationFilter;

    /*
     * blocks requests to all views and endpoints by default
     * except: internal request or public views annotated with @AnonymousAllowed
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // perform sid check, otherwise continue with default auth
        http.authorizeRequests(req -> req
                .requestMatchers(new AntPathRequestMatcher("/images/*")).permitAll() // access to all images
        );
        http.addFilterBefore(sidAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        super.configure(http); // apply default vaadin configuration
        setLoginView(http, LoginView.class); // page for denied users
    }
}
