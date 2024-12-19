pwd
python -m pip install "pelican[markdown]"

git clone https://github.com/manymore13/manymore13.github.io.git
git checkout gh-pages
rm -r docs/*
rm -r content/*
cp -r ./manymore13.github.io/* ./content/
pelican content
git add .
git commit -m "update notes"
git push origin gh-pages

