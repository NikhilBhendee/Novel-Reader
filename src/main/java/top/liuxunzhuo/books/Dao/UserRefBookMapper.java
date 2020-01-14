package top.liuxunzhuo.books.Dao;

import org.apache.ibatis.annotations.Param;
import top.liuxunzhuo.books.Entity.UserRefBook;
import top.liuxunzhuo.books.Entity.UserRefBookExample;

import java.util.List;

public interface UserRefBookMapper {
    int countByExample(UserRefBookExample example);

    int deleteByExample(UserRefBookExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserRefBook record);

    int insertSelective(UserRefBook record);

    List<UserRefBook> selectByExample(UserRefBookExample example);

    UserRefBook selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserRefBook record, @Param("example") UserRefBookExample example);

    int updateByExample(@Param("record") UserRefBook record, @Param("example") UserRefBookExample example);

    int updateByPrimaryKeySelective(UserRefBook record);

    int updateByPrimaryKey(UserRefBook record);

    void updateNewstIndex(@Param("bookId") Long bookId,@Param("userId") String userId,@Param("indexNum") Integer indexNum);

    Integer queryBookIndexNumber(@Param("userId") String userId,@Param("bookId") Long bookId);
}