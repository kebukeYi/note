package cn.gof.snapshot.aritcle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Stack;

/**
 * @author : kebukeYi
 * @date :  2021-07-23 17:04
 * @description:
 * @question:
 * @link:
 **/
@Data
@AllArgsConstructor
public class ArticleMentManger {

    private final Stack<ArticleMenmento> articleMenmentos = new Stack<>();

    public void push(ArticleMenmento articleMenmento) {
        articleMenmentos.push(articleMenmento);
    }

    public ArticleMenmento pop() {
        if (!articleMenmentos.isEmpty()) {
            return articleMenmentos.pop();
        }
        return null;
    }

}
 
