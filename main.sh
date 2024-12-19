pwd
python -m pip install "pelican[markdown]"
git checkout main
rm -r /docs/*
git clone https://github.com/manymore13/manymore13.github.io.git
cp -r ./manymore13.github.io/* /content/
pelican content

