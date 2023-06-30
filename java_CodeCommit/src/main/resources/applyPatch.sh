codePath=$1
patchPath=$2
dtsOrAR=$3
msg=$4

echo " " >> $1/../clonelog1.txt
echo " " >> $1/../clonelog1.txt
echo " 开始 apply patch" >> $1/../clonelog1.txt
echo " 接受到的参数：" >> $1/../clonelog1.txt

echo $codePath >> $1/../clonelog1.txt
echo $patchPath >> $1/../clonelog1.txt
echo $dtsOrAR >> $1/../clonelog1.txt
echo $msg >> $1/../clonelog1.txt

cd $codePath 

echo " 应用patch：" >>../clonelog1.txt
git apply $patchPath >>../clonelog1.txt

echo " add全部：" >>../clonelog1.txt
git add . >>../clonelog1.txt

echo " 提交：" >>../clonelog1.txt
git commit -m "
[dtsOrAR]:$dtsOrAR
[msg]:msg"