package top.liuxunzhuo.books.Controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.liuxunzhuo.books.core.config.IndexRecBooksConfig;
import top.liuxunzhuo.books.core.constant.CacheKeyConstans;
import top.liuxunzhuo.books.Entity.Book;
import top.liuxunzhuo.books.service.BookService;
import top.liuxunzhuo.common.cache.CommonCacheUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 首页controller
 * @author XXY
 */
@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    @Value("${index.template}")
    private String indexTemplate;


    private final BookService bookService;

    private final CommonCacheUtil commonCacheUtil;

    private final IndexRecBooksConfig indexRecBooksConfig;




    @RequestMapping(value = {"/"})
    public String index(@RequestParam(value = "noLazy", defaultValue = "0") String noLazy,HttpServletRequest req,ModelMap modelMap){
        List<Book> recBooks = (List<Book>) commonCacheUtil.getObject(CacheKeyConstans.REC_BOOK_LIST_KEY);
        if (!indexRecBooksConfig.isRead() || recBooks == null) {
            List<Map<String,String>> configMap = indexRecBooksConfig.getRecBooks();
            //查询推荐书籍数据
            recBooks = bookService.queryRecBooks(configMap);
            commonCacheUtil.setObject(CacheKeyConstans.REC_BOOK_LIST_KEY, recBooks, 60 * 60 * 24 * 10);
            indexRecBooksConfig.setRead(true);
        }


        List<Book> hotBooks = (List<Book>) commonCacheUtil.getObject(CacheKeyConstans.HOT_BOOK_LIST_KEY);
        if (hotBooks == null) {
            //查询热点数据
            hotBooks = bookService.search(1, 9, null, null, null, null, null, null, null, "visit_count DESC,score ", "DESC");
            commonCacheUtil.setObject(CacheKeyConstans.HOT_BOOK_LIST_KEY, hotBooks, 60 * 60 * 24);
        }

        List<Book> newBooks = (List<Book>) commonCacheUtil.getObject(CacheKeyConstans.NEWST_BOOK_LIST_KEY);
        if (newBooks == null) {
            //查询最近更新数据
            newBooks = bookService.search(1, 20, null, null, null, null, null, null, null, "update_time", "DESC");
            commonCacheUtil.setObject(CacheKeyConstans.NEWST_BOOK_LIST_KEY, newBooks, 60 * 10);
        }
        modelMap.put("recBooks", recBooks);
        modelMap.put("hotBooks", hotBooks);
        modelMap.put("newBooks", newBooks);
        ServletContext application = req.getServletContext();
        if(!"1".equals(application.getAttribute("noLazy"))) {
            application.setAttribute("noLazy", noLazy);
        }
        return "books/index_"+indexTemplate;
    }
}
