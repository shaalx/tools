#!/bin/sh
#commit changes to remote

git status
git add -A
git add .

#git status
# echo "Press any key to push changes to remote..."
 read commit
 git commit -m  $commit
git push github master:master
sleep 2