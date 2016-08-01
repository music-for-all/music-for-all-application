package com.musicforall.services.track;

import com.musicforall.services.playlist.PlaylistBootstrap;
import com.musicforall.services.user.UserBootstrap;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class TrackTestExecutionListener extends AbstractTestExecutionListener {
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        super.afterTestClass(testContext);

        final UserBootstrap userBootstrap = testContext.getApplicationContext().getBean(UserBootstrap.class);
        userBootstrap.fillDatabase();
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        super.beforeTestClass(testContext);

        final UserBootstrap userBootstrap = testContext.getApplicationContext().getBean(UserBootstrap.class);
        userBootstrap.clean();
    }
}
