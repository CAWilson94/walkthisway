package SystemDateStrategy;

import android.content.Context;

/**
 * Created by Charlotte on 19/03/2017.
 */

public class SystemDateManager {

    SystemDatePreferenceManager sdpm = new SystemDatePreferenceManager();

    public String systemDateDecider(Context context) {
        String boop = null;
        ContextDate contextDate;
        if (sdpm.sharedPrefTestMode(context)) {
            contextDate = new ContextDate(new TestModeSystemDate());
        } else {
            contextDate = new ContextDate(new MainModeSystemDate());
        }
        boop = contextDate.executeStrategy(context);
        return boop;
    }
}
