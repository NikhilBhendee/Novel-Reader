package top.liuxunzhuo.books.Dao;

import org.apache.ibatis.annotations.Param;
import top.liuxunzhuo.books.Entity.BookIndexExample;
import top.liuxunzhuo.books.Entity.BookIndex;

import java.util.List;

public interface BookIndexMapper {
    int countByExample(BookIndexExample example);

    int deleteByExample(BookIndexExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BookIndex record);

    int insertSelective(BookIndex record);

    List<BookIndex> selectByExample(BookIndexExample example);

    BookIndex selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BookIndex record, @Param("example") BookIndexExample example);

    int updateByExample(@Param("record") BookIndex record, @Param("example") BookIndexExample example);

    int updateByPrimaryKeySelective(BookIndex record);

    int updateByPrimaryKey(BookIndex record);

    void insertBatch(List<BookIndex> bookIndex);



    /**
     * 清除无效章节
     * */
    void clearInvilidIndex();
}