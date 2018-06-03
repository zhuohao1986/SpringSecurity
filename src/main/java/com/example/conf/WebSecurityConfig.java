package com.example.conf;

/**
 * spring security配置
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.inMemoryAuthentication().withUser("yiqun").password("123456")
				.roles("USER");
	}
	
	
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/signup", "/about").permitAll()
				.antMatchers("/admin/**").hasRole("ADMIN") // 设置只有ADMIN 权限的人才能访问指定URL
				.anyRequest().authenticated() //除了指定URL  其他都需要验证
				.and()
			.formLogin()
				.loginPage("/login") 
				.permitAll()
				.and()
			.logout()    //提供注销支持 ，AntPathRequestMatcher会自动应用            
//			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			//提供注销支持并且指定跳转页面
				//设置触发注销操作的URL (默认是/logout), 如果CSRF内启用（默认是启用的）的话这个请求的方式被限定为POST。
//				.logoutUrl("/logout")
				//注销之后跳转的URL。默认是/login?logout。指定url之后会跳到指定的注销页面，不会注销后二次登陆
				.logoutSuccessUrl("/login")                                           
				//自己设置logoutSuccessHandler,如果指定了这个选项那么logoutSuccessUrl()的设置会被忽略。                             
//				.logoutSuccessHandler(logoutSuccessHandler) 
				//指定是否在注销时让HttpSession无效。 默认设置为 true
				.invalidateHttpSession(true) 
				//添加一个LogoutHandler.默认SecurityContextLogoutHandler会被添加为最后一个LogoutHandler
//				.addLogoutHandler(logoutHandler)  
				//允许指定在注销成功时将移除的cookie。
//				.deleteCookies(cookieNamesToClear)                                       
				.and()
			.httpBasic();//允许用户使用HTTP基于验证进行认证
		http.csrf()
//				.disable();//禁用CSRF
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());//等于disable
		
	}
}

