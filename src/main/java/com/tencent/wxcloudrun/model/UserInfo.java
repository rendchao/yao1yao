package com.tencent.wxcloudrun.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rean
 * @version 1.0
 * @description: TODO
 * @date 2022/8/21 15:30
 */
public class UserInfo {


    public  String nickName;
   public String avatarUrl;
   public int totalCount;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "{" +
                "nickName:'" + nickName + '\'' +
                ", avatarUrl:'" + avatarUrl + '\'' +
                ", totalCount:" + totalCount +
                '}';
    }

    public static void main(String[] args) {
        String message = "{\"avatarUrl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI0sT18JZDiakI5ZfhoNsMBiacqQyNlXuty705zuZp9un85HNrS1iajpr6pMEJl6qsmbcdvXeatTMiaNA/132\",\"nickName\":\"～R e ａｎ\",\"count\":5}\n";
        UserInfo user  = JSON.parseObject(message, UserInfo.class);
        JSONObject jsonObject = JSONObject.parseObject(message);
        JSONArray json = new JSONArray();
        json.add(jsonObject);
        List<UserInfo> list = new ArrayList<UserInfo> ();
        list.add(user);
        String message2 = "{\"avatarUrl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI0sT18JZDiakI5ZfhoNsMBiacqQyNlXuty705zuZp9un85HNrS1iajpr6pMEJl6qsmbcdvXeatTMiaNA/132\",\"nickName\":\"～R e ａｎ\",\"count\":25}\n";
//        list.replaceAll();
        UserInfo user2  = JSON.parseObject(message, UserInfo.class);
        System.out.println(user == user2);
        list.add(user2);
//        boolean b = Collections.replaceAll(list, user, user2);
//        System.out.println(b);
        System.out.println(list.toString());



    }
}
