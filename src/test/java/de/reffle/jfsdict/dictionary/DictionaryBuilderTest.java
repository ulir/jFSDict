package de.reffle.jfsdict.dictionary;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.reffle.jfsdict.transtable.RichTransTable.ValueType;

public class DictionaryBuilderTest {

  private static Logger LOG = LoggerFactory.getLogger(DictionaryBuilderTest.class);

  Map<String, Integer> someSourceDict;

  public DictionaryBuilderTest() {
  }

  @Before
  public void setUp() throws Exception {
    someSourceDict = new TreeMap<String, Integer>();
    someSourceDict.put(""     , 41);
    someSourceDict.put("anna" , 42);
    someSourceDict.put("anne" , 43);
    someSourceDict.put("bert" , 44);
    someSourceDict.put("berta", 45);
    someSourceDict.put("bohne", 46);
    someSourceDict.put("cäsar", 47);
    someSourceDict.put("resal", 48);
    someSourceDict.put("uli"  , 49);

  }

  @Test
  public void insertAndIterateTrie() {
    DictionaryBuilder trieBuilder = new TrieBuilder();
    insertAndIterate(trieBuilder);
  }

  @Test
  public void insertAndIterateMinDic() {
    DictionaryBuilder minDicBuilder = new MinDicBuilder();
    insertAndIterate(minDicBuilder);
  }

  public void insertAndIterate(DictionaryBuilder aDictBuilder) {
    for(Entry<String, Integer> entry: someSourceDict.entrySet()) {
      aDictBuilder.addWord(entry.getKey(), entry.getValue());
    }
    Dictionary dict = aDictBuilder.finishAndGet();
    Iterator<Entry<String, Integer>> sourceIterator = someSourceDict.entrySet().iterator();
    Iterator<DictEntry> dictIteratorAnn   = dict.iterator(ValueType.ANNOTATION);
    Iterator<DictEntry> dictIteratorIndex = dict.iterator(ValueType.INDEX);

    int expectedIndex = 0;
    while(sourceIterator.hasNext() && dictIteratorAnn.hasNext() && dictIteratorIndex.hasNext()) {
      Entry<String, Integer> nextInSource = sourceIterator.next();
      DictEntry nextInDictAnn  = dictIteratorAnn.next();
      DictEntry nextInDictIndex= dictIteratorIndex.next();
      LOG.trace("nextInSource is {}, nextInDict is {}", nextInSource, nextInDictAnn);
      assertEquals(nextInSource.getKey(), nextInDictAnn.getKey());
      assertEquals(nextInSource.getValue().intValue(), nextInDictAnn.getValue());
      assertEquals(expectedIndex, nextInDictIndex.getValue());
      ++expectedIndex;
    }
    if(sourceIterator.hasNext()) {
      fail("sourceIterator should not hasNext()");
    }
    if(dictIteratorIndex.hasNext()) {
      fail("sourceIterator should not hasNext()");
    }

  }

  @Test
  public void testNonAscii() throws Exception {
    DictionaryBuilder dictionaryBuilder = new MinDicBuilder();
    dictionaryBuilder.addWord("c\u00e4sar");
    Dictionary dict = dictionaryBuilder.finishAndGet();
    assertEquals(6, dict.getNrOfStates());
  }

  @Test
  public void testParseEntry() throws Exception {
    DictEntry entry = DictionaryBuilder.parseEntry("c\u00e4sar#42");
    assertEquals(5, entry.getKey().length());
    assertEquals("(c\u00e4sar,42)", entry.toString());
  }
}
