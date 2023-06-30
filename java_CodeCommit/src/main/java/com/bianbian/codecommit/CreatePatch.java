package com.bianbian.codecommit;

import com.bianbian.common.FileUtil;
import com.bianbian.common.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreatePatch {
    private static final String TAG = "CreatePatch";

    private String codePath = "";

    private String nativebranch = "";

    private String remotebranch = "";

    private String getCommitMsgSH = "./bin/getCommitMsg.sh";

    private String createPatchSH = "./bin/createPatch.sh";

    private static ReadConfig readConfig = new ReadConfig();

    public CreatePatch(String[] args) {
        codePath = args[1];
        nativebranch = args[2];
        remotebranch = args[3];
        String patchSavePath = FileUtil.getRunningPath() + File.separator + "createPatch" + File.separator + FileUtil.pathSubEndName(codePath);
        FileUtil.mkdirsIfNotExist(patchSavePath);
        /* 生成提交信息文件 */
        try {
            Process p = Runtime.getRuntime().exec(readConfig.getGitExePath() + " " + getCommitMsgSH + " "
                    + codePath + " "
                    + nativebranch + " "
                    + remotebranch + " "
                    + patchSavePath
            );
            p.waitFor();
        } catch (IOException | InterruptedException err) {
            LogUtils.err(TAG, "commitCode", err);
        }

        /* 读commitMsg.txt */
        List<String> result = FileUtil.readTextFileLineToListStr(patchSavePath + File.separator + "commitMsg.txt");
        List<CommitMsg> commitMsgs = new ArrayList<>();
        CommitMsg commitMsg = new CommitMsg();
        String msg = "";
        for (int i = 0; i < result.size(); i++) {
            String str = result.get(i);
            if (str.contains("commit")) {
                if (i == 0) {
                    commitMsg.commit = getLineValue(str, "commit").trim();
                } else {
                    commitMsg.msg = msg;
                    commitMsgs.add(commitMsg);
                    commitMsg = new CommitMsg();
                    commitMsg.commit = getLineValue(str, "commit").trim();
                    msg = "";//这里出问题了
                }
            } else if (!str.contains("Author:") && !str.contains("Date:") && str.trim() != null && !"".equals(str.trim())) {
                msg += str.trim() + "\n";
                LogUtils.info(TAG, "BIAN: STR: " + str);
            }
            if (i == result.size() - 1) {
                commitMsg.msg = msg;
                commitMsgs.add(commitMsg);
            }
        }

        /* 生成patch */
        try {
            Process p = Runtime.getRuntime().exec(readConfig.getGitExePath() + " " + createPatchSH + " "
                    + commitMsgs.get(0).commit + " "
                    + commitMsgs.size() + " "
                    + codePath + " "
                    + nativebranch + " "
                    + patchSavePath
            );
            p.waitFor();
        } catch (IOException | InterruptedException err) {
            LogUtils.err(TAG, "commitCode", err);
        }

        List<CommitMsg> commitMsgsTmp = new ArrayList<>();
        /* commitMsg.txt 重新编辑 */
        for (int i = commitMsgs.size() - 1; i >= 0; i--) {
            commitMsgsTmp.add(commitMsgs.get(i));
        }
        String commitMsgFormatTextPath = patchSavePath + File.separator + "commitFormatMsg.txt";
        FileUtil.createFileIfNotExist(commitMsgFormatTextPath);
        File file = new File("");
        FileUtil.clearText(commitMsgFormatTextPath);
        for (int i = 0; i < commitMsgsTmp.size(); i++) {
            String str = "第" + i + "条" + "\n"
                    + commitMsgsTmp.get(i).msg + "\n";
            FileUtil.addStrToText(commitMsgFormatTextPath, str);
        }
    }

    private static class CommitMsg {
        public String commit;
        public String msg;
    }

    private String getLineValue(String str, String key) {
        return str.substring(str.indexOf(key) + key.length()).trim();
    }
}
