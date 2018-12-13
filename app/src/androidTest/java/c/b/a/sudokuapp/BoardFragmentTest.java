package c.b.a.sudokuapp;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.*;

public class BoardFragmentTest {

    private MockWebServer easyserver;

    @Rule
    public final ActivityTestRule<GameActivity> gActivity = new ActivityTestRule<>(GameActivity.class, false, false);

    @Before
    public void init() throws IOException {
        easyserver = new MockWebServer();
        easyserver.start();

        String easy = "{\"board\":[[0,0,8,0,3,0,1,0,0],[0,2,0,0,7,0,0,0,0],[5,0,0,0,6,0,0,3,0],[0,0,0,3,5,0,7,8,9],[0,0,0,0,0,7,4,0,0],[8,9,7,0,0,4,0,5,6],[4,0,2,0,0,1,0,7,5],[7,0,0,9,0,5,8,0,3],[9,0,5,7,4,3,6,0,0]]}";
        easyserver.enqueue(
                new MockResponse()
                        .setBody(easy)
                        .setResponseCode(200)
                        .setHeader("Access-Control-Allow-Credentials", "true")
                        .setHeader("Access-Control-Allow-Headers", "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization, accept, origin, Cache-Control, X-Requested-With")
                        .setHeader("Access-Control-Allow-Methods","POST, OPTIONS, GET, PUT")
                        .setHeader("Access-Control-Allow-Origin","*")
                        .setHeader("Connection", "keep-alive")
                        .setHeader("Content-Length", "192")
                        .setHeader("Content-Type","application/json; charset=utf-8")
                        .setHeader("Date", "Thu, 13 Dec 2018 16:47:08 GMT")
                        .setHeader("Server", "Cowboy")
                        .setHeader("Via", "1.1 vegur")
        );

    }

    @Test
    public void generateNewGame() {
    }

    @Test
    public void saveNewGame() {
    }

    @Test
    public void getSolution() {
    }

    @Test
    public void saveSolution() {
    }
}