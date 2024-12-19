pwd
python -m pip install "pelican[markdown]"

git clone https://github.com/manymore13/manymore13.github.io.git
git checkout main
rm -r /docs/*
cp -r ./manymore13.github.io/* /content/
pelican content

