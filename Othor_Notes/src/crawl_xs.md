# 爬取小说

分享一个简单的例子：爬静态网页，通过requests获取网页html，再通过正则匹配内容，最后把内容下载保存到本地

```python
import os
import re
from multiprocessing.dummy import Pool

import requests

start_url = 'https://www.kanunu8.com/book3/5932/'
dir_name = '李敖回忆录'


def get_html(url):
    """
    获取网页源码
    :param url:网址
    :return: 网页源代码
    """
    return requests.get(url).content.decode('gbk')


def get_all_chapter_link(html):
    """
    :param html:
    :return: 章节名草，章节链接
    """
    all_chapter_url = []
    chapter_block = re.findall('正文<(.*?)</tbody>', html, re.S)[0]
    chapter_url = re.findall('href="(.*?)">', chapter_block, re.S)
    for url in chapter_url:
        all_chapter_url.append(start_url + url)
    return all_chapter_url


def save_chapter_article(chapter_url):
    """
    :param chapter_url:
    :return: 章节名称，章节内容
    """
    chapter_html = get_html(chapter_url).replace('\n', '')
    chapter_name = re.findall('size="4">(.*?)</font>', chapter_html, re.S)[0]
    chapter_content = re.findall('<p>(.*?)</p>', chapter_html, re.S)[0]
    chapter_content = chapter_content.replace('<br />', '')
    save(chapter_name, chapter_content)


def save(chapter_name, chapter_content):
    """
    :param chapter_name: 章节名草
    :param chapter_content:  章节内容
    :return: None
    """
    os.makedirs(dir_name, exist_ok=True)
    with open(os.path.join(dir_name, chapter_name + '.txt'), 'w', encoding='utf-8') as f:
        f.write(chapter_content)


if __name__ == '__main__':
    start_html = get_html(start_url)
    all_chapter_url = get_all_chapter_link(start_html)
    pool = Pool(4)
    pool.map(save_chapter_article, all_chapter_url)
    
```
