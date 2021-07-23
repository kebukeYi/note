package cn.gof.snapshot.aritcle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 16:58
 * @description:
 * @question:
 * @link:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    private String title;
    private String content;
    private String images;

    public ArticleMenmento saveToMemento() {
        final ArticleMenmento menmento = new ArticleMenmento(this.title, this.content, this.images);
        return menmento;
    }

    public void undoFromMemento(ArticleMenmento articleMenmento) {
        this.content = articleMenmento.getContent();
        this.title = articleMenmento.getTitle();
        this.images = articleMenmento.getImages();
    }


}
 
