package SystemDateStrategy;

import android.content.Context;

/**
 * Created by Charlotte on 19/03/2017.
 */

public class ContextDate {
    private SystemDate systemDate;

    public ContextDate(SystemDate systemDate) {
        this.systemDate = systemDate;
    }

    public String executeStrategy(Context context) {
        return systemDate.setSystemDate(context);
    }

}
