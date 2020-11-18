# Overview

基于Springboot的英文阅读网，作为学习Springboot的实战的项目

## 技术栈：

### `SpringBoot+Mybatis+Thymeleaf+Ehcache+Layui`

### 后端技术栈：

后端主要采用了：

1. 业务逻辑 Springboot
2. 持久化层 Mybatis
3. 模版引擎 Thymeleaf
4. 缓存 Ehcache
5. 日志框架 slf4j+logback



### 前端技术栈：

前端主要采用了：

1. 模块化框架：Layui
2. 前端三剑客



### 相关环境：

1. jdk 1.8
2. maven 3.6.3
3. springboot 2.0.1
4. MySQL 8.0.19



### 快速使用：

1. 数据库安装：

   1. 安装MySQL
   3. 新建数据库
       ``` sql 
       books:create database books default character set utf8mb4 collate utf8mb4_general_ci 。
   4. 执行sql文件夹中的 books.sql文件。建立数据表
   5. 为了测试，后续我将提供一个开放的阿里云数据库，可通过访问ip:3306端口 books数据库，直接获取测试数据
2. application.yml 中：

   + 修改配置，使用自己的数据源

   

3. 打成jar包 部署到服务器上即可使用



## 成果展示：

+ 首页：
  ![image](https://github.com/Xunzhuo/SpringBoot-in-Action/raw/master/src/main/resources/public/index.png)

+ 主页面：
  ![image](https://github.com/Xunzhuo/SpringBoot-in-Action/raw/master/src/main/resources/public/main.png)
  ![image](https://github.com/Xunzhuo/SpringBoot-in-Action/raw/master/src/main/resources/public/main1.png)

+ 书籍详情页：
  ![image](https://github.com/Xunzhuo/SpringBoot-in-Action/raw/master/src/main/resources/public/detail.png)

+ 书籍目录页：
  ![image](https://github.com/Xunzhuo/SpringBoot-in-Action/raw/master/src/main/resources/public/content.png)

+ 书籍阅读页：
  ![image](https://github.com/Xunzhuo/SpringBoot-in-Action/raw/master/src/main/resources/public/read.png)

+ 登陆注册页：
  ![image](https://github.com/Xunzhuo/SpringBoot-in-Action/raw/master/src/main/resources/public/login.png)

+ 阅读历史页：
  ![image](https://github.com/Xunzhuo/SpringBoot-in-Action/raw/master/src/main/resources/public/history.png)

+ 个人书架页:
  ![image](https://github.com/Xunzhuo/SpringBoot-in-Action/raw/master/src/main/resources/public/shelf.png)


