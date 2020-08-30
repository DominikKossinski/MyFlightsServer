package pl.kossa.myflightsserver.config.security

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var unauthorizedHandler: AuthenticationEntryPoint

    @Autowired
    lateinit var securityFilter: SecurityFilter

    fun corsConfiguration(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            this.allowCredentials = true
            this.allowedHeaders = arrayListOf("*")
            this.allowedMethods = arrayListOf("*")
            this.allowedOrigins = arrayListOf("*")
            this.maxAge = 3600
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    override fun configure(http: HttpSecurity?) {
        http
                ?.cors()
                ?.configurationSource(corsConfiguration())
                ?.and()
                ?.csrf()?.disable()
                ?.formLogin()?.disable()
                ?.httpBasic()?.disable()
                ?.exceptionHandling()?.authenticationEntryPoint(unauthorizedHandler)
                ?.and()?.authorizeRequests()
                ?.antMatchers(HttpMethod.GET, "/v3/api-docs", "/v3/api-docs/**", "/configuration/ui", "/swagger-resources/**", "/configuration/**",
                        "/swagger-ui.html", "/swagger-ui/**", "/webjars/**",
                        "/csrf")?.permitAll()
                ?.antMatchers(HttpMethod.OPTIONS, "/**")?.permitAll()
                ?.anyRequest()?.authenticated()?.and()
                ?.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter::class.java)
                ?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.authenticationProvider(tokenAuthProvider())
    }

    fun tokenAuthProvider(): AuthenticationProvider {
        return object : AuthenticationProvider {
            override fun authenticate(authentication: Authentication?): Authentication {
                return authentication!!
            }

            override fun supports(authentication: Class<*>?): Boolean {
                return true
            }

        }
    }
}