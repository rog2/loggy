package com.kingsoft.shiyou.loggy;

import com.kingsoft.shiyou.loggy.dagger.DaggerLoggyComponent;
import com.kingsoft.shiyou.loggy.dagger.VertxModule;
import com.kingsoft.shiyou.loggy.db.LoggyLogsStore;
import com.kingsoft.shiyou.loggy.notify.client.NoticeClient;
import com.kingsoft.shiyou.loggy.service.LoggyService;
import com.kingsoft.shiyou.loggy.utils.vertx.AsyncUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import javax.inject.Inject;

/**
 * @author taoshuang on 2020/4/22.
 */
public class LoggyVerticle extends AbstractVerticle {

    @Inject
    LoggyLogsStore loggyLogsStore;

    @Inject
    NoticeClient noticeClient;

    @Inject
    LoggyService loggyService;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        injectDependencies();
        AsyncUtils.startSequentially(startPromise, loggyLogsStore, noticeClient, loggyService);
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        AsyncUtils.stopSequentially(stopPromise, loggyService, noticeClient, loggyLogsStore);
    }

    private void injectDependencies() {
        DaggerLoggyComponent.builder()
                .vertxModule(new VertxModule(context))
                .build()
                .inject(this);
    }
}
