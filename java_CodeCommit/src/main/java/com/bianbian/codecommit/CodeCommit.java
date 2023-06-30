package com.bianbian.codecommit;

import static com.bianbian.common.FileUtil.spell;

import com.bianbian.common.FileUtil;
import com.bianbian.common.LogUtils;
import com.bianbian.common.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CodeCommit {
    private static final String TAG = "CodeCommit";

    private static ReadConfig readConfig = new ReadConfig();

    public CodeCommit() {
        codeCommit();
    }

    /* Java 获取文件Jar包中读取文件 */
    /* https://blog.csdn.net/the_liang/article/details/103957426 */
    private void codeCommit() {
        /* 获取提交的仓库信息 */
        List<ReadConfig.CodeCommitConfigBean> configBeans = readConfig.getCommitConfigs();
        LogUtils.info(TAG, "打印config信息：");
        /* 执行结果列表 */
        resultList = new HashMap<>();
        for (ReadConfig.CodeCommitConfigBean bean : configBeans) {
            LogUtils.info(TAG, "");
            LogUtils.info(TAG, "projectName：" + bean.projectName);
            LogUtils.info(TAG, "ProjectMsg: " + bean.projectMsg.toString());
            LogUtils.info(TAG, "patchMsg: " + bean.patchMsg.toString());
            /* 子线程提交代码 */
            new Thread(bean.projectName) {
                @Override
                public void run() {
                    commitCode(bean, bean.projectName);
                }
            }.start();
            /* 执行结果全部置为false */
            resultList.put(bean.projectName, false);
        }

        /* 另起线程判断是否全部提交完毕 */
        new Thread(() -> {
            System.out.print("正在提交，等待-");
            while (wait) {
                System.out.print(" -");
                wait = false;
                for (String key : resultList.keySet()) {
                    if (!resultList.get(key)) {
                        wait = true;
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!wait) {
                    System.out.println("");
                    LogUtils.info(TAG, "提交完毕");
                    try {
                        /* result */
                        /* 将成功的mr 汇总 放入 最开头 */
                        /* 文件中编写一共提交的代码仓情况 */
                        /* 输出每个 代码仓 提交最后的日志*/
                        String path = FileUtil.getRunningPath() + File.separator + "result.txt";
                        LogUtils.info(TAG, "提交完毕: " + path);
                        LogUtils.printList("", "", FileUtil.readTextFileLineToListStr(path));
                        Process p = Runtime.getRuntime().exec("notepad " + path);//调用系统的记事本
                    } catch (Exception a) {
                        System.out.println("Error exec notepad");
                    }
                }
            }
        }).start();
    }

    boolean wait = true;
    Map<String, Boolean> resultList = new HashMap<>();

    /* 【Java基础知识】java调用并执行shell脚本 */
    /* https://blog.csdn.net/qq_41893274/article/details/116573250 */
    private void commitCode(ReadConfig.CodeCommitConfigBean codeCommitConfigBean, String projectName) {
        String jarPath = FileUtil.getRunningPath();
        String projectCodePath = spell(jarPath, "codeTmp", projectName);
        FileUtil.mkdirsIfNotExist(projectCodePath);
        /* 下载代码 并切换到需要提价的分支 */
        /* D:\appInstall\git\git-bash.exe */
        try {
            Process p = Runtime.getRuntime().exec(readConfig.getGitExePath() + " ./bin/cloneCode.sh "
                    + projectName + " "
                    + codeCommitConfigBean.projectMsg.ssh + " "
                    + codeCommitConfigBean.projectMsg.projectName + " "
                    + codeCommitConfigBean.projectMsg.branch + " "
                    + codeCommitConfigBean.projectMsg.commiter + " "
                    + codeCommitConfigBean.projectMsg.reviewer + " "
                    + Util.time()
            );
            p.waitFor();
        } catch (IOException | InterruptedException err) {
            LogUtils.err(TAG, "commitCode", err);
        }

        LogUtils.info(TAG, projectName + " " + codeCommitConfigBean.projectMsg.projectName + " commitCode code clone end");
        LogUtils.info(TAG, projectName + " " + codeCommitConfigBean.projectMsg.projectName + " commitCode patch apply start：");

        /* 应用patch */
        for (int i = 0; i < codeCommitConfigBean.patchMsg.list.size(); i++) {
            /* 获取patch列表 */
            try {
                Process p = Runtime.getRuntime().exec(readConfig.getGitExePath() + " ./bin/applyPatch.sh "
                        + projectCodePath + File.separator + codeCommitConfigBean.projectMsg.projectName + " "
                        + codeCommitConfigBean.patchMsg.list.get(i).patchPath + " "
                        + codeCommitConfigBean.patchMsg.list.get(i).dtsOrAR + " "
                        + codeCommitConfigBean.patchMsg.list.get(i).msg
                );
                p.waitFor();
            } catch (IOException | InterruptedException err) {
                LogUtils.err(TAG, "commitCode", err);
            }
        }
        resultList.put(Thread.currentThread().getName(), true);
    }
}
