-------------
# go env

#1 安装go

 

#2 配置环境变量

(1). 新建 变量名：GOBIN 变量值 ：c:\go\bin

(3). 新建 变量名：GOOS 变量值：windows

(4). 新建 变量名： GOROOT 变量值：c:\go

(5). 编辑 Path 在Path的变量值的最后加上 %GOBIN%

(6).  新建 变量名：GOPATH 变量值：**

 

#3 下载gocode实现编码提示

在cmd中输入

　go get github.com/nsf/gocode

  go install github.com/nsf/gocode

#4 下载Sublime Text 3

import urllib.request,os; pf = 'Package Control.sublime-package'; ipp = sublime.installed_packages_path(); urllib.request.install_opener( urllib.request.build_opener( urllib.request.ProxyHandler()) ); open(os.path.join(ipp, pf), 'wb').write(urllib.request.urlopen( 'http://sublime.wbond.net/' + pf.replace(' ','%20')).read())

#5 这一步也很重要
(1) sublimetext 安装插件 GoSublime
(2) sublimetext 安装插件 GoDef
(3) Preference --> Package Setting --> GoSublime --> Setting Default
	
	"env": {
		"path":"G:\\Go\\bin"
		},

##	Github Configure

1.	SSH KEY GEN

ssh-keygen -t rsa -C "account@gmail.com"

//填写email地址，然后一直“回车”ok
打开本地..\.ssh\id_rsa.pub文件。此文件里面内容为刚才生成人密钥。

2. 	SSH KEY ADD

登陆github系统。点击右上角的 Account Settings--->SSH Public keys ---> add another public keys

把你本地生成的密钥复制到里面（key文本框中）， 点击 add key 就ok了

3.	Test

ssh -T git@github.com

如果提示：Hi defnngj You've successfully authenticated, but GitHub does not provide shell access. 说明你连接成功了



##	go1.5 build @windows

[gcc-TMD](http://sourceforge.net/projects/tdm-gcc/)

### env

export CC=C:\tmd-gcc\bin\gcc.exe

export CGO_ENABLED=1

__build go1.5 with go1.4__

export GOROOT_BOOTSTRAP=go1.4

export GOROOT=...

export GOBIN=...

export GOPATH=...

PATH=%...%:$PATH

##	cross compile

###	build

CGO_ENABLED=0 GOOS=linux GOARCH=amd64 ./make.bash

### go build

CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build