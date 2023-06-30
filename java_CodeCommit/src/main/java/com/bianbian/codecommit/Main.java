package com.bianbian.codecommit;

import com.bianbian.common.LogUtils;

import java.io.File;

public class Main {
    private static final String TAG = "Main";

    /* 后面跟两个参数，第一个code路径，第二个branch */
    private static final String createPatchTag = "--createpatch";

    /* 默认代码提交 */
    private static final String codeCommit = "--codecommit";

    /* 接收参数指定执行目录或者执行所有 */
    public static void main(String[] args) {
        /* 执行sh文件 bin 文件夹初始化 */
        new BinInit();

        /* 解析参数 */
        String type = codeCommit;
        if (verifyCreatePatchParams(args)) {
            type = createPatchTag;
        }

        switch (type) {
            case createPatchTag:
                new CreatePatch(args);
                break;
            case codeCommit:
                new CodeCommit();
                break;
            default:
                LogUtils.info(TAG, "TYPE default");
                break;
        }

        //回收资源
        int i = 0;
        while (i++ < 10) {
            System.gc();
        }
    }

    /* java -jar bianbian_codeCommit.jar --createpatch C:\Users\1\Desktop\testcode\JZEasyAndroid native_dev dev */
    private static boolean verifyCreatePatchParams(String[] args) {
        if (args == null || args.length == 0 || !args[0].equals(createPatchTag)) {
            return false;
        }
        if (args.length != 4) {
            throw new RuntimeException("创建patch参数错误 eg：--createpatch D:/xx/xx/xx/xx nativebranch remotebranch");
        }
        return true;
    }
}