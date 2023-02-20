package com.julienhammer.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.julienhammer.go4lunch.data.workmate.WorkmateRepository;
import com.julienhammer.go4lunch.models.User;
import com.julienhammer.go4lunch.viewmodel.WorkmateViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */

@RunWith(MockitoJUnitRunner.class)
public class WorkmateViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private WorkmateRepository workmateRepository;
    @Mock
    private WorkmateViewModel workmateViewModel;
    @Mock
    private Observer<List<User>> mWorkmatesObserver;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        workmateRepository = mock(WorkmateRepository.class);
        workmateViewModel = new WorkmateViewModel(workmateRepository);
    }

    @Test
    public void constructor(){
        assertNotNull(workmateRepository);
        assertNotNull(workmateViewModel);
    }

    @Test
    public void testGetWorkmates(){
        // Given
        List<User> expectedWorkmates = new ArrayList<>();
        ArrayList<String> listOfRestaurantsLikes = new ArrayList<>();
        listOfRestaurantsLikes.add("rr22re");
        listOfRestaurantsLikes.add("zz33te");
        User user1 = new User("123","user1","user1@gmail.com","zz11re",null,listOfRestaurantsLikes);
        User user2 = new User("456","user2","user2@gmail.com","zz11re",null,listOfRestaurantsLikes);
        expectedWorkmates.add(user1);
        expectedWorkmates.add(user2);
        MutableLiveData<List<User>> workmatesMutableLiveData = new MutableLiveData<>();
        workmatesMutableLiveData.postValue(expectedWorkmates);
        given(workmateRepository.getWorkmates()).willReturn(workmatesMutableLiveData);

        // When
        workmateViewModel.getWorkmates().observeForever(mWorkmatesObserver);

        // THen
        verify(workmateRepository).getWorkmates();
        verify(mWorkmatesObserver).onChanged(expectedWorkmates);
        assertEquals(expectedWorkmates,workmateRepository.getWorkmates().getValue());
    }


}
