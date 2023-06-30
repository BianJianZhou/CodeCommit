projectPathName=$1
ssh=$2
projectName=$3
branch=$4
commiter=$5
reviewer=$6
time=$7

rm -rf ./codeTmp/$projectPathName/clonelog1.txt
echo " 接受到的参数：" >> ./codeTmp/$projectPathName/clonelog1.txt

echo $projectPathName >> ./codeTmp/$projectPathName/clonelog1.txt
echo $ssh >> ./codeTmp/$projectPathName/clonelog1.txt
echo $projectName >> ./codeTmp/$projectPathName/clonelog1.txt
echo $branch >> ./codeTmp/$projectPathName/clonelog1.txt
echo $commiter >> ./codeTmp/$projectPathName/clonelog1.txt
echo $reviewer >> ./codeTmp/$projectPathName/clonelog1.txt
echo $time >> ./codeTmp/$projectPathName/clonelog1.txt
echo "" >> ./codeTmp/$projectPathName/clonelog1.txt


# jar 调用执行的环境是当前jar包路径

cd ./codeTmp/$projectPathName
rm -rf $projectName
git clone $ssh >> clonelog1.txt
cd $projectName
echo " 开始pull：" >>../clonelog1.txt
git pull >>../clonelog1.txt
echo " 切换分支：" >>../clonelog1.txt
git checkout -b $time"_"$branch remotes/origin/$branch >>../clonelog1.txt
echo " 分支关系：" >>../clonelog1.txt
git branch -vv >>../clonelog1.txt
echo " 当前路径：" >>../clonelog1.txt
pwd >>../clonelog1.txt