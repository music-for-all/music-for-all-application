package musicforall.web;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
public class IndexController {

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	public IndexController() {
		logger.debug("Sample Debug Message");

		sessionFactory.getCurrentSession().createQuery("from track").getFirstResult();
	}

	@RequestMapping("/")
	public String index(Model model) {

		model.addAttribute("date", new Date());
		return "index";
	}
}
