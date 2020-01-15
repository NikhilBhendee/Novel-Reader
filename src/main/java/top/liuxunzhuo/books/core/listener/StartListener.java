package top.liuxunzhuo.books.core.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import top.liuxunzhuo.books.core.config.SeoConfig;
import top.liuxunzhuo.books.core.crawl.BaseCrawlSource;
import top.liuxunzhuo.books.core.utils.Constants;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
@Slf4j
@RequiredArgsConstructor
public class StartListener implements ServletContextListener {

    private final BaseCrawlSource crawlSource;

    @Value("${crawl.book.new.enabled}")
    private String crawlEnable;

    @Value("${website.page.index.title}")
    private String title;

    @Value("${website.domain}")
    private String webSiteDomain;

    @Value("${books.updatePeriod}")
    private float bookUpdatePeriod;

    private final SeoConfig seoConfig;



    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext application = servletContextEvent.getServletContext();

        application.setAttribute("webSiteDomain",webSiteDomain);
        application.setAttribute(Constants.SEO_CONFIG_KEY,seoConfig);
        int i = 1;
        if (Constants.ENABLE_NEW_BOOK.equals(crawlEnable.trim())&&i==0) {
            log.info("程序启动");
            new Thread(() -> {

                    try {

                        log.info("parseBooks执行中。。。。。。。。。。。。");
                        crawlSource.parse();
                        Thread.sleep(new Float(1000 * 60 * bookUpdatePeriod).longValue());
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }


            }).start();

            new Thread(() -> {

                    try {

                        log.info("updateBooks执行中。。。。。。。。。。。。");
                        crawlSource.update();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }


            }).start();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
