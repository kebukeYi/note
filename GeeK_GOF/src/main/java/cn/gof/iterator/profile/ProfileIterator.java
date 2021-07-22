package cn.gof.iterator.profile;

/**
 * @author : kebukeyi
 * @date :  2021-07-22 15:40
 * @description :
 * @question :
 * @usinglink :
 **/
public interface ProfileIterator {
    boolean hasNext();

    Profile getNext();

    void reset();

}
