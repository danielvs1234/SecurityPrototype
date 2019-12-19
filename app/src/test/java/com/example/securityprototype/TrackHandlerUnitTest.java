package com.example.securityprototype;

import android.content.Context;
import android.location.LocationManager;
import android.os.BatteryManager;

import com.example.securityprototype.Interfaces.IEncryption;
import com.example.securityprototype.Interfaces.IStorage;
import com.example.securityprototype.Model.Track;
import com.example.securityprototype.Model.TrackHandler;
import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrackHandlerUnitTest {


    @Captor
    private ArgumentCaptor<Map<String, byte[]>> captor;
    private static Context context;
    private static BatteryManager batmanager;
    @BeforeAll
    public void before(){
        context = mock(Context.class);
        batmanager = mock(BatteryManager.class);
        Mockito.when(context.getSystemService(Context.BATTERY_SERVICE)).thenReturn(batmanager);
        MockitoAnnotations.initMocks(this);
    }

    @Nested
    @DisplayName("saveTrackArrayToStorage")
    class saveTrackArrayToStorage {
        @Test
        @DisplayName("Should crease a new save when the application has never been run before")
        public void shouldParseCorrectByteWhenNeverSavedBefore() throws IOException {
            final IEncryption encryptionHandler = mock(IEncryption.class);
            final IStorage storageHandler = mock(IStorage.class);
            ArrayList<Track> list = new ArrayList<Track>(){{add(new Track(new LatLng(1.0, 2.0)));}};

            TrackHandler handler = new TrackHandler(context);
            handler.setStorageHandler(storageHandler);
            handler.setEncryptionHandler(encryptionHandler);
            handler.setTempTrackArrayList(list);
            Mockito.when(storageHandler.checkIfFileExists()).thenReturn(false);
            byte[] expectedBytes = handler.getByteArrayOfTracks(list);

            Map<String, byte[]> expectedMap = new HashMap<>();
            expectedMap.put("data", expectedBytes);
            Mockito.when(encryptionHandler.encryptBytes(expectedBytes)).thenReturn(expectedMap);
            handler.saveTrackArrayToStorage();
            verify(storageHandler, times(1)).write(captor.capture(), anyString());
            assertThat(captor.getValue(), hasEntry("data", expectedBytes));
        }
    }
}
