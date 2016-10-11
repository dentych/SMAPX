package group.smapx.remindalot;

import org.junit.Test;

import group.smapx.remindalot.Create.AddressValidator;
import group.smapx.remindalot.Location.TravelManager;
import group.smapx.remindalot.model.LocationData;
import group.smapx.remindalot.model.TravelInfo;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TravelInfoTest {
    @Test
    public void addition_isCorrect() throws Exception {

        AddressValidator validator = new AddressValidator();
        validator.validate("Ekkodalen 4 8210");
        LocationData from = validator.getLocationData();
        validator.validate("Sletvej 30 tranbjerg");
        LocationData to = validator.getLocationData();

        TravelManager travelManager = new TravelManager();
        TravelInfo travelInfo = travelManager.getTravelInfo(TravelInfo.TravelType.PUBLIC_TRANSPORTATION,from,to);

        System.out.println("Distance [m]: " + travelInfo.getDistance());
        System.out.println("Time [s]: " + travelInfo.getSecondsOfTravel());


    }
}