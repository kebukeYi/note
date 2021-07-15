package com.java.note.Java_8.function;

@FunctionalInterface
public interface MessageBuilder {

    /**
     * 信息生成器
     *
     * @return 生成的信息
     */
    public abstract String builderMessage();
}
