package com.musicforall.web;

import com.musicforall.history.service.HistoryService;
import com.musicforall.model.Track;
import com.musicforall.services.track.TrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
public class WelcomeController {

    private static final Logger LOG = LoggerFactory.getLogger(WelcomeController.class);
    public static final String SPRING_SECURITY_LAST_EXCEPTION = "SPRING_SECURITY_LAST_EXCEPTION";

    @Autowired
    private TrackService trackService;

    @Autowired
    private HistoryService historyService;

    @RequestMapping(value = {"/", "welcome"})
    public String welcome(Model model, HttpServletRequest request) {
        LOG.debug("Requested /welcome");

        /* Check if there has been an authentication failure. */
        final Object exception = request.getSession().getAttribute(SPRING_SECURITY_LAST_EXCEPTION);
        if (exception != null) {
            model.addAttribute(SPRING_SECURITY_LAST_EXCEPTION, exception);
        }
        model.addAttribute("request", request);
        return "welcome";
    }

    @RequestMapping(value = "welcome/getByPopularity",  method = RequestMethod.GET)
    @ResponseBody
    public Collection<Track> getByPopularity(){
        return historyService.getTheMostPopularTrack()
                .stream().limit(10)
                .map(trackId -> trackService.get(trackId)).collect(Collectors.toList());
    }
}
