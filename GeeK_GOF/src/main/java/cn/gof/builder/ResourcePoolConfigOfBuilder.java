package cn.gof.builder;

import org.apache.commons.lang3.StringUtils;

/**
 * @author : kebukeyi
 * @date :  2021-07-17 09:52
 * @description: 建造者模式 主要解决必填项参数过多、参数之间依赖/约束检验、控制对象创建后不可变、对象的状态可用 的问题
 * @description: 区别:工厂模式与建造者模式的区别： 工厂模式是用来创建不同但是相关类型的对象（继承同一父类或者接口的一组子类），
 *                          由给定的参数来决定创建哪种类型的对象。建造者模式是用来创建一种类型的复杂对象，通过设置不同的可选参数，“定制化”地创建不同的对象。
 * @question: 缺点: 使用建造者模式来构建对象，代码实际上是有点重复的，ResourcePoolConfig 类中的成员变量，要在 Builder 类中重新再定义一遍
 * @link:
 **/
public class ResourcePoolConfigOfBuilder {

    private String name;
    private int maxTotal;
    private int maxIdle;
    private int minIdle;

    private ResourcePoolConfigOfBuilder(Builder builder) {
        this.name = builder.name;
        this.maxTotal = builder.maxTotal;
        this.maxIdle = builder.maxIdle;
        this.minIdle = builder.minIdle;
    }

    //静态内部类
    public static class Builder {
        //包含着 原始类的 默认值
        private static final int DEFAULT_MAX_TOTAL = 8;
        private static final int DEFAULT_MAX_IDLE = 8;
        private static final int DEFAULT_MIN_IDLE = 0;
        //必填项
        private String name;
        private int maxTotal = DEFAULT_MAX_TOTAL;
        private int maxIdle = DEFAULT_MAX_IDLE;
        private int minIdle = DEFAULT_MIN_IDLE;

        // 校验逻辑放到这里来做，包括必填项校验、依赖关系校验、约束条件校验等
        public ResourcePoolConfigOfBuilder build() {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("...");
            }
            if (maxIdle > maxTotal) {
                throw new IllegalArgumentException("...");
            }
            if (minIdle > maxTotal || minIdle > maxIdle) {
                throw new IllegalArgumentException("...");
            }
            //耶斯莫拉
            return new ResourcePoolConfigOfBuilder(this);
        }

        public Builder setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("...");
            }
            this.name = name;
            return this;
        }

        public Builder setMaxTotal(int maxTotal) {
            if (maxTotal <= 0) {
                throw new IllegalArgumentException("...");
            }
            this.maxTotal = maxTotal;
            return this;
        }

        public Builder setMaxIdle(int maxIdle) {
            if (maxIdle < 0) {
                throw new IllegalArgumentException("...");
            }
            this.maxIdle = maxIdle;
            return this;
        }

        public Builder setMinIdle(int minIdle) {
            if (minIdle < 0) {
                throw new IllegalArgumentException("...");
            }
            this.minIdle = minIdle;
            return this;
        }
    }

    public static void main(String[] args) {
        final ResourcePoolConfigOfBuilder configOfBuilder = new Builder().setMaxIdle(1).setMinIdle(2).setName("").setMaxTotal(1).build();

    }
}
 
