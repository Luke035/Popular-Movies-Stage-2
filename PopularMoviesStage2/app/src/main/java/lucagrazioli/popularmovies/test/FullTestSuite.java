package lucagrazioli.popularmovies.test;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/***
 * * Created by lucagrazioli on 16/10/15.
 */
public class FullTestSuite extends TestSuite{

    public static Test suite() {
        return new TestSuiteBuilder(FullTestSuite.class)
                        .includeAllPackagesUnderHere().build();
    }

    public FullTestSuite() {
                super();
    }
}
