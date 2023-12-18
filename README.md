# CodeCommit
# 支持功能：
自动打Patch
同时提交多仓、多分支代码、一笔提交生成多个提交记录

# 前置条件
window 电脑配置java环境变量，支持 java -jar 命令

# 使用方式
java -jar bianbian_codeCommit.jar [option] 
--help, --h, --H                <输出帮助文档>
--codecommit_bininit    初始化提交代码需要的 bin 文件
--codecommit             填写好配置信息后进行代码提交
--createpatch            自动生成patch
--opendes            打开详细说明文档 pdf

# --help, --h, --H                <输出帮助文档>
输入如下图
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/30566508-6d71-49dc-a77c-17660c359be6)


# --codecommit_bininit    初始化提交代码需要的 bin 文件
需要使用到电脑安装的 gitbash ，通过java代码调用 shell 脚本实现生成patch或提交代码
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/d5ea438d-d63d-4d71-8b7d-8f6977ae0a4c)
同时需要将 电脑上 安装的 git 配置到文件 “Git安装目录_写在这里.txt” 中

# --createpatch            自动生成patch
--createpatch D:/xx/xx/xx/xx nativebranch remotebranch
--createpatch 本地代码仓目录 本地分支名称 远程分支名称(不带remote)
生成的patch会在  “createPatch” 目录
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/c43edbdb-138d-4d3b-8acc-27a40259fe2a)
在此目录下，会以代码仓 目录名称 内生成对应 patch 文件，和对应的提交信息
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/3fee17d7-d02a-490d-baa1-82651195589d)
其中 "commitFormatMsg.txt" 中的内容为如下
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/fdb3d089-156d-4cbd-9963-a1b9dd823c62)
其中 “commitMsg.txt” 中内容 为 本地已 commit 未提交的 msg 信息，供查看
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/fed83415-a489-4188-90ba-56e18c6e6472)



# --codecommit             填写好配置信息后进行代码提交
需要在config目录下配置对应需要提交的代码仓信息，如无此目录，请新建 “config”
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/f2c1c420-a4de-42a6-868a-7a708cca9cb9)
如上图，对应提交的代码仓，文件夹目录以 “project_” 为开头，后面跟的是对应的工程名称；
工具Jar 识别的是 “project_” 开头的目录字串，每有一个对应文件夹，视为 一个仓需要提交；
待提交的代码仓生成的 patch 放入对应工程目录下：
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/01f3843b-b733-43d4-96a1-d2358885f445)
“config.txt” 文件为对应需要提交的 代码仓的配置，如下图
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/72c69bea-aa76-4620-a4d3-f64c4d9018f1)
“patchMsgs.txt” 为对应需要提交的 需要提交的 patch 信息 
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/5e301dbe-a05f-435a-b140-7fcdd8d1474b)



# --codecommit 使用其他说明（需要自己做一下实现）
代码提交功能在最后一步提交 git push 未实现，以为此功能用作在 黄区等，自己无代码提交权限，需要对应田主等进行提交的情况，需要自己将最后一步完成：
### 1、打开源码文件：applyPatch.sh
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/01b47a40-a9ec-4127-b9d1-b65ecdca18a0)
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/e305bbeb-2ad7-4542-a0c5-b42c6ca3ea10)
根据公司自己公司代码提交规范，修改 commit 信息提交；
及添加 git push 或者 git mr 等操作；
### 2、修改源码 JAVA 文件中的实现：CodeCommit.java
![image](https://github.com/BianJianZhou/CodeCommit/assets/20183236/31ccc004-50b1-479e-92bb-5222c13c2de2)
这里自己实现后会漂亮点，不做实现 仅做了上面的第一步，也不影响代码提交功能的使用
