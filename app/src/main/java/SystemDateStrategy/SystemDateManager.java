package SystemDateStrategy;

/**
 * Created by Charlotte on 19/03/2017.
 */

public final class SystemDateManager {

    SystemDatePreferenceManager sdpm = new SystemDatePreferenceManager();

    public void systemDateDecider() {
        if(sdpm){

        }
        else{

        }
        ContextDate context = new ContextDate(new MainModeSystemDate());
        context = new ContextDate(new TestModeSystemDate());
    }
}
