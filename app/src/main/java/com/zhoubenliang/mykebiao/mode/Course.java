/**
 * @author yxw
 * date : 2014年4月17日 下午11:11:11
 */
package com.zhoubenliang.mykebiao.mode;

import java.io.Serializable;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    private int classid;
    private String classname;
    private int fromClassNum;
    private int classNumLen;
    private int weekday;
    private String teacher;
    private String classRoom;
    private int endIndex;
    private int startYear;//学年开始年
    private int endYear;//学年结束年
    private int semester;//学期
    /*上课时间（哪一周） 开始 */
    private int beginWeek;
    /*上课时间（哪一周） 结束 */
    private int endWeek;
    /* 课程类型（单周还是双周） */
    private int courseType;
    private int endSection;//第几节课结束

    public int getEndSection() {
        return endSection;
    }

    public void setEndSection(int endSection) {
        this.endSection = endSection;
    }


    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }


    public void setPoint(int fromX, int fromY, int toX, int toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    public void setClassid() {
        this.classid = this.classname.hashCode() + this.fromClassNum +
                this.classNumLen + this.weekday + this.classRoom.hashCode()
                + this.endWeek + this.beginWeek;
    }

    public int getFromX() {
        return fromX;
    }

    public void setFromX(int fromX) {
        this.fromX = fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public void setFromY(int fromY) {
        this.fromY = fromY;
    }

    public int getToX() {
        return toX;
    }

    public void setToX(int toX) {
        this.toX = toX;
    }

    public int getToY() {
        return toY;
    }

    public void setToY(int toY) {
        this.toY = toY;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public int getFromClassNum() {
        return fromClassNum;
    }

    public void setFromClassNum(int fromClassNum) {
        this.fromClassNum = fromClassNum;
    }

    public int getClassNumLen() {
        return classNumLen;
    }

    public void setClassNumLen(int classNumLen) {
        this.classNumLen = classNumLen;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getbeginWeek() {
        return beginWeek;
    }

    public void setBeginWeek(int beginWeek) {
        this.beginWeek = beginWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

}
