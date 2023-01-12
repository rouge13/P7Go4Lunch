package com.julienhammer.go4lunch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.julienhammer.go4lunch.data.restaurants.InfoRestaurantRepository;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.Collections;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(MockitoJUnitRunner.class)
public class InfoRestaurantRepositoryTest {

    @Spy
    private InfoRestaurantRepository repository;

        @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(InfoRestaurantRepository.class);
//        repository = mock(InfoRestaurantRepository.class);
//
//    }

//    @Test
//    public void casesOfStars() {
//        int size = 3;
//        ArrayList<Boolean> mockListRating = new ArrayList<>();
//        mockListRating.addAll(Collections.nCopies(size, Boolean.FALSE));
//        //        double rating = 1.1;
////
////        repository.casesOfStars(rating);
////        Assert.assertFalse(list.get(0));
////        Assert.assertFalse(list.get(1));
////        Assert.assertFalse(list.get(2));
////        Assert.assertNull(list.get(3));
////        resultFromRepository.addAll(doReturn().when(repository.checkStarsFromRating(1.1)));
//        ArrayList<Boolean> resultFromRepository = new ArrayList<>(doReturn(repository.checkStarsFromRating(1.1)).when(repository).checkStarsFromRating(1.1));
//        Assert.assertEquals(mockListRating, resultFromRepository);
//    }


}