package top.liuxunzhuo.books.common.crawl;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;


@Data
public abstract class BaseCrawlSource {

    @Value("${books.lowestScore}")
    private Float lowestScore;

    /**
     * 解析数据
     * */
    public abstract void parse();


    /**
     * 更新书籍
     * */
    public abstract void update();
}
