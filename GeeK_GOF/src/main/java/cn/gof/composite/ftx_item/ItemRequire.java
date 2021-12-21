package cn.gof.composite.ftx_item;

import lombok.Data;

import java.util.List;

/**
 * @author : kebukeYi
 * @date :  2021-12-21 00:42
 * @description: 任务要求条件(公共抽出)
 * @question:
 * @link:
 **/
@Data
public abstract class ItemRequire {

    //所属哪个任务id
    public String itemId;
    //匹配类的名字
    private String name;
    //自己的行记录id
    public String requireId;
    //要求的值
    public String requireValue;

    abstract boolean add(ItemRequire itemRequire);

    abstract boolean remove(ItemRequire itemRequire);

    abstract List<ItemRequire> getChild();

    boolean mache(UserInfo userInfo) {
        final String cityIdList = getRequireValueFromUser(userInfo);
        String[] strings = null;
        if (!"".equals(cityIdList) && cityIdList != null) {
            strings = cityIdList.split(",");
        } else {
            return false;
        }
        final String[] split = requireValue.split(",");
        //这里需要优化性能
        for (String s : split) {
            for (String string : strings) {
                if (s.equals(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    abstract String getRequireValueFromUser(UserInfo userInfo);

    @Deprecated
    abstract boolean isLeaf();

}
 
