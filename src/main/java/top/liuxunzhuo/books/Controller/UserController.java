package top.liuxunzhuo.books.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.liuxunzhuo.books.Entity.User;
import top.liuxunzhuo.books.common.cache.CommonCacheUtil;
import top.liuxunzhuo.books.service.UserService;
import top.liuxunzhuo.books.common.utils.UUIDUtils;

import java.util.*;

/**
 * @author 刘训灼
 */
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    private final CommonCacheUtil commonCacheUtil;


    /**
     * 登陆页面
     * */
    @RequestMapping("/login.html")
    public String login(Long bookId, ModelMap modelMap) {
        modelMap.put("bookId", bookId);
        return "user/login";
    }


    /**
     * 登陆或注册
     * */
    @RequestMapping("/loginOrRegist")
    @ResponseBody
    public Map<String, Object> loginOrRegist(User user, Long bookId) {
        Map<String, Object> result = new HashMap<>(2);
        //查询用户名是否存在
        boolean isExistLoginName = userService.isExistLoginName(user.getLoginName());
        String token = null;
        if (isExistLoginName) {
            //登录
            userService.login(user);
            if (user.getId() != null) {
                token = UUIDUtils.getUUID32();
                commonCacheUtil.set(token, user.getId() + "");
                result.put("code", 1);
                result.put("desc", "Login Success！");
                if(!StringUtils.isEmpty(bookId)) {
                    userService.collectOrCancelBook(user.getId(), bookId);
                }
            } else {
                result.put("code", -1);
                result.put("desc", "Username or Password Wrong！");
            }
        } else {
            //注册
            userService.regist(user);
            Long userId = user.getId();
            token = UUIDUtils.getUUID32();
            commonCacheUtil.set(token, userId + "");
            result.put("code", 2);
            result.put("desc", "Registered SuccessFully！");
            if(!StringUtils.isEmpty(bookId)) {
                userService.collectOrCancelBook(user.getId(), bookId);
            }
        }
        if(token != null){
            result.put("token",token);
        }
        return result;
    }

    /**
     * 登陆状态查询
     * */
    @RequestMapping("/isLogin")
    @ResponseBody
    public Map<String, Object> isLogin(String token) {
        Map<String, Object> result = new HashMap<>(2);
        String userId = commonCacheUtil.get(token);
        if(StringUtils.isEmpty(userId)){
            result.put("code", -1);
            result.put("desc", "Unlogin！");
        }else{
            result.put("code", 1);
            result.put("desc", "Login！");
        }
        return result;
    }


    /**
     * 加入书架
     * */
    @RequestMapping("/addToCollect")
    @ResponseBody
    public Map<String, Object> addToCollect(Long bookId,String token) {
        Map<String, Object> result = new HashMap<>();
        String userId = commonCacheUtil.get(token);
        if(StringUtils.isEmpty(userId)){
            result.put("code", -1);
            result.put("desc", "Unlogin！");
        }else {
            userService.addToCollect(bookId,Long.parseLong(userId));
            result.put("code", 1);
            result.put("desc", "Added SuccessFully,Please Check in Your BookShelf！");
        }
        return result;
    }

    /**
     * 撤下书架
     * */
    @RequestMapping("/cancelToCollect")
    @ResponseBody
    public Map<String, Object> cancelToCollect(Long bookId,String token) {
        Map<String, Object> result = new HashMap<>(2);
        String userId = commonCacheUtil.get(token);
        if(StringUtils.isEmpty(userId)){
            result.put("code", -1);
            result.put("desc", "Unlogin！");
        }else {
            userService.cancelToCollect(bookId,Long.parseLong(userId));
            result.put("code", 1);
            result.put("desc", "Remove SuccessFully！");
        }
        return result;
    }

    /**
     * 判断是否加入书架
     * */
    @RequestMapping("/isCollect")
    @ResponseBody
    public Map<String, Object> isCollect(Long bookId,String token) {
        Map<String, Object> result = new HashMap<>(2);
        String userId = commonCacheUtil.get(token);
        if(StringUtils.isEmpty(userId)){
            result.put("code", -1);
            result.put("desc", "Unlogin！");
        }else {
            boolean isCollect = userService.isCollect(bookId,Long.parseLong(userId));
            if(isCollect) {
                result.put("code", 1);
                result.put("desc", "Added SuccessFully！");
            }else{
                result.put("code", 2);
                result.put("desc", "UnSaved！");
            }
        }
        return result;
    }




}
