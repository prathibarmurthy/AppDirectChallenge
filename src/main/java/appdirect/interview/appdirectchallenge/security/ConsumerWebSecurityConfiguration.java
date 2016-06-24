package main.java.appdirect.interview.appdirectchallenge.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.ProtectedResourceDetails;
import org.springframework.security.oauth.provider.*;
import org.springframework.security.oauth.provider.filter.OAuthProviderProcessingFilter;
import org.springframework.security.oauth.provider.filter.ProtectedResourceProcessingFilter;
import org.springframework.security.oauth.provider.token.InMemoryProviderTokenServices;
import org.springframework.security.openid.OpenIDAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ConsumerWebSecurityConfiguration extends
		WebSecurityConfigurerAdapter {

	@Autowired
	private LogoutSuccessHandlerImpl logoutHandler;

	@Autowired
	private UserDetailsServiceImpl userOperationsService;

	@Value("${oauth.consumer.key}")
	private String oAuthConsumerKey;

	@Value("${oauth.consumer.secret}")
	private String oAuthConsumerSecret;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/*.{js,html}", "/webjars/**");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler(logoutHandler);
		httpSecurity.csrf().disable();
		httpSecurity.authorizeRequests().antMatchers("/**").permitAll().anyRequest()
				.authenticated();
		httpSecurity.addFilterBefore(oAuthProviderProcessingFilter(),
				OpenIDAuthenticationFilter.class);
		httpSecurity.openidLogin().authenticationUserDetailsService(userOperationsService)
				.loginProcessingUrl("/login/openid").permitAll()
				.defaultSuccessUrl("/");

		
	}

	@Bean
	public OAuthProviderProcessingFilter oAuthProviderProcessingFilter() {

		final ProtectedResourceProcessingFilter protectedResourceProcessingFilter = new ProtectedResourceProcessingFilter() {

			@Override
			protected boolean requiresAuthentication(
					final HttpServletRequest httpServletRequest,
					final HttpServletResponse httpServletResponse,
					final FilterChain filterChain) {

				if (new AntPathRequestMatcher("/api/notification/**")
						.matches(httpServletRequest)) {
					OAuthProcessingFilterEntryPoint oAuthProcessingFilter = new OAuthProcessingFilterEntryPoint();
					setAuthenticationEntryPoint(oAuthProcessingFilter);
					String realm = httpServletRequest.getRequestURL().toString();
					oAuthProcessingFilter.setRealmName(realm);
					return true;
				}
				else
					return false;
			}
		};
		protectedResourceProcessingFilter.setConsumerDetailsService(consumerDetailsService());
		protectedResourceProcessingFilter.setTokenServices(inMemoryProviderTokenServices());

		return protectedResourceProcessingFilter;
	}

	@Bean
	public ConsumerDetailsService consumerDetailsService() {
		final BaseConsumerDetails baseConsumerDetails = new BaseConsumerDetails();
		baseConsumerDetails.setConsumerKey(oAuthConsumerKey);
		baseConsumerDetails.setRequiredToObtainAuthenticatedToken(false);
		baseConsumerDetails.setSignatureSecret(new SharedConsumerSecretImpl(
				oAuthConsumerSecret));
		
		final InMemoryConsumerDetailsService baseConsumer = new InMemoryConsumerDetailsService();
		baseConsumer
				.setConsumerDetailsStore(new HashMap<String, ConsumerDetails>() {
					{
						put(oAuthConsumerKey, baseConsumerDetails);
					}
				});
		return baseConsumer;
	}

	@Bean
	public InMemoryProviderTokenServices inMemoryProviderTokenServices() {
		return new InMemoryProviderTokenServices();
	}

	@Bean
	public ProtectedResourceDetails protectedResourceDetails() {
		final BaseProtectedResourceDetails baseProtectedResource = new BaseProtectedResourceDetails();
		baseProtectedResource.setConsumerKey(oAuthConsumerKey);
		baseProtectedResource.setSharedSecret(new SharedConsumerSecretImpl(
				oAuthConsumerSecret));
		return baseProtectedResource;
	}

}