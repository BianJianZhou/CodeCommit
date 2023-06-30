nativeCodePath=$1
nativebranch=$2
remotebranch=$3
resultPath=$4

echo $nativeCodePath
echo $nativebranch
echo $remotebranch
echo $resultPath

cd $nativeCodePath
git checkout $nativebranch
git log origin/$remotebranch.. >$resultPath/commitMsg.txt