package cn.gof.oo.Abstraction;

import java.awt.*;

/**
 * @author : kebukeyi
 * @date :  2021-07-14 09:49
 * @description : 抽象性,比如，我们在使用 C 语言的 malloc() 函数的时候，并不需要了解它的底层代码是怎么实现的.
 * @question :
 * @usinglink :
 **/
public interface IPictureStorage {
    void savePicture(Picture picture);

    Image getPicture(String pictureId);

    void deletePicture(String pictureId);

    void modifyMetaInfo(String pictureId, PictureMetaInfo metaInfo);

}
