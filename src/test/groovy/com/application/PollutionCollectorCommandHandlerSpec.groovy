package com.application

import com.application.commandhandler.PollutionCollectorCommandHandler
import com.application.commandhandler.RobotCollectorCommandHandler
import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.domain.exception.PollutionCollectorException
import com.domain.robot.model.RobotCollector
import com.domain.pollutioncollector.command.ReroutePollutionCollectorCommand
import com.domain.pollutioncollector.command.StartPollutionCollectorCommand
import com.domain.pollutioncollector.event.PollutionCollectorStartedEvent
import com.google.common.eventbus.EventBus
import com.google.maps.model.EncodedPolyline
import com.google.maps.model.LatLng
import spock.lang.Specification

class PollutionCollectorCommandHandlerSpec extends Specification {
    private static final double SPEED_IN_METERS_PER_SECOND = 3.00D
    private static final double STOP_INTERVAL_IN_KM = 0.01D
    @Subject
    private PollutionCollectorCommandHandler commandHandler
    @Collaborator
    private RobotCollectorCommandHandler robotCollectorCommandHandler = Mock()
    @Collaborator
    private EventBus eventBus = Mock()

    private static final LatLng ANY_POS = new LatLng(50, 50)
    private static final double STOP_INTERVAL_AS_KM = 0.1D
    private static final int SPEED = 50


    def setup() {
        RobotCollector.clear()
    }

    def "should fail when trying to start and any robot already exist"() {
        given: " an existing robot"
            RobotCollector.loadRobot("otherRobot", new LatLng(10, 10), 0, 0)

        when: "attempts to start pollution collector"
            commandHandler.handle(new StartPollutionCollectorCommand(
                    UUID.randomUUID().toString(),
                    "anyRobot",
                    SPEED_IN_METERS_PER_SECOND,
                    STOP_INTERVAL_IN_KM,
                    new EncodedPolyline("cpl~F|x|uO{vBtI"))
            )
        then: "fails"
            def ex = thrown(PollutionCollectorException)
            ex.message == "There is a robot already created"
    }

    def "should fail when empty route"() {
        given: " an empty route"
            def emptyRoute = EncodedPolyline.newInstance(Collections.emptyList())

        when: "attempts to start pollution collector with this route"
            commandHandler.handle(new StartPollutionCollectorCommand(
                    UUID.randomUUID().toString(),
                    "anyRobot",
                    SPEED_IN_METERS_PER_SECOND,
                    STOP_INTERVAL_IN_KM,
                    emptyRoute)
            )
        then: "fails"
            def ex = thrown(IllegalArgumentException)
            ex.message == "route cannot be empty"
    }

    def "should initialize robot collector and send event"() {
        given: "a non-empty route"
            def route = new EncodedPolyline("cpl~F|x|uO{vBtI")
        when: "attempts to start pollution collector with this route"
            commandHandler.handle(new StartPollutionCollectorCommand(
                    UUID.randomUUID().toString(),
                    "test",
                    SPEED_IN_METERS_PER_SECOND,
                    STOP_INTERVAL_IN_KM,
                    route)
            )
        then: "robot collector initialized"
            RobotCollector.currentRobot.isPresent()
            RobotCollector.currentRobot.get().speedAsMtrsPerSecond == SPEED_IN_METERS_PER_SECOND
            RobotCollector.currentRobot.get().stopDistanceInterval == STOP_INTERVAL_IN_KM
        and: "pollution collector started event sent"
            1 * eventBus.post(_ as PollutionCollectorStartedEvent)
    }


    def "should fail when reroute and empty route"() {
        when: "attempts to restart"
            commandHandler.handle(new ReroutePollutionCollectorCommand())
        then: "fails"
            def ex = thrown(NullPointerException)
            ex.message == "route cannot be null"
    }

    def "should fail when reroute and empty new route"() {
        given: " an empty route"
            def emptyRoute = EncodedPolyline.newInstance(Collections.emptyList())

        when: "attempts to restart"
            commandHandler.handle(new ReroutePollutionCollectorCommand(emptyRoute))

        then: "fails"
            def ex = thrown(IllegalArgumentException)
            ex.message == "route cannot be empty"
    }

    def "should fail when reroute and there is no any existing robot"() {
        given: "a non-empty route"
            def route = new EncodedPolyline("cpl~F|x|uO{vBtI")
        when: "attempts to restart"
            commandHandler.handle(new ReroutePollutionCollectorCommand(route))
        then: "fails"
            def ex = thrown(PollutionCollectorException)
            ex.message == "There is a not any robot created"
    }

    def "should restart when there is an existing robot and new valid route"() {
        given: "an existing robot"
            RobotCollector.loadRobot("existingRobot", ANY_POS, STOP_INTERVAL_AS_KM, SPEED)
        and: "valid route"
            def route = new EncodedPolyline("cpl~F|x|uO{vBtI")
        when: "attempts to restart"
            commandHandler.handle(new ReroutePollutionCollectorCommand(route))
        then: "restarts"
            def robot = RobotCollector.getCurrentRobot().get()
            robot.currentPosition == route.decodePath().get(0)
            1 * eventBus.post(PollutionCollectorStartedEvent.builder().robotCollector(RobotCollector.getCurrentRobot().get()).route(route.decodePath()).build())
    }
}
