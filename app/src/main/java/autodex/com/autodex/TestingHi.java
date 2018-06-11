package autodex.com.autodex;

import android.util.Log;

import java.util.Stack;

/**
 * Created by yasar on 22/5/18.
 */

public class TestingHi {

    private static final String TAG = "TestingHi";

    private Stack<String> stack = new Stack<>();

    public void addValue() {

        stack.add("Karthik");
        stack.add("weee");
        stack.add("sssss");
        stack.add("qqqq");
        stack.add("aaaa");

    }

    public String getValue() {


        return stack.pop();

    }

    public void size() {

        Log.e(TAG, "size: " + stack.size());

    }


}
