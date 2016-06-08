package musicforall.web;

import musicforall.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	public IndexController() {
		HibernateUtil.getSessionFactory();
		logger.debug("Sample Debug Message");
		logger.trace("Sample Trace Message");
	}

	@RequestMapping("/")
	public String index(Model model) {

		model.addAttribute("date", new Date());
		return "index";
	}
}
