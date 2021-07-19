package cn.gof.composite.file;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 17:30
 * @description:
 * @question:
 * @link:
 **/
public class FileDemo {
    public static void main(String[] args) {
        /**
         * /
         *  /wz/
         *  /wz/a.txt
         *  /wz/b.txt
         *  /wz/movies/
         *  /wz/movies/c.avi
         *  /xzg/
         *  /xzg/docs/
         *  /xzg/docs/d.txt
         */
        Directory fileSystemTree = new Directory("/");
        Directory node_wz = new Directory("/wz/");
        Directory node_xzg = new Directory("/xzg/");
        fileSystemTree.addSubNode(node_wz);
        fileSystemTree.addSubNode(node_xzg);
        FileNode node_wz_a = new FileNode("/wz/a.txt");
        FileNode node_wz_b = new FileNode("/wz/b.txt");
        Directory node_wz_movies = new Directory("/wz/movies/");
        node_wz.addSubNode(node_wz_a);
        node_wz.addSubNode(node_wz_b);
        node_wz.addSubNode(node_wz_movies);
        FileNode node_wz_movies_c = new FileNode("/wz/movies/c.avi");
        node_wz_movies.addSubNode(node_wz_movies_c);

        Directory node_xzg_docs = new Directory("/xzg/docs/");
        node_xzg.addSubNode(node_xzg_docs);
        FileNode node_xzg_docs_d = new FileNode("/xzg/docs/d.txt");
        node_xzg_docs.addSubNode(node_xzg_docs_d);

        System.out.println("/ files num:" + fileSystemTree.countNumOfFiles());
        System.out.println("/wz/ files num:" + node_wz.countNumOfFiles());
    }
}
 
