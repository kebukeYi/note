package cn.gof.composite.ftx_item;

import lombok.val;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : kebukeYi
 * @date :  2021-12-21 01:05
 * @description: 开始
 * @question:
 * @link:
 **/
public class ItemService {

    public List<Item> getAllItem() {
        final ArrayList<Item> itemArrayList = new ArrayList<>();

        final CityRequire cityRequire = new CityRequire();
        cityRequire.setName("城市匹配类");
        cityRequire.setItemId("1");
        cityRequire.setRequireId("11");
        cityRequire.setRequireValue("11,12,13,14");

        final RoleRequire roleRequire = new RoleRequire();
        roleRequire.setName("角色匹配类");
        roleRequire.setItemId("1");
        roleRequire.setRequireId("12");
        roleRequire.setRequireValue("11,12,13,14");

        final SpecificAccountRequire accountRequire = new SpecificAccountRequire();
        accountRequire.setName("账号匹配类");
        accountRequire.setItemId("1");
        accountRequire.setRequireId("13");
        accountRequire.setRequireValue("11111,11112,11113");

        cityRequire.add(roleRequire);
        accountRequire.add(cityRequire);
        final Item item_1 = new Item("1", accountRequire);

        final CityRequire cityRequire_2 = new CityRequire();
        cityRequire_2.setName("城市匹配类");
        cityRequire_2.setItemId("2");
        cityRequire_2.setRequireId("21");
        cityRequire_2.setRequireValue("21,22,23,24");

        final RoleRequire roleRequire_2 = new RoleRequire();
        roleRequire_2.setName("角色匹配类");
        roleRequire_2.setItemId("2");
        roleRequire_2.setRequireId("22");
        roleRequire_2.setRequireValue("21,22,23,24");

        final SpecificAccountRequire accountRequire_2 = new SpecificAccountRequire();
        accountRequire_2.setName("账号匹配类");
        accountRequire_2.setItemId("1");
        accountRequire_2.setRequireId("13");
        accountRequire_2.setRequireValue("22222,22223,22224");

        cityRequire_2.add(roleRequire_2);
        accountRequire_2.add(cityRequire_2);
        final Item item_2 = new Item("2", accountRequire_2);

        final CityRequire cityRequire_3 = new CityRequire();
        cityRequire_3.setName("城市匹配类");
        cityRequire_3.setItemId("3");
        cityRequire_3.setRequireId("31");
        cityRequire_3.setRequireValue("31,32,33,34");

        final RoleRequire roleRequire_3 = new RoleRequire();
        roleRequire_3.setName("角色匹配类");
        roleRequire_3.setItemId("3");
        roleRequire_3.setRequireId("32");
        roleRequire_3.setRequireValue("31,32,33,34");

        final SpecificAccountRequire accountRequire_3 = new SpecificAccountRequire();
        accountRequire_3.setName("账号匹配类");
        accountRequire_3.setItemId("1");
        accountRequire_3.setRequireId("33");
        accountRequire_3.setRequireValue("33333,33334,33335");

        cityRequire_3.add(roleRequire_3);
        accountRequire_3.add(cityRequire_3);
        final Item item_3 = new Item("3", accountRequire_3);


        itemArrayList.add(item_1);
        itemArrayList.add(item_2);
        itemArrayList.add(item_3);
        return itemArrayList;
    }

    public List<String> getAllItemIdByUser(UserInfo userInfo) {
        //初始化用户能参与任务 IdList
        final ArrayList<String> itemIds = new ArrayList<>();
        //获得后台配置的所有任务
        final List<Item> allItem = getAllItem();
        //遍历所有任务集合来筛选用户能参与的任务集合(最后的结果 可能是全部任务 或者 其中二三任务 )
        for (Item item : allItem) {
            //获得当前任务所配置的 规则树根 root
            final ItemRequire itemRequireRoot = item.getRoot();
            //传入参数 ：任务 规则树根 用户信息
            //当前任务匹配 ( 匹配思想：只要有一个规则节点匹配成功，那么就直接返回，不再继续匹配其子节点)
            //这里是递归匹配 并不是 map 的 for 循环 匹配
            if (convert(item, itemRequireRoot, userInfo)) {
                //当前任务的几个规则中 存在一个规则要求 用户能够满足 就保存起来
                itemIds.add(item.getItemId());
            }
        }
        return itemIds;
    }

    public boolean convert(Item item, ItemRequire itemRequire, UserInfo userInfo) {
        //先来当前节点进行匹配  ItemRequire是抽象类 根据传入的具体实现类进行匹配(城市匹配、角色匹配、账号匹配)
        final boolean mache = itemRequire.mache(userInfo);
        //打印匹配过程
        System.out.println("任务" + item.getItemId() + "  的  " + itemRequire.getName() + "   " + userInfo.getAccount() + "   " + mache);
        //如果当前节点并不能匹配
        if (!mache) {
            //获取当前节点是否存在子节点
            final List<ItemRequire> child = itemRequire.getChild();
            //假如还存在子节点 那么就有机会 任务和用户匹配上
            if (child != null && child.size() > 0) {
                //再来一 一匹配
                for (ItemRequire require : child) {
                    //传入当前节点 再去匹配
                    return convert(item, require, userInfo);
                }
            } else {
                //不存在子节点 并且 当前任务又不能满足匹配 那么就返回失败 说明 当前任务 用户不能参与
                return false;
            }
        }
        return mache;
    }

    public static void testSimpleItemOfAccount() {
        final ItemService itemService = new ItemService();
        final UserInfo userInfo = new UserInfo();
        userInfo.setAccount("11111"); //账号匹配上了 就返回
        userInfo.setCityIdList("11");//不再匹配
        userInfo.setRoleIdList("11");//不再匹配
        final List<String> allItemIdByUser = itemService.getAllItemIdByUser(userInfo);
        System.out.println(allItemIdByUser);
        /**
         * 任务1  的  账号匹配类   11111   true
         *
         * 任务2  的  账号匹配类   11111   false
         * 任务2  的  城市匹配类   11111   false
         * 任务2  的  角色匹配类   11111   false
         * 任务3  的  账号匹配类   11111   false
         * 任务3  的  城市匹配类   11111   false
         * 任务3  的  角色匹配类   11111   false
         * [1]
         */
    }

    public static void testSimpleItemOfCity() {
        final ItemService itemService = new ItemService();
        final UserInfo userInfo = new UserInfo();
        userInfo.setAccount("11110");//匹配失败 往下匹配
        userInfo.setCityIdList("11");//城市匹配上了  返回
        userInfo.setRoleIdList("11");//不再匹配
        final List<String> allItemIdByUser = itemService.getAllItemIdByUser(userInfo);
        System.out.println(allItemIdByUser);
        /**
         * 任务1  的  账号匹配类   11110   false
         * 任务1  的  城市匹配类   11110   true
         *
         * 任务2  的  账号匹配类   11110   false
         * 任务2  的  城市匹配类   11110   false
         * 任务2  的  角色匹配类   11110   false
         * 任务3  的  账号匹配类   11110   false
         * 任务3  的  城市匹配类   11110   false
         * 任务3  的  角色匹配类   11110   false
         * [1]
         */
    }

    public static void testSimpleItemOfRole() {
        final ItemService itemService = new ItemService();
        final UserInfo userInfo = new UserInfo();
        userInfo.setAccount("11110");
        userInfo.setCityIdList("10");
        userInfo.setRoleIdList("11");
        final List<String> allItemIdByUser = itemService.getAllItemIdByUser(userInfo);
        System.out.println(allItemIdByUser);
        /**
         * 任务1  的  账号匹配类   11110   false
         * 任务1  的  城市匹配类   11110   false
         * 任务1  的  角色匹配类   11110   true
         * 任务2  的  账号匹配类   11110   false
         * 任务2  的  城市匹配类   11110   false
         * 任务2  的  角色匹配类   11110   false
         * 任务3  的  账号匹配类   11110   false
         * 任务3  的  城市匹配类   11110   false
         * 任务3  的  角色匹配类   11110   false
         * [1]
         */
    }

    public static void testMultipleItemOfAccount() {
        final ItemService itemService = new ItemService();
        final UserInfo userInfo = new UserInfo();
        userInfo.setAccount("11111,22222"); //账号匹配上了 就返回
        userInfo.setCityIdList("11");//不再匹配
        userInfo.setRoleIdList("11");//不再匹配
        final List<String> allItemIdByUser = itemService.getAllItemIdByUser(userInfo);
        System.out.println(allItemIdByUser);
        /**
         * 任务1  的  账号匹配类   11111,22222   true
         *
         * 任务2  的  账号匹配类   11111,22222   true
         *
         * 任务3  的  账号匹配类   11111,22222   false
         * 任务3  的  城市匹配类   11111,22222   false
         * 任务3  的  角色匹配类   11111,22222   false
         * [1, 2]
         */
    }

    public static void testMultipleItemOfCity() {
        final ItemService itemService = new ItemService();
        final UserInfo userInfo = new UserInfo();
        userInfo.setAccount("11110");//匹配失败 往下匹配
        userInfo.setCityIdList("11,22");//城市匹配上了  返回
        userInfo.setRoleIdList("11");//不再匹配
        final List<String> allItemIdByUser = itemService.getAllItemIdByUser(userInfo);
        System.out.println(allItemIdByUser);
        /**
         * 任务1  的  账号匹配类   11110   false
         * 任务1  的  城市匹配类   11110   true
         * 任务2  的  账号匹配类   11110   false
         * 任务2  的  城市匹配类   11110   true
         * 任务3  的  账号匹配类   11110   false
         * 任务3  的  城市匹配类   11110   false
         * 任务3  的  角色匹配类   11110   false
         * [1, 2]
         */
    }

    public static void testMultipleItemOfRole() {
        final ItemService itemService = new ItemService();
        final UserInfo userInfo = new UserInfo();
        userInfo.setAccount("11110");//各个任务匹配失败 往下走
        userInfo.setCityIdList("10");// 各个任务匹配失败 往下走
        userInfo.setRoleIdList("11,22");//匹配上 返回
        final List<String> allItemIdByUser = itemService.getAllItemIdByUser(userInfo);
        System.out.println(allItemIdByUser);
        /**
         * 任务1  的  账号匹配类   11110   false
         * 任务1  的  城市匹配类   11110   false
         * 任务1  的  角色匹配类   11110   true
         * 任务2  的  账号匹配类   11110   false
         * 任务2  的  城市匹配类   11110   false
         * 任务2  的  角色匹配类   11110   true
         * 任务3  的  账号匹配类   11110   false
         * 任务3  的  城市匹配类   11110   false
         * 任务3  的  角色匹配类   11110   false
         * [1, 2]
         */
    }

    public static void testComplexItem() {
        final ItemService itemService = new ItemService();
        final UserInfo userInfo = new UserInfo();
        userInfo.setAccount("33333");//只能匹配到 任务3 的 账号
        userInfo.setCityIdList("22");// 只能匹配到  任务2 的城市
        userInfo.setRoleIdList("44");// 哪个都不能匹配到
        final List<String> allItemIdByUser = itemService.getAllItemIdByUser(userInfo);
        System.out.println(allItemIdByUser);
        /**
         * 任务1  的  账号匹配类   33333   false
         * 任务1  的  城市匹配类   33333   false
         * 任务1  的  角色匹配类   33333   false
         * 任务2  的  账号匹配类   33333   false
         * 任务2  的  城市匹配类   33333   true
         * 任务3  的  账号匹配类   33333   true
         * [2, 3]
         */
    }

    public static void testComplexItemOfNone() {
        final ItemService itemService = new ItemService();
        final UserInfo userInfo = new UserInfo();
        userInfo.setAccount("5555");//哪个都不能匹配到
        userInfo.setCityIdList("55");// 哪个都不能匹配到
        userInfo.setRoleIdList("55");// 哪个都不能匹配到
        final List<String> allItemIdByUser = itemService.getAllItemIdByUser(userInfo);
        System.out.println(allItemIdByUser);
        /**
         * 任务1  的  账号匹配类   5555   false
         * 任务1  的  城市匹配类   5555   false
         * 任务1  的  角色匹配类   5555   false
         * 任务2  的  账号匹配类   5555   false
         * 任务2  的  城市匹配类   5555   false
         * 任务2  的  角色匹配类   5555   false
         * 任务3  的  账号匹配类   5555   false
         * 任务3  的  城市匹配类   5555   false
         * 任务3  的  角色匹配类   5555   false
         * []
         */
    }

    public static void main(String[] args) {
        // testSimpleItemOfAccount();
        // testSimpleItemOfCity();
        // testSimpleItemOfRole();

        // testMultipleItemOfAccount();
        // testMultipleItemOfCity();
        // testMultipleItemOfRole();

        // testComplexItem();
        testComplexItemOfNone();

    }
}

