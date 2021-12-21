package cn.gof.composite.ftx_item;

import lombok.Data;

/**
 * @author : kebukeYi
 * @date :  2021-12-21 00:41
 * @description: 任务类
 * @question:
 * @link:
 **/
@Data
public class Item {

    //任务id
    private String itemId;

    //要求树的根节点  递归查找
    //map类型的话 就循环查找
    private ItemRequire root;

    public Item(String itemId, ItemRequire root) {
        this.itemId = itemId;
        this.root = root;
    }
}
 
