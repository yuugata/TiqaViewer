package jp.gr.java_conf.tiqaviewer;

import android.content.Intent;
import static android.app.SearchManager.QUERY;
import static android.content.Intent.ACTION_SEARCH;
import android.test.ActivityInstrumentationTestCase2;

/**
 */
public class SearchResultActivityTest extends ActivityInstrumentationTestCase2<SearchResultActivity> {
    public SearchResultActivityTest(){
        super(SearchResultActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityIntent(new Intent(ACTION_SEARCH).putExtra(QUERY, "ちくわ"));
    }

}
