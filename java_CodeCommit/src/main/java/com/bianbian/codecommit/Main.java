package com.bianbian.codecommit;

import com.bianbian.common.LogUtils;

public class Main {
    private static final String TAG = "Main";

    private static final String HELP = "--help";
    private static final String HELP1 = "--h";
    private static final String HELP2 = "--H";

    private static final String CODE_COMMIT_BIN_INIT = "--codecommit_bininit";

    /* 后面跟两个参数，第一个code路径，第二个本地branch，第三个远程branch */
    /* java -jar bianbian_codeCommit.jar --createpatch C:\Users\1\Desktop\testcode\JZEasyAndroid native_dev dev */
    private static final String createPatchTag = "--createpatch";

    /* 代码提交 */
    private static final String CODE_COMMIT = "--codecommit";
    private static final String OPENDES = "--opendes";

    /* 接收参数指定执行目录或者执行所有 */
    public static void main(String[] args) {
        /* 解析参数 */
        switch (getType(args)) {
            /* 说明文档 */
            case HELP:
                printHelpMsg();
                break;
            /* 下面是代码提交相关功能 */
            case CODE_COMMIT_BIN_INIT:
                /* 执行sh文件 bin 文件夹初始化 */
                new BinInit();
                break;
            /* 自动创建patch */
            case createPatchTag:
                new CreatePatch(args);
                break;
            /* 打开代码提交帮助文档 */
            case OPENDES:
                break;
            case CODE_COMMIT:
                new CodeCommit();
                break;
            default:
                LogUtils.info(TAG, "");
                LogUtils.info(TAG, "运行 \" java -jar bianbian_codeCommit.jar --help \" 查看支持的命令");
                break;
        }

        //回收资源
        int i = 0;
        while (i++ < 10) {
            System.gc();
        }
    }

    /* java -jar bianbian_codeCommit.jar --createpatch C:\Users\1\Desktop\testcode\JZEasyAndroid native_dev dev */
    private static String getType(String[] args) {
        if (args == null || args.length == 0) {
            throw new RuntimeException("运行 \" java -jar bianbian_codeCommit.jar --help \" 查看支持的命令");
        }
        if (args[0].equals(OPENDES)) {
            return OPENDES;
        }
        if (args[0].equals(HELP) || args[0].equals(HELP1) || args[0].equals(HELP2)) {
            return HELP;
        }
        if (args[0].equals(CODE_COMMIT_BIN_INIT)) {
            return CODE_COMMIT_BIN_INIT;
        }
        if (args[0].equals(CODE_COMMIT)) {
            return CODE_COMMIT;
        }
        if (args[0].equals(createPatchTag)) {
            if (args.length != 4) {
                throw new RuntimeException("创建patch参数错误 eg：--createpatch D:/xx/xx/xx/xx nativebranch remotebranch");
            }
            return createPatchTag;
        }
        return "";
    }

    private static void printHelpMsg() {
        print("");
        print("--help, --h, --H            <输出帮助文档>");
        print("");
        print("");
        print("代码提交功能：");
        print("如果需要提交代码请按下面步骤先执行：");
        print("         1、请先执行一次 \" java -jar bianbian_codeCommit.jar --codecommit_bininit\" 初始化bin文件");
        print("         2、然后将\"git-bash.exe\"安装路径修改配置到 \" ./bin/Git安装目录_写在这里.txt \" 文件中");
        print("");
        print("         --codecommit_bininit        ");
        print("             初始化提交代码需要的 bin 文件");
        print("");
        print("         --codecommit                ");
        print("             填写好配置信息后进行代码提交");
        print("");
        print("         --createpatch            ");
        print("             自动对比打Patch，每笔提交单独Patch，生成的patch及配置文件可直接使用命令 \"--codecommit\" 进行代码提交 ");
        print("             使用方法：\"命令样例：--createpatch D:/xx/xx/xx/xx nativebranch remotebranch\"");
        print("             使用方法：\"参数解释：--createpatch 代码位置目录 本地分支名称 远程分支名称\"");
        print("");
        print("         --opendes                   ");
        print("             打开 提交代码 需要配置的详细说明文档，或者打开路径 \" ./bin/xxx \" 查看");
        print("");
        print("");
        print("1功能开发中：");
        print("         说明____");
        print("");
        print("");
        print("2功能开发中：");
        print("         说明____");
        print("");
        print("");
    }

    private static void print(String msg) {
        System.out.println(msg);
    }
}