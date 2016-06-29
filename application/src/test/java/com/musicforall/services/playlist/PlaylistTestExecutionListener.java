package com.musicforall.services.playlist;

import com.musicforall.services.user.UserBootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Created by kgavrylchenko on 29.06.16.
 */
public class PlaylistTestExecutionListener extends AbstractTestExecutionListener {
    @Autowired
    PlaylistBootstrap playlistBootstrap;

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        super.afterTestClass(testContext);
        PlaylistBootstrap playlistBootstrap = testContext.getApplicationContext().getBean(PlaylistBootstrap.class);
        playlistBootstrap.fillDatabase();
        UserBootstrap userBootstrap = testContext.getApplicationContext().getBean(UserBootstrap.class);
        userBootstrap.fillDatabase();
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        super.beforeTestClass(testContext);
        PlaylistBootstrap playlistBootstrap = testContext.getApplicationContext().getBean(PlaylistBootstrap.class);
        playlistBootstrap.clean();
        UserBootstrap userBootstrap = testContext.getApplicationContext().getBean(UserBootstrap.class);
        userBootstrap.clean();
    }
}
