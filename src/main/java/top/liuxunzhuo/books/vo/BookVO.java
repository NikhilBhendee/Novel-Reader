package top.liuxunzhuo.books.vo;

import top.liuxunzhuo.books.Entity.Book;

public class BookVO extends Book {

    private String cateName;

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }
}
