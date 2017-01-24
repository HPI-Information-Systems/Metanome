if [ "$#" -neq 3 ]
   then

    echo "Usage ./update_submodule <directory> <branch_name> <url>"
fi

#Remove Submodule
git submodule deinit $1
git rm $1
git rm --cached $1


#Add new Submodule
git submodule add -b $2 $3 $1

#Add new Submodule
git add .gitsubmodule
git commit -m $(printf "Updated/Added Submodule %s" "$1")
