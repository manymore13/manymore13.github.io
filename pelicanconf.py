AUTHOR = 'Yanzhiwei'
SITENAME = 'manymore13'
SITEURL = ''

PATH = 'posts'

TIMEZONE = 'Asia/Shanghai'

DEFAULT_LANG = 'Chinese (Simplified)'

# Feed generation is usually not desired when developing
FEED_ALL_ATOM = None
CATEGORY_FEED_ATOM = None
TRANSLATION_FEED_ATOM = None
AUTHOR_FEED_ATOM = None
AUTHOR_FEED_RSS = None

# Blogroll
LINKS = (('Github', 'https://github.com/manymore13'),)

# Social widget
# SOCIAL = (('You can add links in your config file', '#'),
#           ('Another social link', '#'),)

DEFAULT_PAGINATION = False

# static paths will be copied without parsing their contents
STATIC_PATHS = [
    'img',
    'extra/robots.txt',
    ]

THEME = 'bootstrap'
# Uncomment following line if you want document-relative URLs when developing
#RELATIVE_URLS = True