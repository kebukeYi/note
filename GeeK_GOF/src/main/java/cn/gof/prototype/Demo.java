package cn.gof.prototype;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : kebukeyi
 * @date :  2021-07-17 13:12
 * @description:
 * @question:
 * @link:
 **/
public class Demo {

    //版本一
    private ConcurrentHashMap<String, SearchWord> currentKeywords = new ConcurrentHashMap<>();
    private long lastUpdateTime = -1;

    public void refresh() {
        // 从数据库中取出更新时间>lastUpdateTime的数据，放入到currentKeywords中
        final List<SearchWord> searchWords = getSearchWords(lastUpdateTime);
        long maxNewUpdatedTime = lastUpdateTime;
        for (SearchWord searchWord : searchWords) {
            //如果有最新的 保存下来时间
            if (searchWord.getLastUpdateTime() > maxNewUpdatedTime) {
                maxNewUpdatedTime = searchWord.getLastUpdateTime();
            }
            //如果它已经在散列表中存在了，我们就更新相应的搜索次数、更新时间等信息
            if (currentKeywords.containsKey(searchWord.getKeyWord())) {
                currentKeywords.replace(searchWord.getKeyWord(), searchWord);
            } else {
                //如果它在散列表中不存在，我们就将它插入到散列表中
                currentKeywords.put(searchWord.getKeyWord(), searchWord);
            }
        }
        lastUpdateTime = maxNewUpdatedTime;
    }

    private List<SearchWord> getSearchWords(long lastUpdateTime) {
        // TODO: 从数据库中取出更新时间>lastUpdateTime的数据
        return null;
    }

    //新需求 版本A的只能在一个容器中  版本B的只能在另外一个容器中 就是分割开来
    // 版本二
    private HashMap<String, SearchWord> currentHashKeywords = new HashMap<>();

    //新的数据直接在 new 空间里完事了 直接赋值操作
    public void refresh2() {
        HashMap newKeywords = new LinkedHashMap<>();
        // 从数据库中取出所有的数据，放入到newKeywords中
        List<SearchWord> toBeUpdatedSearchWords = getSearchWordsNoArgs();
        for (SearchWord searchWord : toBeUpdatedSearchWords) {
            //我们需要将这 10 万条数据从数据库中读出，然后计算哈希值，构建 newKeywords。这个过程显然是比较耗时
            // 为了提高效率，原型模式就派上用场了
            newKeywords.put(searchWord.getKeyWord(), searchWord);
        }
        currentHashKeywords = newKeywords;
    }

    public void refresh3() {
        // 原型模式就这么简单，拷贝已有对象的数据，更新少量差值
        HashMap<String, SearchWord> newKeywords = (HashMap<String, SearchWord>) currentHashKeywords.clone();

        // 从数据库中取出更新时间>lastUpdateTime的数据，放入到newKeywords中
        List<SearchWord> toBeUpdatedSearchWords = getSearchWords(lastUpdateTime);

        long maxNewUpdatedTime = lastUpdateTime;

        for (SearchWord searchWord : toBeUpdatedSearchWords) {
            if (searchWord.getLastUpdateTime() > maxNewUpdatedTime) {
                maxNewUpdatedTime = searchWord.getLastUpdateTime();
            }

            if (newKeywords.containsKey(searchWord.getKeyWord())) {
                SearchWord oldSearchWord = newKeywords.get(searchWord.getKeyWord());
                oldSearchWord.setConut(searchWord.getConut());
                oldSearchWord.setLastUpdateTime(searchWord.getLastUpdateTime());
            } else {
                newKeywords.put(searchWord.getKeyWord(), searchWord);
            }
        }

        lastUpdateTime = maxNewUpdatedTime;
        currentHashKeywords = newKeywords;
    }

    private List<SearchWord> getSearchWordsNoArgs() {
        // TODO: 从数据库中取出更新时间>lastUpdateTime的数据
        return null;
    }

    //序列化实现 深拷贝
    public Object deepCopy(Object object) throws IOException, ClassNotFoundException {
        final ByteArrayOutputStream bo = new ByteArrayOutputStream();
        final ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(object);

        final ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        final ObjectInputStream oi = new ObjectInputStream(bi);
        return oi.readObject();
    }

    public void refresh4() {
        // Shallow copy
        //我们可以先采用浅拷贝的方式创建 newKeywords
        // 对于需要更新的 SearchWord 对象，我们再使用深度拷贝的方式创建一份新的对象，替换 newKeywords 中的老对象
        HashMap<String, SearchWord> newKeywords = (HashMap<String, SearchWord>) currentHashKeywords.clone();

        // 从数据库中取出更新时间>lastUpdateTime的数据，放入到newKeywords中
        List<SearchWord> toBeUpdatedSearchWords = getSearchWords(lastUpdateTime);
        long maxNewUpdatedTime = lastUpdateTime;
        for (SearchWord searchWord : toBeUpdatedSearchWords) {
            if (searchWord.getLastUpdateTime() > maxNewUpdatedTime) {
                maxNewUpdatedTime = searchWord.getLastUpdateTime();
            }
            //直接将需要更新的旧对象从浅拷贝集合中移除，而不是去更新，避免影响老版本的数据，后面直接将数据添加到浅拷贝中
            if (newKeywords.containsKey(searchWord.getKeyWord())) {
                newKeywords.remove(searchWord.getKeyWord());
            }
            newKeywords.put(searchWord.getKeyWord(), searchWord);
        }

        lastUpdateTime = maxNewUpdatedTime;
        currentHashKeywords = newKeywords;
    }

}
 
