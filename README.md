# SMMS-V2.0
springboot+uploadifive+qiniuyun 写的一个图床网站，页面布局仿照SM.MS...

# V2.0之改变
- 界面小调整，新增上传文件数量提示，仍然仿照sm.ms
- 修复uploadifive可以直接拖动非图片文件、选择框恶意选择非图片文件的错误，同时后台判断文件后缀
- 配置文件和项目代码分离，修改配置文件后直接重启应用即可生效，无需重新打包然后部署到服务器上
- 新增登录、注册功能，登录使用shiro进行身份认证、注册使用sendcloud发送邮件
- 全局强制https访问，http将自动跳转到https，用的是letsencrypt的免费ssl证书转换成tomcat的jks证书
- 数据库支持，mybatis+pagehelper分页实现...数据库功能等待更多开发，不过我应该不会再去搞这个了...

# 预览
https://imgbox.studio
