package cn.gof.builder;

/**
 * @author : kebukeyi
 * @date :  2021-07-17 10:30
 * @description: 当 isRef 为 true 的时候，arg 表示 String 类型的 refBeanId，type 不需要设置；当 isRef 为 false 的时候，arg、type 都需要设置
 * @question:
 * @link:
 **/
public class ConstructorArg {

    private boolean isRef;
    private Class type;
    private Object arg;

    //普通方式
    public ConstructorArg(boolean isRef, Class type, Object arg) {
        if (isRef) {
            if (arg != null && arg instanceof String) {
                this.arg = arg;
                this.isRef = isRef;
                this.type = type;
            } else {
                throw new IllegalArgumentException("arg 参数需要重新检查规则");
            }
        } else {
            if (arg != null && type != null) {
                this.arg = arg;
                this.isRef = isRef;
                this.type = type;
            } else {
                throw new IllegalArgumentException("arg  type 参数需要重新检查规则");
            }
        }
    }

    //建造者模式
    public ConstructorArg(Builder builder) {
        this.type = builder.type;
        this.isRef = builder.isRef;
        this.arg = builder.arg;
    }

    //建造者模式
    public static class Builder {
        private boolean isRef;
        private Class type;
        private Object arg;

        public ConstructorArg build() {
            if (isRef) {
                if (arg == null || !(arg instanceof String))
                    throw new IllegalArgumentException("arg 参数需要重新检查规则");
            } else {
                if (arg == null || type == null) {
                    throw new IllegalArgumentException("arg  type 参数需要重新检查规则");
                }
            }
            return new ConstructorArg(this);
        }

        public Builder setRef(boolean ref) {
            isRef = ref;
            return this;
        }

        public Builder setType(Class type) {
            this.type = type;
            return this;
        }

        public Builder setArg(Object arg) {
            this.arg = arg;
            return this;
        }
    }

    public static void main(String[] args) {
        //  final ConstructorArg constructorArg_1 = new ConstructorArg(true, null, "3");
        // final ConstructorArg constructorArg_2 = new ConstructorArg(false, null, 1);
        final ConstructorArg build = new Builder().setArg(1).setRef(false).setType(null).build();
    }
}
 
