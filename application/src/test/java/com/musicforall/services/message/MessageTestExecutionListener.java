package com.musicforall.services.message;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * @author IliaNik on 14.08.2016.
 */
public class MessageTestExecutionListener extends AbstractTestExecutionListener {
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        final MessageBootstrap messageBootstrap = testContext.getApplicationContext().getBean(MessageBootstrap.class);
        messageBootstrap.fillDatabase();
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        final MessageBootstrap messageBootstrap = testContext.getApplicationContext().getBean(MessageBootstrap.class);
        messageBootstrap.clean();
    }
}