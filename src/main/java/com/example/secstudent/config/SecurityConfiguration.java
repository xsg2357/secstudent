package com.example.secstudent.config;

import com.example.secstudent.filter.ImageValidateCodeFilter;
import com.example.secstudent.filter.SmsValidateCodeFilter;
import com.example.secstudent.handler.MyAuthenticationFailureHandler;
import com.example.secstudent.handler.MyAuthenticationSuccessHandler;
import com.example.secstudent.handler.MyLogoutSuccessHandler;
import com.example.secstudent.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //图片验证码
        ImageValidateCodeFilter imageValidateCodeFilter = new ImageValidateCodeFilter();
        imageValidateCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        //短信验证码
        SmsValidateCodeFilter smsValidateCodeFilter = new SmsValidateCodeFilter();
        smsValidateCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

//        maximumSessions：最大会话数量，设置为1表示一个用户只能有一个会话
//        expiredSessionStrategy：会话过期策略

        /*http.csrf().disable()
                .antMatchers("/login", "/session/invalid").permitAll()
                .sessionManagement()
                .invalidSessionUrl("/session/invalid")
                .maximumSessions(1)
                .expiredSessionStrategy(new MyExpiredSessionStrategy())*/

//        阻止用户第二次登录
//        sessionManagement也可以配置 maxSessionsPreventsLogin：boolean值，当达到maximumSessions设置的最大会话个数时阻止登录。
        /*http.csrf().disable()
                .antMatchers("/login", "/session/invalid").permitAll()
                .sessionManagement()
                .invalidSessionUrl("/session/invalid")
                .maximumSessions(1)
                .expiredSessionStrategy(new MyExpiredSessionStrategy())
                .maxSessionsPreventsLogin(true)
                .and().and()
                .logout().permitAll();*/

        http.csrf().disable()
                // 配置需要认证的请求
                .authorizeRequests()
                .antMatchers("/login", "/session/invalid",
                        "/code/image","/code/sms",  "/logout","/signOut").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                //session会话管理
                .sessionManagement()
                .invalidSessionUrl("/session/invalid")
                .maximumSessions(1)
                .expiredSessionStrategy(new MyExpiredSessionStrategy())
                .maxSessionsPreventsLogin(true)
                .and()
                .and()
                .addFilterBefore(imageValidateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                // 登录表单相关配置
                .addFilterBefore(smsValidateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                // 登录表单相关配置
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(myAuthenticationSuccessHandler)
                .failureUrl("/login?error")
                .permitAll()
                .and()
                //记住我
                .rememberMe()
                .userDetailsService(myUserDetailsService)
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60 * 60 * 60 * 30)
                .and()
                // 登出相关配置
                .logout()
                .permitAll();

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/static/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);

        return tokenRepository;
    }
}
