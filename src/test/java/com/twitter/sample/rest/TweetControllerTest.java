package com.twitter.sample.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.sample.Application;
import com.twitter.sample.domain.Tweet;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TweetControllerTest {

    private static final String RESOURCE_LOCATION_PATTERN = "http://localhost/api/v1/tweets/[0-9]+";

    @InjectMocks
    TweetRestController controller;

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    //@Test
    public void shouldHaveEmptyDB() throws Exception {
        mvc.perform(get("/api/v1/tweets")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldCreateRetrieveDelete() throws Exception {
        Tweet r1 = mockTweet("shouldCreateRetrieveDelete");
        byte[] r1Json = toJson(r1);
        //CREATE
        MvcResult result = mvc.perform(post("/api/v1/tweets")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern(RESOURCE_LOCATION_PATTERN))
                .andReturn();
        
        long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());
        assertTrue(id > 0);

        //RETRIEVE
        mvc.perform(get("/api/v1/tweets/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tweetId", is((int) id)))
                .andExpect(jsonPath("$.employeeId", is(r1.getEmployeeId())))
                .andExpect(jsonPath("$.tweetContent", is(r1.getTweetContent())));

        //DELETE
        mvc.perform(delete("/api/v1/tweets/" + id)
        			.accept(MediaType.APPLICATION_JSON))
        			.andExpect(status().isNoContent());

        //RETRIEVE should fail
        mvc.perform(get("/api/v1/tweets/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        
    }

    @Test
    public void shouldCreateAndUpdateAndDelete() throws Exception {
        Tweet r1 = mockTweet("shouldCreateAndUpdate");
        byte[] r1Json = toJson(r1);
        //CREATE
        MvcResult result = mvc.perform(post("/api/v1/tweets")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern(RESOURCE_LOCATION_PATTERN))
                .andReturn();
        
        
        long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());
        
        assertTrue(id > 0);

        Tweet r2 = mockTweet("shouldCreateAndUpdate2");
        r2.setTweetId(id);
        byte[] r2Json = toJson(r2);

        //UPDATE
        result = mvc.perform(put("/api/v1/tweets/" + id)
                .content(r2Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        //RETRIEVE updated
        mvc.perform(get("/api/v1/tweets/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tweetId", is((int) id)))
                .andExpect(jsonPath("$.employeeId", is(r2.getEmployeeId())))
                .andExpect(jsonPath("$.tweetContent", is(r2.getTweetContent())));

        //DELETE
        mvc.perform(delete("/api/v1/tweets/" + id))
                .andExpect(status().isNoContent());
    }


    private long getResourceIdFromUrl(String locationUrl) {
        String[] parts = locationUrl.split("/");
        return Long.valueOf(parts[parts.length - 1]);
    }


    private Tweet mockTweet(String prefix) {
    		Tweet r = new Tweet();
        r.setEmployeeId(new Random().nextInt(6));
        r.setTweetContent(prefix + "_tweetContent");
        return r;
    }

    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }

    private static ResultMatcher redirectedUrlPattern(final String expectedUrlPattern) {
        return new ResultMatcher() {
            public void match(MvcResult result) {
                Pattern pattern = Pattern.compile("\\A" + expectedUrlPattern + "\\z");
                System.out.println("URL:" +result.getResponse().getRedirectedUrl());
                assertTrue(pattern.matcher(result.getResponse().getRedirectedUrl()).find());
            }
        };
    }

}
