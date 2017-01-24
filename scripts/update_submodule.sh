#!/bin/sh

if [ "$#" -ne 3 ]
   then

    echo "Usage ./update_submodule <directory> <branch_name> <url>"
    exit -1
fi

#Remove Submodule
git submodule deinit -f $1
git rm -f $1
git rm -f --cached $1


#Add new Submodule
git submodule add -b $2 $3 $1

#Add new Submodule
git add ../.gitsubmodule
git commit -m $(printf "Updated/Added Submodule %s" "$1")
