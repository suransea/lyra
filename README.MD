# PXX

## 项目介绍

数据库原型系统

## 使用说明

### pxxadmin

* 查看帮助： `pxxadmin -h`或`pxxadmin --help`
* 修改密码： `pxxadmin -u <user> -p <passwd>`
> 默认root密码为123456，使用AES-128加密存储于pxx.conf中（暂定）

## 协作指令

1. **克隆远程仓库** `git clone https://github.com/suransea/pxx.git`
2. **进入仓库** `cd pxx` 切换到仓库里
3. **查看分支** `git branch`  如果确定自己是在master就没问题。
4. **更新仓库** `git pull`
5. **分支说明**， `master`是**主分支** ,`dev` 是**开发分支**。
6. **切换到开发分支** `git checkout dev`
7. **修改bug，** **增加新功能**  
   * `git add <filename>`  filename 也可以用*表示全部
   * `git commit -m "message"` message格式照下面
   * `git push origin dev` 推送自己的改动
   * `git pull` 抓取仓库的新提交 （如果推送失败先更新解决冲突）
8. **日常开发** `git pull` 然后接6，不断迭代开发

> master分支由管理员来合并，防止后期项目出现bug崩溃，不用回滚直接拉，并且master作为稳定版本，每一次dev更新新功能并且没有bug了，管理员合并一次。

## 格式要求

**commit** 里面的信息 ：`<type> : <body> <footer>`

type必须有，body跟footer选填。

type：

* feat：新功能
* fix：修复bug
* docs：文档
* style：格式（代码功能逻辑不变）
* refactor：重构
* test：测试文件
* chore：辅助工具的变动

常见的信息如下：

```shell
git commit -m "fix: 修改某某bug"

git commit -m "feat: 增加新功能"
```

后面footer可以省略，描述清楚就行了。

如果自己输错了信息

`git commit --amend`

输入修改后的commit message 保存

`git push <remote> <branch> -f`
