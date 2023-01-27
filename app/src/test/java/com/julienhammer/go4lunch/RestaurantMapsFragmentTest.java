package com.julienhammer.go4lunch;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.julienhammer.go4lunch.ui.MainActivity;
import com.julienhammer.go4lunch.ui.map.RestaurantMapsFragment;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
@RunWith(MockitoJUnitRunner.class)
public class RestaurantMapsFragmentTest {

    @Test
    public void getOpenHourTextTest() throws Exception {

        RestaurantMapsFragment fragment = spy(RestaurantMapsFragment.class);
        Assert.assertEquals(R.string.openNowCaseTrue, fragment.getOpenHourTextId(true));
        Assert.assertEquals(R.string.openNowCaseFalse, fragment.getOpenHourTextId(false));
        Assert.assertEquals(R.string.openNowCaseNotShowing, fragment.getOpenHourTextId(null));


    }
}