package com.bianbian.codecommit;

import static com.bianbian.common.FileUtil.spell;
import static com.bianbian.common.LogUtils.logFormat;

import com.bianbian.common.FileUtil;
import com.bianbian.common.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadConfig {
    private static final String TAG = "ReadConfig";
    /* jar所在路径config路径 */
    private static String configPath;

    public ReadConfig() {
        configPath = FileUtil.getRunningPath() + File.separator + "config";
    }

    /* 获取代码提交的配置对象 */
    public List<CodeCommitConfigBean> getCommitConfigs() {
        /* 获取需要提交代码的仓目录 */
        List<String> projects = new ArrayList<>();
        for (File configItemFile : new File(configPath).listFiles()) {
            String path = configItemFile.getPath();
            if (path.substring(path.lastIndexOf(File.separator) + 1).contains("project")) {
                projects.add(path);
                LogUtils.info(TAG, "projects path: " + path);
            }
        }
        /* 根据需要提交的工程构造 List<CodeCommitConfigBean> */
        List<CodeCommitConfigBean> configBeans = new ArrayList<>();
        for (String projectPath : projects) {
            CodeCommitConfigBean config = new CodeCommitConfigBean();
            config.projectName = FileUtil.pathSubEndName(projectPath);
            config.projectMsg = getProjectMsg(projectPath + File.separator + "config.txt");
            config.patchMsg = getPatchMsg(projectPath + File.separator + "patchMsgs.txt", projectPath);
            configBeans.add(config);
        }
        return configBeans;
    }

    /* 获取patch信息对象 */
    public PatchMsg getPatchMsg(String patchMsgsTxtPath, String projectPath) {
        PatchMsg pathMsg = new PatchMsg();
        pathMsg.list = new ArrayList<>();
        List<String> list = FileUtil.readTextFileLineToListStr(patchMsgsTxtPath);
        PatchMsgItem itemPathMsg = new PatchMsgItem();
        for (String str : list) {
            if (str.equals("--itemStart--")) {
                itemPathMsg = new PatchMsgItem();
            }
            if (str.contains("dtsOrAR =")) {
                itemPathMsg.dtsOrAR = getLineValue(str, "dtsOrAR =");
            }
            if (str.contains("msg =")) {
                itemPathMsg.msg = getLineValue(str, "msg =");
            }
            if (str.equals("--itemEnd--")) {
                pathMsg.list.add(itemPathMsg);
            }
        }
        /* 将 patch 文件路径信息 合并到 patch 信息列表中 */
        /* 一个 patch 对应一个提交信息 */
        List<String> patchFilePaths = getPatchFilePathList(projectPath);
        LogUtils.info(TAG, "工程:" + FileUtil.pathSubEndName(projectPath) + " 提交信息" + pathMsg.list.size() + "条, patch文件" + patchFilePaths.size() + "个");
        int min = Math.min(pathMsg.list.size(), patchFilePaths.size());
        if (pathMsg.list.size() != patchFilePaths.size()) {
            throw new RuntimeException("patch 文件数量和提交信息数量不一致");
        }
        for (int i = 0; i < min; i++) {
            pathMsg.list.get(i).patchPath = patchFilePaths.get(i);
        }
        return pathMsg;
    }

    /* 获取patch文件列表 */
    private List<String> getPatchFilePathList(String projectPath) {
        File[] files = new File(projectPath).listFiles();
        List<String> filePaths = new ArrayList<>();
        for (File file : files) {
            String path = file.getPath();
            if (FileUtil.pathSubEndName(path).endsWith(".patch")) {
                filePaths.add(path);
            }
        }
        return filePaths;
    }

    /* 获取提交信息 */
    public ProjectMsg getProjectMsg(String configTextPath) {
        /* 从每个工程目录的config.txt中读取ssh等配置信息 */
        List<String> list = FileUtil.readTextFileLineToListStr(configTextPath);
        ProjectMsg projectMsg = new ProjectMsg();
        for (String str : list) {
            if (str.contains("ssh =")) {
                projectMsg.ssh = getLineValue(str, "ssh =");
            }
            if (str.contains("projectName =")) {
                projectMsg.projectName = getLineValue(str, "projectName =");
            }
            if (str.contains("branch =")) {
                projectMsg.branch = getLineValue(str, "branch =");
            }
            if (str.contains("commiter =")) {
                projectMsg.commiter = getLineValue(str, "commiter =");
            }
            if (str.contains("reviewer =")) {
                projectMsg.reviewer = getLineValue(str, "reviewer =");
            }
        }
        return projectMsg;
    }

    private String getLineValue(String str, String key) {
        return str.substring(str.indexOf(key) + key.length()).trim();
    }

    /* 获取git.ext 文件路径 */
    public String getGitExePath() {
        String gitExeConfigPath = spell(configPath, "Git安装目录_写在这里.txt");
        LogUtils.debug(TAG, "getGitExePath gitExeConfigPath " + gitExeConfigPath);
        List<String> git_exePathTmp = FileUtil.readTextFileLineToListStr(gitExeConfigPath);
        if (git_exePathTmp == null || git_exePathTmp.size() == 0) {
            throw new RuntimeException("配置git安装目录：" + gitExeConfigPath);
        }
        return git_exePathTmp.get(0);
    }

    public class CodeCommitConfigBean {
        public String projectName;
        public ProjectMsg projectMsg;
        public PatchMsg patchMsg;
    }

    public class PatchMsg {
        public List<PatchMsgItem> list;

        @Override
        public String toString() {
            String str = "\n";
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    str += "\t\t\t\t" + i + ": " + "patchPath:" + list.get(i).patchPath
                            + "  dtsOrAR:" + list.get(i).dtsOrAR
                            + "  msg:" + list.get(i).msg
                            + "\n";
                }
            }
            return str;
        }
    }

    public static class PatchMsgItem {
        public String patchPath;
        public String dtsOrAR;
        public String msg;
    }

    public class ProjectMsg {
        public String ssh;
        public String projectName;
        public String branch;
        public String commiter;
        public String reviewer;

        @Override
        public String toString() {
            String str = "\n\t\t\t\t" + logFormat("ssh:", 15) + ssh
                    + "\n\t\t\t\t" + logFormat("projectName:", 15) + projectName
                    + "\n\t\t\t\t" + logFormat("branch:", 15) + branch
                    + "\n\t\t\t\t" + logFormat("commiter:", 15) + commiter
                    + "\n\t\t\t\t" + logFormat("reviewer:", 15) + reviewer;
            return str;
        }
    }
}
