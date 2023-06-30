package com.bianbian.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    private static final String TAG = "FileUtil";

    public static String getRunningPath() {
        String path = System.getProperty("user.dir");
        LogUtils.debug(TAG, "getRunningPath str: " + path);
        return path;
    }

    /* Java逐行读取文件：https://blog.csdn.net/qq_30436011/article/details/127488031 */
    public static List<String> readTextFileLineToListStr(String path) {
        LogUtils.debug(TAG, "readTextFileLineToListStr path: " + path);
        File file = new File(path);
        if (!file.exists()) {
            LogUtils.debug(TAG, "readTextFileLineToListStr file notExist");
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException err) {
            LogUtils.err(TAG, "readTextFileLineToListStr", err);
            return new ArrayList<>();
        }
    }

    /* Java 获取文件Jar包中读取文件 https://blog.csdn.net/the_liang/article/details/103957426 */
    public static List<String> readJarFileLineToListStr(ClassLoader classLoader, String fileName) {
        URL url = classLoader.getResource(fileName);
        if (url == null) {
            LogUtils.debug(TAG, "readJarFileLineToListStr url == null");
            return new ArrayList<>();
        }
        LogUtils.debug(TAG, "readJarFileLineToListStr fileName path: " + url.getPath());
        try (InputStream inputStream = url.openStream();) {
            byte[] bytes = new byte[10];
            String str = "";
            while (inputStream.read(bytes) != -1) {
                str += new String(bytes, "UTF-8");
            }
            LogUtils.debug(TAG, "readJarFileLineToListStr str: " + str);
            return Arrays.stream(str.trim().split("\n")).toList();
        } catch (IOException err) {
            LogUtils.err(TAG, "readJarFileLineToListStr", err);
            return new ArrayList<>();
        }
    }

    public static void jarFileCopyNativePath(ClassLoader classLoader, String fileName, String path) {
        URL url = classLoader.getResource(fileName);
        if (url == null) {
            LogUtils.debug(TAG, "jarFileCopyNativePath " + fileName + " not exist");
            throw new RuntimeException("jarFileCopyNativePath " + fileName + " not exist");
        }
        if (!new File(path).exists()) {
            LogUtils.debug(TAG, "jarFileCopyNativePath " + path + " not exist");
            throw new RuntimeException("jarFileCopyNativePath " + path + " not exist");
        }
        LogUtils.debug(TAG, "jarFileCopyNativePath fileName path: " + url.getPath());
        LogUtils.debug(TAG, "jarFileCopyNativePath copy to path: " + path);
        try (InputStream is = url.openStream();
             OutputStream os = new FileOutputStream(path)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException err) {
            LogUtils.err(TAG, "jarFileCopyNativePath", err);
        }
    }

    /* 路径切割最后一个名称 */
    public static String pathSubEndName(String path) {
        return path.substring(path.lastIndexOf(File.separator) + 1);
    }

    /* 拼接路径 */
    public static String spell(String... arr) {
        String path = "";
        if (arr != null && arr.length > 0) {
            for (int i = 0; i < arr.length - 1; i++) {
                path += arr[i] + File.separator;
            }
            path += arr[arr.length - 1];
        }
        return path;
    }

    /* 文件夹是否存在不存在创建 */
    public static void mkdirsIfNotExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /* 文件夹是否存在不存在创建 */
    public static void createFileIfNotExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LogUtils.err(TAG, "fileIfNotExist: " + path, e);
                throw new RuntimeException(e);
            }
        }
    }

    /* 从 process.exec() 执行结果中按行获取字串 */
    public static List<String> readProcessExecStr(Process process) {
        List<String> result = new ArrayList<>();
        // 获取命令输出流
        try (InputStream inputStream = process.getInputStream()) {
            // 构造InputReader 读取输出结果
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            // 逐行读取输出结果
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            reader.close();
        } catch (IOException err) {
            LogUtils.err(TAG, "readProcessExecStr", err);
        }
        return result;
    }

    public static void addStrToText(String filePath, String content) {
        FileWriter fileWriter;
        try {
            /* 是否在原有内容上追加  */
            boolean append = true;
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearText(String path) {
        // 创建原文件对象
        final File file = new File(path);
        try {
            // 使用FileWriter不需要考虑原文件不存在的情况
            // 当该文件不存在时，new FileWriter(file)会自动创建一个真实存在的空文件
            FileWriter fileWriter = new FileWriter(file);
            // 往文件重写内容
            fileWriter.write("");// 清空
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
