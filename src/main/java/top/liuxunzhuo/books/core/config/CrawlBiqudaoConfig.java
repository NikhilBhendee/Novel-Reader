package top.liuxunzhuo.books.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.liuxunzhuo.books.core.crawl.BaseHtmlCrawlSource;
import top.liuxunzhuo.books.core.crawl.BiquCrawlSource;


@Slf4j
@Configuration
public class CrawlBiqudaoConfig {


    @Bean
    @ConfigurationProperties(prefix = "biqudao.crawlsource") // prefix值必须是application.yml中对应属性的前缀
    @ConditionalOnProperty(prefix = "crawl.website",name = "type",havingValue = "1")
    public BaseHtmlCrawlSource biqudaoCrawlSource() {
        return new BiquCrawlSource();
    }


}
