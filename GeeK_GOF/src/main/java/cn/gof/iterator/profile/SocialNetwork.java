package cn.gof.iterator.profile;

/**
 * @author : kebukeyi
 * @date :  2021-07-22 15:42
 * @description : 定义通用的社交网络接口
 * @question :
 * @usinglink :
 **/
public interface SocialNetwork {
    ProfileIterator createFriendsIterator(String profileEmail);

    ProfileIterator createCoworkersIterator(String profileEmail);
}
