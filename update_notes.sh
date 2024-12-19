#!/bin/bash

# 设置错误中断
set -e

# 安装 Pelican 和依赖
python -m pip install --upgrade pip
python -m pip install "pelican[markdown]"

# 克隆 GitHub Pages 分支
git clone https://github.com/manymore13/manymore13.github.io.git
cd manymore13.github.io
git checkout gh-pages
cd ..

# 清理旧文件并复制新内容
rm -rf docs/*
rm -rf content/*
cp -r ./manymore13.github.io/* ./content/

# 生成静态网站
pelican content

# 提交和推送更改
git config --global user.name "github-actions"
git config --global user.email "github-actions@github.com"
git add .
git commit -m "Automated update notes" || echo "No changes to commit"
git push origin gh-pages
