# apitest
这是一个使用springboot2.0搭建起来的接口测试框架，开发语言为java,测试数据的来源可以来源于数据库或者excel文件，并且将测试数据展示到前端页面。

开发工具
IntelliJIDEA
java版本
jdk 1.8.0_171

如何调试项目通过
1.将apiTest.sql文件导入MySQL数据库中
2.application.properties中，数据库相关配置信息改成你本地的数据库信息
3.如果使用excel作为测试数据的来源，请直接运行testng.xml文件，选中test.xml文件->右键单击->run
4.如果使用数据库作为测试数据的原来，请直接运营db_testng.xml文件，db_testng.xml文件->右键单击->run
5.运行成功后，可查看测试结果报告，在test-output文件夹下


