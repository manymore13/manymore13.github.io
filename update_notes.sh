#!/bin/bash

# 设置错误中断
set -e

# 安装 Pelican 和依赖
python -m pip install --upgrade pip
python -m pip install "pelican[markdown]"

# 克隆 GitHub Pages 分支
# git clone https://github.com/manymore13/manymore13.github.io.git

#备份
rm -rf ../temp
mkdir -p ../temp
cp -r ./* ../temp

git checkout gh-pages

# 确保 content/ 目录存在
mkdir -p content

# 清理旧文件并复制新内容
rm -rf docs/*
rm -rf content/*
cp -r ../temp/* ./content/

# 生成静态网站
pelican content

# 清理默认
rm -rf ../temp

# 提交和推送更改
echo "------------------"
# git branch
# git config --global user.name "action"
# git config --global user.email "action@qq.com"
# git add .
# git commit -m "Automated update notes" || echo "No changes to commit"
# git push origin gh-pages
