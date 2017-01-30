#!/bin/sh

if [ "$#" -ne 3 ]
   then

    echo "Usage ./update_submodule <directory> <url> <branch_name>"
    exit -1
fi

#Remove Submodule
git submodule deinit -f $1
git rm -f $1


#Add new Submodule
git submodule add --force -b $3 $2 $1
git submodule update --init --recursive --remote

#Add new Submodule
git add $1
git add ../.gitmodules
git commit -m "Updated/Added Submodule $1 to $2:$3"
