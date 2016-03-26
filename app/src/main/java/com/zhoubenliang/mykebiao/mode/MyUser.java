package com.zhoubenliang.mykebiao.mode;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/3/16.
 */
public class MyUser extends BmobUser{
    private String face_id;

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }
}
