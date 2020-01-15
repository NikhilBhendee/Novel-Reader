package top.liuxunzhuo.books.Controller;


import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.liuxunzhuo.books.core.config.SeoConfig;
import top.liuxunzhuo.books.core.utils.CatUtil;
import top.liuxunzhuo.books.Entity.BookContent;
import top.liuxunzhuo.books.Entity.ScreenBullet;
import top.liuxunzhuo.books.Entity.Book;
import top.liuxunzhuo.books.Entity.BookIndex;
import top.liuxunzhuo.books.service.BookService;
import top.liuxunzhuo.books.service.UserService;
import top.liuxunzhuo.books.vo.BookVO;
import top.liuxunzhuo.common.cache.CommonCacheUtil;
import top.liuxunzhuo.books.core.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 小说Controller
 *
 * @author LiuXunzhuo
 */
@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {


    private final BookService bookService;

    private final UserService userService;

    private final CommonCacheUtil commonCacheUtil;



    /**
     * 精品小说搜索页
     */
    @RequestMapping("/search")
    public String search(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "20") int pageSize,
                         @RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "catId", required = false) Integer catId,
                         @RequestParam(value = "historyBookIds", required = false) String ids,
                         @RequestParam(value = "bookStatus", required = false) String bookStatus,
                         @RequestParam(value = "token", required = false) String token,
                         @RequestParam(value = "sortBy", defaultValue = "update_time") String sortBy,
                         @RequestParam(value = "sort", defaultValue = "DESC") String sort, ModelMap modelMap) {

        String userId = null;
        String titleType = "Update";
        if (catId != null) {
            titleType = CatUtil.getCatNameById(catId);
        } else if (Constants.NOVEL_TOP_FIELD.equals(sortBy)) {
            titleType = "Rank";
        } else if (ids != null) {
            titleType = "Reading History";
        } else if (token != null) {
            userId = commonCacheUtil.get(token);
            titleType = "BookShelf";
        } else if (bookStatus != null && bookStatus.contains(Constants.NOVEL_END_TAG)) {
            titleType = "Finished Book";
        } else if (keyword != null) {
            titleType = "Search";
        }
        modelMap.put("titleType", titleType);
        List<Book> books;
        List<BookVO> bookVoList;
        if (StringUtils.isEmpty(ids) || !StringUtils.isEmpty(keyword)) {
            books = bookService.search(page, pageSize, userId, ids, keyword, bookStatus, catId, null, null, sortBy, sort);
            bookVoList = new ArrayList<>();
            for (Book book : books) {
                BookVO bookvo = new BookVO();
                BeanUtils.copyProperties(book, bookvo);
                bookvo.setCateName(CatUtil.getCatNameById(bookvo.getCatid()));
                bookVoList.add(bookvo);
            }

        } else {
            if (!ids.contains(Constants.BOOK_ID_SEPARATOR)) {
                books = bookService.search(page, 50, null, ids, keyword, null, catId, null, null, sortBy, sort);
                List<String> idsArr = Arrays.asList(ids.split(","));
                BookVO[] bookVoArr = new BookVO[books.size()];
                for (Book book : books) {
                    int index = idsArr.indexOf(book.getId() + "");
                    BookVO bookvo = new BookVO();
                    BeanUtils.copyProperties(book, bookvo);
                    bookvo.setCateName(CatUtil.getCatNameById(bookvo.getCatid()));
                    bookVoArr[books.size() - index - 1] = bookvo;
                }
                bookVoList = Arrays.asList(bookVoArr);
            } else {
                books = new ArrayList<>();
                bookVoList = new ArrayList<>();
            }

        }

        PageInfo<Book> bookPageInfo = new PageInfo<>(books);
        modelMap.put("limit", bookPageInfo.getPageSize());
        modelMap.put("curr", bookPageInfo.getPageNum());
        modelMap.put("total", bookPageInfo.getTotal());
        modelMap.put("books", bookVoList);
        modelMap.put("ids", ids);
        modelMap.put("token", token);
        modelMap.put("bookStatus", bookStatus);
        modelMap.put("keyword", keyword);
        modelMap.put("catId", catId);
        modelMap.put("sortBy", sortBy);
        modelMap.put("sort", sort);
        return "books/book_search";
    }


    /**
     * 书籍详情页
     */
    @RequestMapping("/{bookId}.html")
    public String detail(@PathVariable("bookId") Long bookId, @RequestParam(value = "token", required = false) String token, HttpServletRequest req, ModelMap modelMap) {
        String userId = commonCacheUtil.get(token);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userId)) {
            Integer indexNumber = userService.queryBookIndexNumber(userId, bookId);
            if (indexNumber != null) {
                return "redirect:/book/" + bookId + "/" + indexNumber + ".html";
            }

        }

        //查询基本信息
        Book book = bookService.queryBaseInfo(bookId);
        //查询最新目录信息
        List<BookIndex> indexList = bookService.queryNewIndexList(bookId);

        int minIndexNum = 0;
        //查询最小目录号
        List<Integer> integers = bookService.queryMaxAndMinIndexNum(bookId);
        if (integers.size() > 1) {
            minIndexNum = integers.get(1);
        }


        BookVO bookvo = new BookVO();
        BeanUtils.copyProperties(book, bookvo);
        bookvo.setCateName(CatUtil.getCatNameById(bookvo.getCatid()));

        modelMap.put("bookId", bookId);
        modelMap.put("book", bookvo);
        modelMap.put("minIndexNum", minIndexNum);
        modelMap.put("indexList", indexList);
        if (indexList != null && indexList.size() > 0) {
            modelMap.put("lastIndexName", indexList.get(0).getIndexName());
            modelMap.put("lastIndexNum", indexList.get(0).getIndexNum());
        }
        SeoConfig seoConfig = (SeoConfig) req.getServletContext().getAttribute(Constants.SEO_CONFIG_KEY);
        Map<String,String> page = seoConfig.getPage();
        modelMap.put("title",page.get("detail.title").replaceAll("<bookName>",book.getBookName()));
        modelMap.put("keyword",page.get("detail.keyword").replaceAll("<bookName>",book.getBookName()));
        modelMap.put("description",page.get("detail.description").replaceAll("<bookName>",book.getBookName()));

        return "books/book_detail";
    }

    /**
     * 书籍目录页
     */
    @RequestMapping("/{bookId}/index.html")
    public String bookIndex(@PathVariable("bookId") Long bookId,HttpServletRequest req, ModelMap modelMap) {
        List<BookIndex> indexList = bookService.queryAllIndexList(bookId);
        String bookName = bookService.queryBaseInfo(bookId).getBookName();
        modelMap.put("indexList", indexList);
        modelMap.put("bookName", bookName);
        modelMap.put("bookId", bookId);
        SeoConfig seoConfig = (SeoConfig) req.getServletContext().getAttribute(Constants.SEO_CONFIG_KEY);
        Map<String,String> page = seoConfig.getPage();
        modelMap.put("title",page.get("catalog.title").replaceAll("<bookName>",bookName));
        modelMap.put("keyword",page.get("catalog.keyword").replaceAll("<bookName>",bookName));
        modelMap.put("description",page.get("catalog.description").replaceAll("<bookName>",bookName));
        return "books/book_index";
    }


    /**
     * 书籍内容页
     */
    @RequestMapping("/{bookId}/{indexNum}.html")
    public String bookContent(@PathVariable("bookId") Long bookId, @PathVariable("indexNum") Integer indexNum,HttpServletRequest req, ModelMap modelMap) {
        BookContent bookContent = bookService.queryBookContent(bookId, indexNum);
        String indexName;
        if (bookContent == null) {
            bookContent = new BookContent();
            bookContent.setId(-1L);
            bookContent.setBookId(bookId);
            bookContent.setIndexNum(indexNum);
            bookContent.setContent(Constants.NO_CONTENT_DESC);
            indexName = "Updating。。。";
        } else {
            indexName = bookService.queryIndexNameByBookIdAndIndexNum(bookId, indexNum);
        }
        List<Integer> preAndNextIndexNum = bookService.queryPreAndNextIndexNum(bookId, indexNum);
        modelMap.put("nextIndexNum", preAndNextIndexNum.get(0));
        modelMap.put("preIndexNum", preAndNextIndexNum.get(1));
        bookContent.setContent(bookContent.getContent().replaceAll(Constants.CONTENT_AD_PATTERN, ""));
        modelMap.put("bookContent", bookContent);
        modelMap.put("indexName", indexName);
        Book basicBook = bookService.queryBaseInfo(bookId);
        if (basicBook.getCatid() <= Constants.MAX_NOVEL_CAT) {
            bookContent.setContent(StringEscapeUtils.unescapeHtml4(bookContent.getContent()));
        }
        String bookName = basicBook.getBookName();
        Integer catId = basicBook.getCatid();
        modelMap.put("bookName", bookName);
        modelMap.put("catId", catId);
        SeoConfig seoConfig = (SeoConfig) req.getServletContext().getAttribute(Constants.SEO_CONFIG_KEY);
        Map<String,String> page = seoConfig.getPage();
        modelMap.put("title",page.get("content.title").replaceAll("<bookName>",bookName).replaceAll("<indexName>",indexName));
        modelMap.put("keyword",page.get("content.keyword").replaceAll("<bookName>",bookName).replaceAll("<indexName>",indexName));
        modelMap.put("description",page.get("content.description").replaceAll("<bookName>",bookName).replaceAll("<indexName>",indexName));
        return "books/book_content";
    }


    /**
     * 增加访问次数
     */
    @RequestMapping("/addVisit")
    @ResponseBody
    public String addVisit(@RequestParam("bookId") Long bookId, @RequestParam(value = "indexNum", defaultValue = "0") Integer indexNum, @RequestParam(value = "token", defaultValue = "") String token) {
        String userId = commonCacheUtil.get(token);

        bookService.addVisitCount(bookId, userId, indexNum);

        return "ok";
    }

    /**
     * 发送弹幕
     */
    @RequestMapping("/sendBullet")
    @ResponseBody
    public Map<String, Object> sendBullet(@RequestParam("contentId") Long contentId, @RequestParam("bullet") String bullet) {
        Map<String, Object> result = new HashMap<>(2);
        bookService.sendBullet(contentId, bullet);
        result.put("code", 1);
        result.put("desc", "ok");
        return result;
    }

    /**
     * 查询是否正在下载
     */
    @RequestMapping("/queryIsDownloading")
    @ResponseBody
    public Map<String, Object> queryIsDownloading(HttpSession session) {
        Map<String, Object> result = new HashMap<>(1);
        if (session.getAttribute(Constants.NOVEL_IS_DOWNLOADING_KEY) != null) {
            result.put("code", 1);
        } else {
            result.put("code", 0);
        }
        return result;
    }


    /**
     * 查询弹幕
     */
    @RequestMapping("/queryBullet")
    @ResponseBody
    public List<ScreenBullet> queryBullet(@RequestParam("contentId") Long contentId) {

        return bookService.queryBullet(contentId);
    }


    /**
     * 文件下载
     */
    @RequestMapping(value = "/download")
    public void download(@RequestParam("bookId") Long bookId, @RequestParam("bookName") String bookName, HttpServletResponse resp, HttpSession session) {
        try {
            session.setAttribute(Constants.NOVEL_IS_DOWNLOADING_KEY, 1);
            int count = bookService.countIndex(bookId);


            OutputStream out = resp.getOutputStream();
            //设置响应头，对文件进行url编码
            bookName = URLEncoder.encode(bookName, "UTF-8");
            //解决手机端不能下载附件的问题
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment;filename=" + bookName + ".txt");
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    String index = bookService.queryIndexNameByBookIdAndIndexNum(bookId, i);
                    if (index != null) {
                        String content = bookService.queryContentList(bookId, i);
                        out.write(index.getBytes(StandardCharsets.UTF_8));
                        out.write("\n".getBytes(StandardCharsets.UTF_8));
                        content = content.replaceAll(Constants.CONTENT_AD_PATTERN, "")

                                .replaceAll("<br\\s*/*>", "\r\n")
                                .replaceAll("&nbsp;", " ")
                                .replaceAll("<a[^>]*>", "")
                                .replaceAll("</a>", "")
                                .replaceAll("<div[^>]*>", "")
                                .replaceAll("</div>", "")
                                .replaceAll("<p[^>]*>[^<]*<a[^>]*>[^<]*</a>\\s*</p>", "")
                                .replaceAll("<p[^>]*>", "")
                                .replaceAll("</p>", "\r\n");
                        out.write(content.getBytes(StandardCharsets.UTF_8));
                        out.write("\r\n".getBytes(StandardCharsets.UTF_8));
                        out.write("\r\n".getBytes(StandardCharsets.UTF_8));
                        out.flush();
                    }
                }

            }

            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.removeAttribute(Constants.NOVEL_IS_DOWNLOADING_KEY);
        }

    }



}