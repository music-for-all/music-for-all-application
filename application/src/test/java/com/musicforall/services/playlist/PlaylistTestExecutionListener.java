package com.musicforall.services.playlist;

import com.musicforall.services.user.UserBootstrap;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Created by kgavrylchenko on 29.06.16.
 */
public class PlaylistTestExecutionListener extends AbstractTestExecutionListener {
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        super.afterTestClass(testContext);
        final PlaylistBootstrap playlistBootstrap =
                testContext.getApplicationContext().getBean(PlaylistBootstrap.class);
        playlistBootstrap.fillDatabase();
        final UserBootstrap userBootstrap = testContext.getApplicationContext().getBean(UserBootstrap.class);
        userBootstrap.fillDatabase();
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        super.beforeTestClass(testContext);
        final PlaylistBootstrap playlistBootstrap = testContext.getApplicationContext().getBean(PlaylistBootstrap.class);
        playlistBootstrap.clean();
        final UserBootstrap userBootstrap = testContext.getApplicationContext().getBean(UserBootstrap.class);
        userBootstrap.clean();
    }
}
