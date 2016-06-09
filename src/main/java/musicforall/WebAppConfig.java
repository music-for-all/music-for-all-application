package musicforall;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

@Configuration
@ComponentScan("musicforall.web")
@EnableWebMvc
public class WebAppConfig {
	@Bean
	public VelocityViewResolver setupViewResolver() {
		VelocityViewResolver  resolver = new VelocityViewResolver();
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
