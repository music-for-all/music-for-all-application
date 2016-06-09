package musicforall;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityView;

@Configuration
@ComponentScan("musicforall.web")
@EnableWebMvc
public class WebAppConfig {
	@Bean
	public UrlBasedViewResolver setupViewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setCache(true);
		resolver.setPrefix("");
		resolver.setSuffix(".vm");
		resolver.setViewClass(VelocityView.class);
		return resolver;
	}

	@Bean
	public VelocityConfigurer setupVelocityConfig() {
		VelocityConfigurer conf = new VelocityConfigurer();
		conf.setResourceLoaderPath("/WEB-INF/velocity/");
		return conf;
	}
}
