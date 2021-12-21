package cn.gof.composite.ftx_item;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kebukeYi
 * @date :  2021-12-21 01:00
 * @description: 具体账号匹配类
 * @question:
 * @link:
 **/
@Data
public class SpecificAccountRequire extends ItemRequire {

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
        //获得用户信息中的 账号
        return userInfo.getAccount();
    }

    @Override
    boolean isLeaf() {
        return false;
    }
}
 
