[H2O](http://h2object.io) oauth2
================

Package oauth2 is a brief RESTful API design for signin using weibo,qq or wechat account.

oauth2抽象了第三方认证登录的开发过程，整个认证过程对开发者和用户完全透明，使用本包，可以让您的认证登录更简单。

#特点
1. 低耦合。不依赖第三方包，完全使用golang标准库开发。
2. 易扩展。定义了统一接口，扩展性强。

#signin 功能说明
用户调用该接口，需使用第三方账号（weibo，qq，微信）授权登录，接口返回登录账号的基本信息。

*  **注意事项：** 需要登录账号的授权，本授权仅获取用户的基本信息，不用于其它用途。授权成功后，可使用该账号使用我们的服务。

## 接口调用说明
###URL格式
http://host:port/auth/:provider/signin
###方法
GET
###格式
json
###请求示例
####微博
`http://h2object.io:80/auth/weibo/signin`
####QQ
`http://h2object.io:80/auth/weibo/signin`
####微信
`http://h2object.io:80/auth/wechat/signin`

###返回参数说明
1.  error_code			错误代码，详见[错误代码说明](http://h2object.io)。
2. msg		备注消息。

####认证成功
认证成功后，返回用户第三方账号的基本信息。
* 正确返回结果示例

       ` {
        "error_code":0,

        "msg":"",

        "nickname":"Peter",

        "figureurl":"http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/30",

        "figureurl_1":"http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/50",
        "figureurl_2":"http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/100",
        "figureurl_qq_1":"http://q.qlogo.cn/qqapp/100312990/DE1931D5330620DBD07FB4A5422917B6/40",
        "figureurl_qq_2":"http://q.qlogo.cn/qqapp/100312990/DE1931D5330620DBD07FB4A5422917B6/100",
        "gender":"男",
        "is_yellow_vip":"1",
        "vip":"1",
        "yellow_vip_level":"7",
        "level":"7",
        "is_yellow_year_vip":"1"
        }`

####认证失败
认证失败，返回错误代码以及备注消息。
* 错误返回结果

       ` {
        "error_code":1,


        
        "msg":"auth failed."
        }`

##License
MIT