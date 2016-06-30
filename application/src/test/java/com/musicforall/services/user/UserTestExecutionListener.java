package com.musicforall.services.user;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Created by kgavrylchenko on 29.06.16.
 */
public class UserTestExecutionListener extends AbstractTestExecutionListener {
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        final UserBootstrap userBootstrap = testContext.getApplicationContext().getBean(UserBootstrap.class);
        userBootstrap.fillDatabase();
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        final UserBootstrap userBootstrap = testContext.getApplicationContext().getBean(UserBootstrap.class);
        userBootstrap.clean();
    }
}
