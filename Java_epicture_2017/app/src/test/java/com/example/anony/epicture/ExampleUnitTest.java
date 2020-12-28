package com.example.anony.epicture;

import com.example.anony.epicture.Imgur;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void unitTestImgur() throws Exception {
        Imgur imgur = new Imgur();
        String idClient = "Hello World";
        assertEquals(imgur.getAuthUrl(idClient), "https://api.imgur.com/oauth2/authorize?client_id=" + idClient +
                "&response_type=token&state=AUTH");
        assertEquals(imgur.describeContents(), 0);
        assertEquals(imgur.getAccessToken(), imgur.accessToken);
        assertEquals(imgur.getRefreshToken(), imgur.refreshToken);
        assertEquals(imgur.getAccountId(), imgur.accountId);
        assertEquals(imgur.getAccountUsername(), imgur.accountUsername);
        assertEquals(imgur.getIdClient(), imgur.idClient);
    }
}