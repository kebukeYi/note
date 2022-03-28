package com.java.note.Algorithm.mianshi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/16  下午 12:51
 * @Description 有一个新闻列表 同时运营可以往这个新闻列表里 插入一些内容 如广告 或 其他一些运营内容 以便推广和曝光
 * <p>
 * 插入的内容 有三种插入方式
 * <p>
 * - 置顶插入 即 排在第一位
 * - 固定位置插入 如 插入在第二条新闻后面 第四条新闻后面
 * - 时间插入 每个新闻都有一个发布时间 如 新闻一 发布时间 10点 新闻二发布时间09点  如果插入内容配置的时间是 09:30 那么就插入在这两个新闻之间
 * **
 * * newsList 原始资讯列表
 * * insertList 插入内容
 * * *List<News>  merge(final List<News> newsList,final List<InsertContent> insertList)
 * * 返回 合并后的新闻列表 即将插入内容转成news 同时插入到指定位置
 */

public class News {

    static class Message {
        Message next;
        LocalTime startTime;
        LocalTime endTime;
        int flag;

        public Message getNext() {
            return next;
        }

        public void setNext(Message next) {
            this.next = next;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime = endTime;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public Message() {
        }

        public Message(LocalTime startTime, LocalTime endTime, int flag) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.flag = flag;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", flag=" + flag +
                    '}';
        }
    }


    public static void merge() {
        //新闻列表为 链表 方式 进行插入
        Message head = new Message(LocalTime.of(8, 0), LocalTime.of(9, 0), 1);
        Message next = new Message(LocalTime.of(10, 0), LocalTime.of(11, 0), 1);
        head.next = next;

        //创建 新闻、广告列表
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(LocalTime.of(7, 0), LocalTime.of(8, 0), 1));
        messages.add(new Message(LocalTime.of(9, 0), LocalTime.of(9, 30), 1));
        messages.add(new Message(LocalTime.of(9, 30), LocalTime.of(9, 40), 2));
        messages.add(new Message(LocalTime.of(9, 40), LocalTime.of(10, 20), 2));

        //新闻链表不为空
        Message result = merge(head, messages);

        //新闻链表为空
        // Message result = merge(null, messages);

        //输出结果链表
        while (result != null) {
            System.out.println(result.toString());
            result = result.next;
        }

    }

    public static Message merge(Message head, List<Message> contentList) {
        // 把 要插入的各个信息 按照 节目 开始时间 进行升序
        Collections.sort(contentList, new Comparator<Message>() {
            @Override
            public int compare(Message message, Message t1) {
                if (message.startTime.isAfter(t1.startTime))
                    return 1;
                else if (message.startTime.equals(t1.startTime)) return 0;
                else return -1;
            }
        });

        System.out.println(contentList);


        //如果新闻列表为空 直接赋值节目列表的第一个
        if (head == null) head = contentList.get(0);


        //当前节点的下一个节点的临时占位
        Message temp;
        //保存头节点的节点
        Message dummy = new Message();
        dummy.next = head;

        //开始遍历信息列表
        for (Message message : contentList) {
            //获取头节点为当前节点
            Message cur = head;
            //当前节点不为空时 进行新闻列表遍历
            while (cur != null) {
                // 判断  要插入的信息 是否 在 新闻列表的 首节点  之前
                // 是的话 就需要插入头 并成为 新的新闻链表头节点
                //不是的话  就判断 是否在 头节点之后
                if (message.endTime.isBefore(cur.startTime) || message.endTime.equals(cur.startTime)) {
                    //新插入的信息 指向 新闻链表的头节点
                    message.next = cur;
                    // 插入信息成为新的头节点
                    head = message;
                    //开始遍历下一个 信息节点
                    break;
                }

                //获取当前节点的下一个节点
                temp = cur.next;
                //判断 当前信息节点 是否 能插入 新闻列表首节点的下一个位置
                if (message.startTime.isAfter(cur.endTime) || message.startTime.equals(cur.endTime)) {
                    //如果 当前新闻节点的下一个节点为空  并且符合  信息符合插入时间逻辑   那就在当前链表尾部 新加信息
                    if (temp == null) {
                        cur.next = message;
                        //开始遍历下一个 信息节点
                        break;
                    }

                    //如果当前节点的下一个节点不为空  那么就需要判断你  是否符合插入逻辑
                    //符合的话：在链表中开始执行插入逻辑
                    //不符合的话：开始下一个新闻节点
                    if ((temp.startTime.isAfter(message.endTime) || temp.startTime.equals(message.endTime))) {
                        cur.next = message;
                        message.next = temp;
                        break;
                    }
                }
                // 开始下一个新闻节点
                cur = cur.next;
            }
        }
        //返回头节点
        return head;
    }


    public static void main(String[] args) {
        //链表方式
        // merge();

        //主要是实验 数组新闻列表 功能
        mergeTwo();
    }


    public static void mergeTwo() {
        //新闻列表为 数组 方式 进行插入
        List<Message> news = new ArrayList<>();
        Message head = new Message(LocalTime.of(8, 0), LocalTime.of(9, 0), 1);
        Message next = new Message(LocalTime.of(10, 0), LocalTime.of(11, 0), 1);
        Message next_1 = new Message(LocalTime.of(12, 0), LocalTime.of(13, 0), 1);


        news.add(head);
        news.add(next);
        news.add(next_1);

        List<Message> messages = new ArrayList<>();
        messages.add(new Message(LocalTime.of(7, 0), LocalTime.of(8, 0), 1));
        messages.add(new Message(LocalTime.of(9, 0), LocalTime.of(9, 30), 1));
        messages.add(new Message(LocalTime.of(9, 30), LocalTime.of(9, 40), 2));
        messages.add(new Message(LocalTime.of(9, 40), LocalTime.of(10, 20), 2));
        // System.out.println(mergeTwo(news, messages));


        // 插入到指定位置
        Message next_2 = new Message(LocalTime.of(11, 30), LocalTime.of(12, 0), 1);
        // System.out.println(insertById(news, next_2, 2));

        //置顶插入
        Message next_3 = new Message(LocalTime.of(7, 30), LocalTime.of(8, 0), 1);
        System.out.println(insertAtFrist(news, next_3));

    }

    //时间插入
    public static List<Message> mergeTwo(final List<Message> newsList, final List<Message> insertList) {
        if (insertList.isEmpty()) {
            return newsList;
        }


        // 把 要插入的各个信息 按照 节目开始时间  进行升序
        Collections.sort(insertList, new Comparator<Message>() {
            @Override
            public int compare(Message message, Message t1) {
                if (message.startTime.isAfter(t1.startTime))
                    return 1;
                else if (message.startTime.equals(t1.startTime)) return 0;
                else return -1;
            }
        });

        if (newsList.isEmpty()) {
            newsList.add(insertList.get(0));
        }

        for (int i = 0; i < insertList.size(); i++) {
            Message insert = insertList.get(i);

            for (int j = 0; j < newsList.size(); j++) {
                Message news = newsList.get(j);

                //是否符合 新闻列表的首节点插入
                if (insert.endTime.isBefore(news.startTime) || insert.endTime.equals(news.startTime)) {
                    newsList.add(j, insert);
                    break;
                }

                //是否符合当前节点的下一个节点插入
                if (insert.startTime.isAfter(news.endTime) || insert.startTime.equals(news.endTime)) {
                    //先获取当前节点的下一个节点
                    Message temp = newsList.get(j + 1);
                    //如果为空 则直接插入
                    if (temp == null) {
                        newsList.add(insert);
                        break;
                    } else {
                        //否则话就需要进行插入判断
                        if (temp.endTime.isBefore(temp.startTime)) {
                            newsList.add(j + 1, insert);
                            break;
                        }
                    }
                }
            }
        }
        return newsList;
    }


    //固定位置插入
    public static List<Message> insertById(final List<Message> newsList, Message news, int id) {
        int count = 0;
        for (int i = 0; i < newsList.size(); i++) {
            Message message = newsList.get(i);
            if (message.flag == 1) {
                count++;
            }
            if (count == id) {
                if (newsList.get(i + 1) == null) {
                    newsList.add(i + 1, news);
                } else {
                    Message next = newsList.get(i + 1);
                    if ((news.startTime.isAfter(message.endTime) || news.startTime.equals(message.endTime)
                            && (message.endTime.isBefore(news.startTime) || message.endTime.equals(news.startTime)))) {
                        newsList.add(i + 1, news);
                    }
                }
                return newsList;
            }
        }
        return newsList;
    }

    //置顶插入
    public static List<Message> insertAtFrist(final List<Message> newsList, Message news) {
        if (newsList.size() == 0) {
            newsList.add(news);
            return newsList;
        }

        Message index = newsList.get(0);
        if (news.endTime.isBefore(index.startTime) || news.endTime.equals(index.startTime)) {
            newsList.add(0, news);
        }
        return newsList;
    }
}
