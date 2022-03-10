package com.example.taobao.entity;

import java.util.List;

/**
 * @创建人: lzh
 * @创建时间: 2022/3/7
 * @描述:
 */
public class FilePage {

    private List<FileInfo> fileInfos;

    private Boolean hashNext;

    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    public Boolean getHashNext() {
        return hashNext;
    }

    public void setHashNext(Boolean hashNext) {
        this.hashNext = hashNext;
    }
}
