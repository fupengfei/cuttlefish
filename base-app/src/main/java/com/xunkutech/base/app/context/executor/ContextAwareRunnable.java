package com.xunkutech.base.app.context.executor;

import com.xunkutech.base.app.context.AppContext;
import com.xunkutech.base.app.context.AppContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class ContextAwareRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ContextAwareRunnable.class);

    private Runnable task;
    private RequestAttributes requestContext;
    private LocaleContext localeContext;
    private AppContext appContext;

    public ContextAwareRunnable(Runnable task, RequestAttributes requestContext, LocaleContext localeContext, AppContext appContext) {
        this.task = task;
        this.requestContext = requestContext;
        this.localeContext = localeContext;
        this.appContext = appContext;
    }

    @Override
    public void run() {
        logger.debug("Setting RequestContextHolder, LocaleContextHolder and AppContextHolder after borrow thread from pool");
        if (requestContext != null) {
            RequestContextHolder.setRequestAttributes(requestContext);
        }

        if (localeContext != null) {
            LocaleContextHolder.setLocaleContext(localeContext);
        }

        if (appContext != null) {
            AppContextHolder.setAppContext(appContext);
        }

        try {
            task.run();
        } finally {
            logger.debug("Reset RequestContextHolder, LocaleContextHolder and AppContextHolder before return thread to pool");
            RequestContextHolder.resetRequestAttributes();
            LocaleContextHolder.resetLocaleContext();
            AppContextHolder.resetAppContext();
        }
    }
}
