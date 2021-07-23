package cn.gof.snapshot.aritcle;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 17:06
 * @description:
 * @question:
 * @link: https://mp.weixin.qq.com/s/J5a5ZcNCBBExpyCpU2o3wg
 **/
public class Test {

    public static void main(String[] args) {
        final ArticleMentManger manger = new ArticleMentManger();
        final Article article = new Article("标题a", "内容a", "图片a");
        System.out.println(article);
        manger.push(article.saveToMemento());
        article.setTitle("a修改为b");
        article.setContent("a修改为b");
        article.setImages("a修改为b");
        System.out.println(article);
        article.undoFromMemento(manger.pop());
        System.out.println(article.toString());
    }
}
 
