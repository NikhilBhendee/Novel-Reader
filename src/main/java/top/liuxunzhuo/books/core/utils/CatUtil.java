package top.liuxunzhuo.books.core.utils;

/**
 * @author 11797
 */
public class CatUtil {

    public static int getCatNum(String catName) {
        int catNum;
        switch (catName) {
            case "Life": {
                catNum = 2;
                break;
            }
            case "City": {
                catNum = 3;
                break;
            }
            case "History": {
                catNum = 4;
                break;
            }
            case "Scary": {
                catNum = 5;
                break;
            }
            case "Game": {
                catNum = 6;
                break;
            }
            case "Soft": {
                catNum = 7;
                break;
            }
            default: {
                catNum = 1;
                break;
            }
        }
        return catNum;
    }




    /**
     * 获取分类名
     * */
    public static String getCatNameById(Integer catid) {
        String catName = "Other";

        switch (catid) {
            case 1: {
                catName = "Fantasy";
                break;
            }
            case 2: {
                catName = "Life";
                break;
            }
            case 3: {
                catName = "City";
                break;
            }
            case 4: {
                catName = "History";
                break;
            }
            case 5: {
                catName = "Scary";
                break;
            }
            case 6: {
                catName = "Game";
                break;
            }
            case 7: {
                catName = "Soft";
                break;
            }
            case 8: {
                catName = "Adventure";
                break;
            }
            case 9: {
                catName = "Funny";
                break;
            }
            default: {
                break;
            }


        }
        return catName;
    }
}
