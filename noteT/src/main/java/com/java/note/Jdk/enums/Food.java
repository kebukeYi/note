package com.java.note.Jdk.enums;

/**
 * @Author : mmy
 * @Creat Time : 2020/10/18  下午 1:21
 * @Description 我们可能需要对一组数据进行分类，比如进行食物菜单分类而且希望这些菜单都属于food类型，
 * appetizer(开胃菜)、mainCourse(主菜)、dessert(点心)、Coffee等，
 * 每种分类下有多种具体的菜式或食品，此时可以利用接口来组织，如下(代码引用自Thinking in Java)：
 */
public interface Food {
    enum Appetizer implements Food {
        SALAD, SOUP, SPRING_ROLLS;
    }

    enum MainCourse implements Food {
        LASAGNE, BURRITO, PAD_THAI,
        LENTILS, HUMMOUS, VINDALOO;
    }

    enum Dessert implements Food {
        TIRAMISU, GELATO, BLACK_FOREST_CAKE,
        FRUIT, CREME_CARAMEL;
    }

    enum Coffee implements Food {
        BLACK_COFFEE, DECAF_COFFEE, ESPRESSO,
        LATTE, CAPPUCCINO, TEA, HERB_TEA;
    }


    class TypeOfFood {
        public static void main(Strings[] args) {
            Food food = Appetizer.SALAD;
            food = MainCourse.LASAGNE;
            food = Dessert.GELATO;
            food = Coffee.CAPPUCCINO;
        }
    }

}
