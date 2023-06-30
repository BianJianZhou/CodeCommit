package com.bianbian.codecommit;

import com.bianbian.common.FileUtil;

import java.io.File;

public class BinInit {
    private static final String applyPatch = "applyPatch.sh";
    private static final String cloneCode = "cloneCode.sh";
    private static final String createPatch = "createPatch.sh";
    private static final String getCommitMsg = "getCommitMsg.sh";

    private static final String binPath = FileUtil.getRunningPath() + File.separator + "bin";

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
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), applyPatch, binPath + File.separator + applyPatch);
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), cloneCode, binPath + File.separator + cloneCode);
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), createPatch, binPath + File.separator + createPatch);
        FileUtil.jarFileCopyNativePath(Main.class.getClassLoader(), getCommitMsg, binPath + File.separator + getCommitMsg);
    }
}
