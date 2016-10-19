package com.musicforall.services;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ENikolskiy.
 */
@Component
public class Notifier {
    @Scheduled(fixedRate = 2000)
    public void processQueues() {
        LoggerFactory.getLogger(Notifier.class).info("SCHEDULED!");
    }
}