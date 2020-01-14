package top.liuxunzhuo.books.Dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import top.liuxunzhuo.books.Entity.BookUpdateTimeLog;
import top.liuxunzhuo.books.Entity.BookUpdateTimeLogExample;

public interface BookUpdateTimeLogMapper {
    int countByExample(BookUpdateTimeLogExample example);

    int deleteByExample(BookUpdateTimeLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BookUpdateTimeLog record);

    int insertSelective(BookUpdateTimeLog record);

    List<BookUpdateTimeLog> selectByExample(BookUpdateTimeLogExample example);

    BookUpdateTimeLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BookUpdateTimeLog record, @Param("example") BookUpdateTimeLogExample example);

    int updateByExample(@Param("record") BookUpdateTimeLog record, @Param("example") BookUpdateTimeLogExample example);

    int updateByPrimaryKeySelective(BookUpdateTimeLog record);

    int updateByPrimaryKey(BookUpdateTimeLog record);
}