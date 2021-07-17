package cn.gof.builder;

import org.apache.commons.lang3.StringUtils;

/**
 * @author : kebukeyi
 * @date :  2021-07-17 10:10
 * @description:  这种违背了什么原则???
 * @question:
 * @link:
 **/
public class ResourcePoolConfigOfBuilder2 {

    //包含着 原始类的 默认值
    public static final int DEFAULT_MAX_TOTAL = 8;
    public static final int DEFAULT_MAX_IDLE = 8;
    public static final int DEFAULT_MIN_IDLE = 0;
    //必填项
    public String name;
    public int maxTotal = DEFAULT_MAX_TOTAL;
    public int maxIdle = DEFAULT_MAX_IDLE;
    public int minIdle = DEFAULT_MIN_IDLE;

    private ResourcePoolConfigOfBuilder2(Builder builder) {
        this.name = builder.name;
        this.maxTotal = builder.maxTotal;
        this.maxIdle = builder.maxIdle;
        this.minIdle = builder.minIdle;
    }

    private ResourcePoolConfigOfBuilder2() {
    }

    //静态内部类
    public static class Builder extends ResourcePoolConfigOfBuilder2 {

        public Builder() {
        }

        // 校验逻辑放到这里来做，包括必填项校验、依赖关系校验、约束条件校验等
        public ResourcePoolConfigOfBuilder2 build() {
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
            return new ResourcePoolConfigOfBuilder2(this);
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
        final ResourcePoolConfigOfBuilder2 ofBuilder2 = new Builder().setMaxIdle(1).setMinIdle(2).setName("").setMaxTotal(1).build();
        System.out.println(ofBuilder2);
    }
}
 
