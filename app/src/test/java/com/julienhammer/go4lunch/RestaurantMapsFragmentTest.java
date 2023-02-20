package com.julienhammer.go4lunch;

import static org.mockito.Mockito.spy;

import com.julienhammer.go4lunch.ui.map.RestaurantMapsFragment;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
@RunWith(MockitoJUnitRunner.class)
public class RestaurantMapsFragmentTest {

    @Test
    public void getOpenHourTextTest() throws Exception {

        RestaurantMapsFragment fragment = spy(RestaurantMapsFragment.class);
        Assert.assertEquals(R.string.open_now_case_true, fragment.getOpenHourTextId(true));
        Assert.assertEquals(R.string.open_now_case_false, fragment.getOpenHourTextId(false));
        Assert.assertEquals(R.string.open_now_case_not_showing, fragment.getOpenHourTextId(null));


    }
}