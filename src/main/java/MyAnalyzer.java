import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.commongrams.CommonGramsFilter;
import org.apache.lucene.analysis.commongrams.CommonGramsQueryFilter;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.analysis.en.*;
import org.apache.lucene.analysis.miscellaneous.HyphenatedWordsFilter;
import org.apache.lucene.analysis.miscellaneous.KeepWordFilter;
import org.apache.lucene.analysis.miscellaneous.RemoveDuplicatesTokenFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.wikipedia.WikipediaTokenizer;

import java.util.regex.Pattern;

public class MyAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String s) {
        Tokenizer tokenizer = new StandardTokenizer();
        TokenStream filter = new RemoveDuplicatesTokenFilter(tokenizer);
        filter = new LowerCaseFilter(tokenizer);
        filter = new EnglishMinimalStemFilter(filter);
        filter = new EnglishPossessiveFilter(filter);
        filter = new PorterStemFilter(filter);
        filter = new KStemFilter(filter);
        filter = new StopFilter(filter, EnglishAnalyzer.getDefaultStopSet());
        return new TokenStreamComponents(tokenizer, filter);
    }
}
