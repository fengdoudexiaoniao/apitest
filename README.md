# apitest
这是一个使用springboot2.0搭建起来的接口测试框架，开发语言为java,测试数据的来源可以来源于数据库或者excel文件，并且将测试数据展示到前端页面。

一.开发工具

IntelliJIDEA
java版本
jdk 1.8.0_171

二.如何调试项目通过

1.将apiTest.sql文件导入MySQL数据库中
2.application.properties中，数据库相关配置信息改成你本地的数据库信息
3.如果使用excel作为测试数据的来源，请直接运行testng.xml文件，选中test.xml文件->右键单击->run
4.如果使用数据库作为测试数据的原来，请直接运营db_testng.xml文件，db_testng.xml文件->右键单击->run
5.运行成功后，可查看测试结果报告，在test-output文件夹下

三.项目讲解
1.api-data.xml中,run:输入y/n决定该条测试用例是否执行。desc:测试用例的描述。url:接口路径。param:参数（json格式）
verify:需要验证的返回参数（比如：{"screen_name":"$.screen_name"}，“$.screen_name”是从之前接口返回数据中取的值也可以是{"screen_name":"张三"}）
save:将接口中返回的数据保存下来，提供后续接口使用，多个参数使用;隔开。

