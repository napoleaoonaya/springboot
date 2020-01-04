package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebConfigSegurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;

	@Override //Configura as solicitações de acesso por http
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable()//desativa as configurações padrão de memoria
		.authorizeRequests()//permite restringir acessos
		.antMatchers(HttpMethod.GET,"/").permitAll()//Qualquer usuario acessa a parte inicial
		.antMatchers(HttpMethod.GET,"/cadastropessoa").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and().formLogin().permitAll()//permite qualquer usuario
		.and().logout()//mapeia url de logout e invalida usuario autenticado
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
	@Override//Cria autentificação do usuario com o banco ou em memoria
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
	auth.userDetailsService(implementacaoUserDetailsService)
	.passwordEncoder(new BCryptPasswordEncoder());
		
		
//		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
//		.withUser("onaya")
//		.password("$2a$10$zSmjNNur4XPM87Xz5z3NRec.DCWUFD8Kn.SiArwgRtrSgK2PJQM0C")
//		.roles("admin");
		
	}
	
	@Override //Ignora URL especificas
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/materialize/**");
	}
	
}
