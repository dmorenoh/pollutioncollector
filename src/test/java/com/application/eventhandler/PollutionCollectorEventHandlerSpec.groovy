package com.application.eventhandler

import com.application.commandhandler.RobotCollectorCommandHandler
import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.domain.robot.command.MoveRobotCommand
import com.domain.robot.model.RobotCollector
import com.domain.pollutioncollector.event.PollutionCollectorStartedEvent
import com.google.maps.model.EncodedPolyline
import com.google.maps.model.LatLng
import spock.lang.Specification

class PollutionCollectorEventHandlerSpec extends Specification {
    @Subject
    PollutionCollectorEventHandler pollutionCollectorEventHandler
    @Collaborator
    RobotCollectorCommandHandler robotCollectorCommandHandler = Mock()

    private static final LatLng ANY_POS = new LatLng(50, 50)
    private static final double STOP_INTERVAL_AS_KM = 0.1D
    private static final int SPEED = 50

    def setup() {
        RobotCollector.clear()
    }

    def "should fail when robot is null"() {
        when: "calling event with robot null"
            pollutionCollectorEventHandler.handle(PollutionCollectorStartedEvent.builder().build())

        then: "fails"
            def ex = thrown(NullPointerException)
            ex.message == "Robot cannot be null"
    }

    def "should fail when robot is not started"() {
        given: "a robot stopped"
            def robotCollector = RobotCollector.loadRobot("test",
                    ANY_POS,
                    STOP_INTERVAL_AS_KM,
                    SPEED)
            robotCollector.stop()
        when: "calling event with robot null"

            pollutionCollectorEventHandler.handle(PollutionCollectorStartedEvent.builder()
                    .robotCollector(robotCollector)
                    .build())

        then: "fails"
            def ex = thrown(IllegalArgumentException)
            ex.message == "Robot is not started"
    }

    def "should fail when empty route"() {
        given: "a robot"
            def robotCollector = RobotCollector.loadRobot("test",
                    ANY_POS,
                    STOP_INTERVAL_AS_KM,
                    SPEED)

        when: "calling event with this robot"
            def event = PollutionCollectorStartedEvent.builder()
                    .robotCollector(robotCollector)
                    .route(Collections.emptyList()).build()
            pollutionCollectorEventHandler.handle(event)

        then: "fails"
            def ex = thrown(IllegalArgumentException)
            ex.message == "route cannot be empty"
    }

    def "should move robot when valid robot and route"() {
        given: "a robot and valid route"
            def robotCollector = RobotCollector.loadRobot("test",
                    ANY_POS,
                    STOP_INTERVAL_AS_KM,
                    SPEED)
            def route = new EncodedPolyline("cpl~F|x|uO{vBtI")
        when: "calling event with this robot and route"
            def event = PollutionCollectorStartedEvent.builder()
                    .robotCollector(robotCollector)
                    .route(route.decodePath()).build()
            pollutionCollectorEventHandler.handle(event)
        then: "move robot along route"
            1 * robotCollectorCommandHandler.handle(new MoveRobotCommand(robotCollector, route.decodePath().get(0)))
            1 * robotCollectorCommandHandler.handle(new MoveRobotCommand(robotCollector, route.decodePath().get(1)))
            robotCollector.isFinished()
    }
}
