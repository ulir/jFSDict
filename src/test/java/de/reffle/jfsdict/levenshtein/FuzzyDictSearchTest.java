package de.reffle.jfsdict.levenshtein;

import org.junit.Test;

import de.reffle.jfsdict.dictionary.Trie;
import de.reffle.jfsdict.dictionary.TrieBuilder;
import de.reffle.jfsdict.util.test.Tests;

public class FuzzyDictSearchTest {
  @Test
  public void test() throws Exception {
    Trie trie = getTrie();

    FuzzyDictSearch fuzzyDictSearch = new FuzzyDictSearch(trie);

    MatchReceiverList matchReceiver = new MatchReceiverList();

    fuzzyDictSearch.query("anne"   , 1, matchReceiver);
    Tests.assertStringEquals("[anna, 1, 13, anne, 0, 14]", matchReceiver.toString());

    matchReceiver.clear();
    fuzzyDictSearch.query("annx"   , 1, matchReceiver);
    Tests.assertStringEquals("[anna, 1, 13, anne, 1, 14]", matchReceiver);

    matchReceiver.clear();
    fuzzyDictSearch.query("bertram", 2, matchReceiver);
    Tests.assertStringEquals("[berta, 2, 15, bertram, 0, 16]", matchReceiver);

    matchReceiver.clear();
    fuzzyDictSearch.query("rtram", 2, matchReceiver);
    Tests.assertStringEquals("[bertram, 2, 16]", matchReceiver);

    matchReceiver.clear();
    fuzzyDictSearch.query("a", 3, matchReceiver);
    Tests.assertStringEquals("[anna, 3, 13, anne, 3, 14]", matchReceiver);

    matchReceiver.clear();
    fuzzyDictSearch.query("bxxa", 3, matchReceiver);
    Tests.assertStringEquals("[anna, 3, 13, berta, 3, 15]", matchReceiver);

  }

  public Trie getTrie() throws Exception {
    TrieBuilder trieBuilder = new TrieBuilder();
    trieBuilder.addWord("anna", 13);
    trieBuilder.addWord("anne", 14);
    trieBuilder.addWord("berta", 15);
    trieBuilder.addWord("bertram", 16);
    trieBuilder.addWord("otto", 17);
    return trieBuilder.finishAndGet();
  }

 }
