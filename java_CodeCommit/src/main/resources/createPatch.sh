commitId=$1
num=$2
codePath=$3
nativebranch=$4
resultpath=$5

echo $commitId
echo $num
echo $codePath
echo $nativebranch
echo $resultpath

cd $codePath
git checkout $nativebranch
git format-patch -$num $commitId

mv *.patch $resultpath\

