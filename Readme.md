#[H2O](http://h2object.io) signin

Package signin is a brief RESTful API design for signin using weibo,qq or wechat account.
signin抽象了第三方认证登录的开发过程，整个认证过程对开发者和用户完全透明，使用本包，可以让您的认证登录更简单。

#特点
1. 低耦合。不依赖第三方包，完全使用golang标准库开发。
2. 易扩展。定义了统一接口，扩展性强。

#signin 功能说明
调用该接口，返回登录账号的基本信息。

**注意事项：**  用户第一次访问该接口，访问页面会跳转至第三方授权页面，需第三方账号（weibo，qq，微信）授权登录。

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
1.  error_code				错误代码，详见[错误代码说明](http://h2object.io)。
2. msg			备注消息。

####认证成功
认证成功后，返回用户第三方账号的基本信息。
* 正确返回结果示例

       ` {
	        "error_code":0,
	        "msg":"",
	        "nickname":"Peter",
	        "gender":"男"
        }`

####认证失败
认证失败，返回错误代码以及备注消息。
* 错误返回结果

       ` {
	        "error_code":1,
	        "msg":"auth failed."
        }`


        
##[About us](http://h2object.io/about.md)
有任何疑问，请联系[我们](http://h2object.io/about.md)。
##License
MIT