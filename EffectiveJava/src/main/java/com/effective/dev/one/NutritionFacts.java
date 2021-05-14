package com.effective.dev.one;

/**
 * @author : kebukeyi
 * @date :  2021-04-14 12:23
 * @description :  放弃 重叠构造器模式、JavaBean模式； 采用 建造者 Builder 模式；好耶；
 * @usinglink :
 **/
public class NutritionFacts {

    //required 必要参数
    private final int servingSize;
    private final int serving;

    //optional 可选参数
    private final int fat;
    private final int sodium;
    private final int calories;

    public static class Builder {

        private final int servingSize;
        private final int serving;

        //利用自定义的入参取代这个值
        private int fat = 0;
        private int sodium = 0;
        private int calories = 0;

        public Builder(int servingSize, int serving) {
            this.servingSize = servingSize;
            this.serving = serving;
        }

        public Builder calories(int val) {
            calories = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }

        public Builder fat(int val) {
            fat = val;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize = builder.servingSize;
        serving = builder.serving;
        fat = builder.fat;
        sodium = builder.sodium;
        calories = builder.calories;
    }

    @Override
    public String toString() {
        return "NutritionFacts{" +
                "servingSize=" + servingSize +
                ", serving=" + serving +
                ", fat=" + fat +
                ", sodium=" + sodium +
                ", calories=" + calories +
                '}';
    }

    public static void main(String[] args) {
        NutritionFacts nutritionFacts = new Builder(204, 2).calories(2).fat(2).sodium(4).build();
        System.out.println(nutritionFacts);
        //这两个并不会一定被执行
        System.gc();
        System.runFinalization();

        //臭名昭著
        System.runFinalizersOnExit(true);
        Runtime.runFinalizersOnExit(true);
    }
}
