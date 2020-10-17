# notebook
项目介绍：
一个笔记app  
1.可以通过手机验证码进行注册，然后登录，登录后可以新建笔记，查看笔记列表，修改笔记和修改密码。还可以进行新闻查看。  
2.手机验证码教程（转载），引用的外部SDK：https://www.mob.com/wiki/detailed/?wiki=SMSSDK_for_Android_kuaisujicheng&id=23  
3.数据库存储：使用SQLite,是一款轻型的数据库，是遵守ACID的关系型数据库管理系统，它包含在一个相对小的C库中。  
4.因为是本地化，没有使用服务器，所以登录信息及登录状态是使用SharedPreferences来进行传递，使笔记列表与设置界面与登录用户相符  
5.主要的4个界面是使用Fragment和ViewPager完成的，笔记列表和新闻列表使用了ListView  
6.新闻是自己上传到Tomcat里的，请求网络使用的是okHttp技术,图片的显示使用了Glide  
7.实现图片：  
![Image text](https://github.com/Hello-Yuan/img-storage/blob/main/img/login.png)
![Image text](https://github.com/Hello-Yuan/img-storage/blob/main/img/register.png)
![Image text](https://github.com/Hello-Yuan/img-storage/blob/main/img/page_1.png)
![Image text](https://github.com/Hello-Yuan/img-storage/blob/main/img/page_2.png)
![Image text](https://github.com/Hello-Yuan/img-storage/blob/main/img/page_3.png)
![Image text](https://github.com/Hello-Yuan/img-storage/blob/main/img/page_4.png)

