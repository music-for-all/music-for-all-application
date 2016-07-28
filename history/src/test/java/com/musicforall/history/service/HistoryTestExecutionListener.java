package com.musicforall.history.service;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * @author IliaNik on 28.07.2016.
 */

public class HistoryTestExecutionListener extends AbstractTestExecutionListener {
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        super.afterTestClass(testContext);
        final HistoryBootstrap historyBootstrap =
                testContext.getApplicationContext().getBean(HistoryBootstrap.class);
        historyBootstrap.fillDatabase();
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        super.beforeTestClass(testContext);
        final HistoryBootstrap historyBootstrap =
                testContext.getApplicationContext().getBean(HistoryBootstrap.class);
        historyBootstrap.clean();
    }
}