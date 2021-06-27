package com.java.note.Algorithm.leecode;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/22  15:34
 * @Description 实现一个 Trie (前缀树)，包含 insert, search, 和 startsWith 这三个操作。
 */
public class LeeCode208 {

    class TrieNode {
        // R nexts to node children
        //最多 R 个指向 子结点 的链接，其中每个链接对应字母表数据集中的一个字母。
        private TrieNode[] nexts;
        private final int R = 26;
        private boolean isEnd;

        public TrieNode() {
            nexts = new TrieNode[R];
        }

        //是否指向这个字母
        public boolean containsKey(char ch) {
            return nexts[ch - 'a'] != null;
        }

        public TrieNode get(char ch) {
            return nexts[ch - 'a'];
        }

        public void put(char ch, TrieNode node) {
            nexts[ch - 'a'] = node;
        }

        public void setEnd() {
            isEnd = true;
        }

        public boolean isEnd() {
            return isEnd;
        }
    }


    class Trie {

        private TrieNode root;

        /**
         * Initialize your data structure here.
         */
        public Trie() {
            root = new TrieNode();
        }

        /**
         * Inserts a word into the trie.
         */
        public void insert(String word) {
            char[] words = word.toCharArray();
            TrieNode node = root;
            for (char x : words) {
                if (!node.containsKey(x)) {
                    node.put(x, new TrieNode());
                }
                node = node.get(x);//如果有就找到前缀
            }
            node.setEnd();
        }

        // returns the node where search ends
        private TrieNode searchPrefix(String word) {
            TrieNode node = root;
            for (int i = 0; i < word.length(); i++) {
                char curLetter = word.charAt(i);
                if (node.containsKey(curLetter)) {
                    node = node.get(curLetter);
                } else {
                    return null;
                }
            }
            return node;
        }


        /**
         * Returns if the word is in the trie.
         */
        public boolean search(String word) {
            TrieNode trieNode = searchPrefix(word);
            return trieNode != null && trieNode.isEnd;
        }

        /**
         * Returns if there is any word in the trie that starts with the given prefix.
         * 判断 Trie 中是或有以 prefix 为前缀的单词
         */
        public boolean startsWith(String prefix) {
            TrieNode node = searchPrefix(prefix);
            return node != null;
        }
    }


}
