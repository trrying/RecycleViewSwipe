package com.owm.clear.service;

import com.owm.clear.app.Constans;
import com.owm.clear.bean.HandleFile;
import com.owm.clear.bean.ScanType;
import com.owm.clear.bean.event.ScanEvent;
import com.owm.clear.util.FileUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 *
 * Created by owm on 2017/8/3.
 */

public class ScanFileThread extends Thread {

    public List<List<HandleFile>> result;

    private List<ScanType> scanType;

    public void setScanType(List<ScanType> scanType) {
        this.scanType = scanType;
    }

    public void setScanType(String[][] scanType) {
        if (scanType == null) {

        }
    }

    @Override
    public void run() {
        String innerSDCardPath = FileUtils.getInnerSDCardPath();
        EventBus.getDefault().post(new ScanEvent(Constans.SCAN_START));
        traverseFolder2(innerSDCardPath);
        EventBus.getDefault().post(new ScanEvent(Constans.SCAN_FINISH));
    }

    public void traverseFolder2(String path) {
        EventBus.getDefault().post(new ScanEvent(Constans.REFRESH_DIR));
        File file = new File(path);
        if (file.exists()) {
//            File[] matchFile = file.listFiles(new FileFilter() {
//                @Override
//                public boolean accept(File file) {
//                    return file.isFile() && file.length() > MATCH_FILE_SIZE;
//                }
//            });
//            if (matchFile != null && matchFile.length > 0) {
//                data.addAll(Arrays.asList(matchFile));
//                getHandler().sendEmptyMessage(REFRESH_LIST);
//            }
            File[] dirFile = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });

            if (dirFile.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : dirFile) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        traverseFolder2(file2.getAbsolutePath());
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }

}
