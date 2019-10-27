package com.ports

import com.application.commandhandler.PollutionCollectorCommandHandler
import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import spock.lang.Unroll

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given

class PollutionCollectorCommandControllerSpec extends Specification {
    private static final String REQUEST_WITHOUT_ROUTE = "{\n" +
            "    \"robotName\":\"test\",\n" +
            "    \"speed\":10.00\n" +
            "}"
    private static final String REQUEST_WITHOUT_ROBOT = "{\n" +
            "    \"speed\":10.00,\n" +
            "    \"polylineEncoded\":\"uim~Ftd}uO}`@wB\"\n" +
            "}"
    private static final String VALID_REQUEST = "{\n" +
            "    \"robotName\":\"test\",\n" +
            "    \"speed\":10.00,\n" +
            "    \"polylineEncoded\":\"uim~Ftd}uO}`@wB\"\n" +
            "}"

    @Subject
    PollutionCollectorCommandController controller
    @Collaborator
    PollutionCollectorCommandHandler commandHandler = Mock()

    def setup() {
        RestAssuredMockMvc.standaloneSetup(MockMvcBuilders.standaloneSetup(controller))
    }

    @Unroll
    def "should return #responseValue when request is #requestValue"() {
        given: "a invalid request"
            def request = given().body(requestValue).contentType(ContentType.JSON)
        when: "request start pollution collection"
            def response = request.when().post("/myapp/start")
        then: "fails"
            response.then().statusCode(responseValue)

        where:
            requestValue          | responseValue
            REQUEST_WITHOUT_ROUTE | HttpStatus.BAD_REQUEST.value()
            REQUEST_WITHOUT_ROBOT | HttpStatus.BAD_REQUEST.value()
    }

    def "should send command when valid request"() {
        given: "a invalid request"
            def request = given().body(VALID_REQUEST).contentType(ContentType.JSON)
        when: "request start pollution collection"
            def response = request.when().post("/myapp/start")
        then: "fails"
            response.then().statusCode(HttpStatus.ACCEPTED.value())

    }
}
