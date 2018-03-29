package io.pivotal.microservices.pact.consumer;

import au.com.dius.pact.consumer.*;
import au.com.dius.pact.consumer.dsl.*;
import au.com.dius.pact.model.PactFragment;
import au.com.dius.pact.model.RequestResponsePact;
import au.com.dius.pact.model.matchingrules.MatchingRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class ConsumerPortTest extends ConsumerPactTestMk2 {

    @Override
    protected RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");

        DslPart dslPart = new PactDslJsonArray()
                .object()
                .integerType("value", 42)
                .closeObject()
                .object()
                .integerType("value", 100)
                .closeObject();

        return builder.uponReceiving("a request for Foos")
                .path("/foos")
                .method("GET")

                .willRespondWith()
                .headers(headers)
                .status(200)
                .body(dslPart).toPact();
    }

    @Override
    protected String providerName() {
        return "Foo_Provider";
    }

    @Override
    protected String consumerName() {
        return "Foo_Consumer";
    }


    @Override
    protected void runTest(MockServer mockServer) {
        List<Foo> expectedResponse = Arrays.asList(new Foo(42), new Foo(100));
        assertEquals(new ConsumerPort(mockServer.getUrl()).foos(), expectedResponse);
    }
}
