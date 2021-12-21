package cn.gof.composite.ftx_item;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kebukeYi
 * @date :  2021-12-21 00:47
 * @description: 城市匹配类
 * @question:
 * @link:
 **/
@Data
public class CityRequire extends ItemRequire {

    //下几个节点
    List<ItemRequire> child = new ArrayList<ItemRequire>();

    @Override
    boolean add(ItemRequire itemRequire) {
        return child.add(itemRequire);
    }

    @Override
    boolean remove(ItemRequire itemRequire) {
        return child.remove(itemRequire);
    }

    @Override
    List<ItemRequire> getChild() {
        return child;
    }

    @Override
    String getRequireValueFromUser(UserInfo userInfo) {
        //获得用户信息中的 城市值
        return userInfo.getCityIdList();
    }

    @Override
    boolean isLeaf() {
        //是否是叶子节点（当前未使用到）
        return false;
    }

    public static void main(String[] args) {
        String cityIdList = "11111,11112";
        final String[] split = cityIdList.split(",");
        for (String s : split) {
            System.out.println(s);
        }
    }

}
 
