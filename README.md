# NovelDao

基于Springboot开发的英文小说网，作为学习Springboot的实战的项目

## 技术栈：

### SpringBoot+Mybatis+Thymeleaf+Ehcache+Layui

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

   1. 安装MySQL软件。
   2. 修改MySQL`max_allowed_packet `配置（建议100M）。
   3. 新建数据库
       ``` sql 
       books:create database books default character set utf8mb4 collate utf8mb4_general_ci 。
   4. 执行public文件夹中的/sql/books.sql文件。建立数据表

2. application.yml 中：

   + 修改配置，使用自己的数据源

   

3. 打成jar包 部署到服务器上即可使用



## 成果展示：

+ 首页：
  ![image](https://github.com/Xunzhuo/NovelDao/raw/master/src/main/resources/public/index.png)

+ 主页面：
  ![image](https://github.com/Xunzhuo/NovelDao/raw/master/src/main/resources/public/main.png)
  ![image](https://github.com/Xunzhuo/NovelDao/raw/master/src/main/resources/public/main1.png)

+ 书籍详情页：
  ![image](https://github.com/Xunzhuo/NovelDao/raw/master/src/main/resources/public/detail.png)

+ 书籍目录页：
  ![image](https://github.com/Xunzhuo/NovelDao/raw/master/src/main/resources/public/content.png)

+ 书籍阅读页：
  ![image](https://github.com/Xunzhuo/NovelDao/raw/master/src/main/resources/public/read.png)

+ 登陆注册页：
  ![image](https://github.com/Xunzhuo/NovelDao/raw/master/src/main/resources/public/login.png)

+ 阅读历史页：
  ![image](https://github.com/Xunzhuo/NovelDao/raw/master/src/main/resources/public/history.png)

+ 个人书架页:
  ![image](https://github.com/Xunzhuo/NovelDao/raw/master/src/main/resources/public/shelf.png)

### 后期规划：

1. 完成对应的一个后台管理的springboot项目，可以持续跟进
2. 对接口实现全RestFul化
3. 可能会对页面进一步美化
4. 持续维护升级
5. 围绕Springboot：
   + 发布更多的开源实战项目，初步规划
     1. 基于springboot+vue实现的前后端分离的宠物论坛开发
     2. 基于springboot+vue+nginx+redis+rabbitmq+ES 实现的Java技术论坛开发
     3. ....
   + 结合各种中间件发布对应的实战项目：
     1. 消息队列 RabbitMQ
     2. 负载均衡 Nginx
     3. 缓存 Redis
     4. RPC通讯框架: Zoomkeeper Duboo
     5. 微服务
     6. 全文搜索引擎：ElasticSearch
6. 围绕Python爬虫：
   + 发布自己写的爬虫框架
   + 网络爬虫实战

### 展示网站:

##  www.noveldao.com



## 欢迎大家交流～ 可以加我微信交流

+ 本人菜鸟一名，不断和大家一起学习进步中！加油

![wechat](https://github.com/Xunzhuo/NovelDao/raw/master/src/main/resources/public/wechat.jpg)
