package top.liuxunzhuo.books.Dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import top.liuxunzhuo.books.Entity.BookParseLog;
import top.liuxunzhuo.books.Entity.BookParseLogExample;

public interface BookParseLogMapper {
    int countByExample(BookParseLogExample example);

    int deleteByExample(BookParseLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BookParseLog record);

    int insertSelective(BookParseLog record);

    List<BookParseLog> selectByExample(BookParseLogExample example);

    BookParseLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BookParseLog record, @Param("example") BookParseLogExample example);

    int updateByExample(@Param("record") BookParseLog record, @Param("example") BookParseLogExample example);

    int updateByPrimaryKeySelective(BookParseLog record);

    int updateByPrimaryKey(BookParseLog record);
}