package top.liuxunzhuo.books.Controller;


import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.liuxunzhuo.books.core.utils.CatUtil;
import top.liuxunzhuo.books.Entity.BookContent;
import top.liuxunzhuo.books.Entity.Book;
import top.liuxunzhuo.books.Entity.BookIndex;
import top.liuxunzhuo.books.service.BookService;
import top.liuxunzhuo.books.vo.BookVO;
import top.liuxunzhuo.common.cache.CommonCacheUtil;
import top.liuxunzhuo.books.core.utils.Constants;

import java.util.*;

/**
 * api接口
 * @author XXY
 */
@RestController
@RequestMapping("api/book")
@RequiredArgsConstructor
public class ApiBookController {


    private final BookService bookService;

    private final CommonCacheUtil commonCacheUtil;


    /**
     * 首页热门书籍查询接口
     * */
    @RequestMapping("hotBook")
    public List<Book> hotBooks () {
        //查询热点数据
        List<Book> hotBooks = bookService.search(1, 6, null, null, null, null, null, null, null, "visit_count DESC,score ", "DESC");
        return hotBooks;
    }

    /**
     * 首页最新书籍查询接口
     * */
    @RequestMapping("newstBook")
    public List<Book> newstBook() {
        //查询最近更新数据

        return bookService.search(1, 6, null, null, null, null, null, null, null, "update_time", "DESC");
    }

    /**
     * 书籍搜索接口
     * */
    @RequestMapping("search")
    public Map<String,Object> search(@RequestParam(value = "curr", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "20") int pageSize,
                         @RequestParam(value = "keyword", required = false) String keyword,
                                     @RequestParam(value = "bookStatus", required = false) String bookStatus,
                                     @RequestParam(value = "catId", required = false) Integer catId,
                         @RequestParam(value = "historyBookIds", required = false) String ids,
                         @RequestParam(value = "token", required = false) String token,
                         @RequestParam(value = "sortBy", defaultValue = "update_time") String sortBy, @RequestParam(value = "sort", defaultValue = "DESC") String sort
                         ) {

        Map<String,Object> modelMap = new HashMap<>();
        String userId = null;
        String titleType = "Update";
        if (catId != null) {
            titleType = CatUtil.getCatNameById(catId);
        } else if (keyword != null) {
            titleType = "Search";
        } else if ("score".equals(sortBy)) {
            titleType = "Rank";
        } else if (ids != null) {
            titleType = "Reading History";
        } else if (token != null) {
            userId = commonCacheUtil.get(token);
            titleType = "My BookShelf";
        }
        modelMap.put("titleType", titleType);
        List<Book> books = bookService.search(page, pageSize, userId, ids, keyword,bookStatus, catId, null, null, sortBy, sort);
        List<BookVO> bookVOList;
        if (StringUtils.isEmpty(ids)) {
            bookVOList = new ArrayList<>();
            for (Book book : books) {
                BookVO bookvo = new BookVO();
                BeanUtils.copyProperties(book, bookvo);
                bookvo.setCateName(CatUtil.getCatNameById(bookvo.getCatid()));
                bookVOList.add(bookvo);
            }

        } else {
            if (!ids.contains("-")) {
                List<String> idsArr = Arrays.asList(ids.split(","));
                int length = idsArr.size();
                BookVO[] bookVOArr = new BookVO[length];
                for (Book book : books) {
                    int index = idsArr.indexOf(book.getId() + "");
                    BookVO bookvo = new BookVO();
                    BeanUtils.copyProperties(book, bookvo);
                    bookvo.setCateName(CatUtil.getCatNameById(bookvo.getCatid()));
                    bookVOArr[length - index - 1] = bookvo;
                }
                bookVOList = Arrays.asList(bookVOArr);
            } else {
                bookVOList = new ArrayList<>();
            }

        }

        PageInfo<Book> bookPageInfo = new PageInfo<>(books);
        modelMap.put("limit", bookPageInfo.getPageSize());
        modelMap.put("curr", bookPageInfo.getPageNum());
        modelMap.put("total", bookPageInfo.getTotal());
        modelMap.put("books", bookVOList);
        modelMap.put("ids", ids);
        modelMap.put("keyword", keyword);
        modelMap.put("catId", catId);
        modelMap.put("sortBy", sortBy);
        modelMap.put("sort", sort);
        return modelMap;
    }

    /**
     * 书籍详情信息查询接口
     * */
    @RequestMapping("{bookId}.html")
    public Map<String,Object> detail(@PathVariable("bookId") Long bookId) {
        Map<String,Object> modelMap = new HashMap<>();
        //查询基本信息
        Book book = bookService.queryBaseInfo(bookId);
        //查询最新目录信息
        List<BookIndex> indexList = bookService.queryNewIndexList(bookId);

        BookVO bookvo = new BookVO();
        BeanUtils.copyProperties(book, bookvo);
        bookvo.setCateName(CatUtil.getCatNameById(bookvo.getCatid()));
        modelMap.put("bookId", bookId);
        modelMap.put("book", bookvo);
        modelMap.put("indexList", indexList);
        return modelMap;
    }

    /**
     * 书籍目录查询接口
     * */
    @RequestMapping("{bookId}/index.html")
    public Map<String,Object> bookIndex(@PathVariable("bookId") Long bookId) {
        Map<String,Object> modelMap = new HashMap<>();
        List<BookIndex> indexList = bookService.queryAllIndexList(bookId);
        String bookName = bookService.queryBaseInfo(bookId).getBookName();
        modelMap.put("indexList", indexList);
        modelMap.put("bookName", bookName);
        modelMap.put("bookId", bookId);
        return modelMap;
    }

    /**
     * 书籍章节内容查询接口
     * */
    @RequestMapping("{bookId}/{indexNum}.html")
    public Map<String,Object> bookContent(@PathVariable("bookId") Long bookId, @PathVariable("indexNum") Integer indexNum) {
        Map<String,Object> modelMap = new HashMap<>();
        //查询最大章节号
        List<Integer> maxAndMinIndexNum = bookService.queryMaxAndMinIndexNum(bookId);
        if(maxAndMinIndexNum.size()>0) {
            int maxIndexNum = maxAndMinIndexNum.get(0);
            int minIndexNum = maxAndMinIndexNum.get(1);
            if (indexNum < minIndexNum) {
                indexNum = maxIndexNum;
            }
            if (indexNum > maxIndexNum) {
                indexNum = minIndexNum;
            }
        }
        BookContent bookContent = bookService.queryBookContent(bookId, indexNum);
        String indexName;
        if(bookContent==null) {
            bookContent = new BookContent();
            bookContent.setId(-1L);
            bookContent.setBookId(bookId);
            bookContent.setIndexNum(indexNum);
            bookContent.setContent(Constants.NO_CONTENT_DESC);
            indexName="？";
        }else{
            indexName = bookService.queryIndexNameByBookIdAndIndexNum(bookId, indexNum);
        }
        modelMap.put("indexName", indexName);
        String bookName = bookService.queryBaseInfo(bookId).getBookName();
        modelMap.put("bookName", bookName);
		modelMap.put("bookContent", bookContent);
        return modelMap;
    }



}
