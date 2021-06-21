package com.java.note.Algorithm.mianshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/19  下午 8:26
 * @Description
 */
public class NewsUtils {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Message {
        private Date beginTime;
        private Strings content;
        private int flag;
    }


    // 时间插入
    List<Message> merge_time(final List<Message> newsList, final List<Message> insertList) {
        if (newsList == null) return insertList;
        if (insertList == null) return newsList;

        for (Message insert : insertList) {
            for (int i = newsList.size() - 1; i > 0; i--) {
                //newsList :7:00    8:00  9:00   11:00  12:00  13:00
                //insertList: 7:30    8:30   9:30   10:30  11:30   12:00
                if (insert.flag == 1) {
                    if (insert.beginTime.before(newsList.get(i).beginTime) && insert.beginTime.after(newsList.get(i - 1).beginTime)) {
                        newsList.add(i, insert);
                        break;
                    }
                }
                newsList.add(insert);
            }
        }
        return newsList;
    }

    //固定插入
    List<Message> merge_fix(final List<Message> newsList, final List<Message> insertList) {
        return newsList;


    }


}
