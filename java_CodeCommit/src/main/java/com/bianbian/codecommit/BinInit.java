package com.bianbian.codecommit;

import com.bianbian.common.FileUtil;

import java.io.File;

public class BinInit {
    private static final String applyPatch = "applyPatch.sh";
    private static final String cloneCode = "cloneCode.sh";
    private static final String createPatch = "createPatch.sh";
    private static final String getCommitMsg = "getCommitMsg.sh";
    private static final String gitInstallPathConfig = "Git安装目录_写在这里.txt";
    private static final String desPdf = "BianBianTool 使用方式.pdf";

    private static final String 样例1 = "样例_config.rar";
    private static final String 样例2 = "样例_config.txt";
    private static final String 样例3 = "样例_patchMsgs.txt";

    private static final String binPath = FileUtil.getRunningPath() + File.separator + "bin";
    private static final String jarPath = FileUtil.getRunningPath();

    public BinInit() {
        File file = new File(binPath);
        if (file.exists()) {
            file.delete();
        }
        FileUtil.mkdirsIfNotExist(binPath);
        FileUtil.createFileIfNotExist(binPath + File.separator + applyPatch);
        FileUtil.createFileIfNotExist(binPath + File.separator + cloneCode);
        FileUtil.createFileIfNotExist(binPath + File.separator + createPatch);
        FileUtil.createFileIfNotExist(binPath + File.separator + getCommitMsg);
        FileUtil.createFileIfNotExist(binPath + File.separator + gitInstallPathConfig);
        FileUtil.createFileIfNotExist(binPath + File.separator + desPdf);
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), applyPatch, binPath + File.separator + applyPatch);
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), cloneCode, binPath + File.separator + cloneCode);
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), createPatch, binPath + File.separator + createPatch);
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), getCommitMsg, binPath + File.separator + getCommitMsg);
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), gitInstallPathConfig, binPath + File.separator + gitInstallPathConfig);
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), desPdf, binPath + File.separator + desPdf);

        FileUtil.createFileIfNotExist(jarPath + File.separator + 样例1);
        FileUtil.createFileIfNotExist(jarPath + File.separator + 样例2);
        FileUtil.createFileIfNotExist(jarPath + File.separator + 样例3);
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), 样例1, jarPath + File.separator + 样例1);
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), 样例2, jarPath + File.separator + 样例2);
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), 样例3, jarPath + File.separator + 样例3);
    }
}
