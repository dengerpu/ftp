package main.java.java.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class FileBean {
    private String filename;  //文件名
    private String updateTime;  //修改日期
    private String fileType;   //文件类型
    private String fileLength;   //文件大小

    @Override
    public String toString() {
        return "{\'" + filename + '\'' +
                ",\'" + updateTime + '\'' +
                ",\'" + fileType + '\'' +
                ",\'" + fileLength + '\'' +
                '}'+",";
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTimes = simpleDateFormat.format(new Date(updateTime));
        this.updateTime = updateTimes;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength +"B";
    }

}
