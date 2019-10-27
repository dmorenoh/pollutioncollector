package com.application

import com.application.commandhandler.RobotCollectorCommandHandler
import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.domain.exception.PollutionCollectorException
import com.domain.pm.repository.InMemoryPMValueRepository
import com.domain.pm.repository.PMValueRepository
import com.domain.robot.command.MoveRobotCommand
import com.domain.robot.model.RobotCollector
import com.domain.utils.DistanceCalculator
import com.google.maps.model.LatLng
import spock.lang.Specification

class RobotCollectorCommandHandlerSpec extends Specification {

    private static final double STOP_INTERVAL_AS_KM = 0.3D
    private static final int SPEED = 50
    @Subject
    RobotCollectorCommandHandler defaultRobotCollectorService

    @Collaborator
    PMValueRepository pmValueRepository = new InMemoryPMValueRepository()

    private static final LatLng ANY_POS = new LatLng(50, 50)
    //private static final LatLng START_POS = new LatLng(41.86717, -87.64460)
    private static final LatLng START_POS = new LatLng(41.375070, 2.156412)
    private static final LatLng END_POS = new LatLng(41.375096, 2.158134)
    //private static final LatLng END_POS = new LatLng(41.86684, -87.64472)
    private static final LatLng FAR_END_POS = new LatLng(41.375062, 2.160966)
    //private static final LatLng FAR_END_POS = new LatLng(41.86339, -87.64443)

    def setup() {
        RobotCollector.clear()
    }

    def "should do nothing when robot current position is equal to next position"() {
        given: "robot in a specific position"
            def robotCollector = RobotCollector.loadRobot("test",
                    ANY_POS,
                    STOP_INTERVAL_AS_KM,
                    SPEED)

        when: "asking to move robot to position same as current robot position"
            defaultRobotCollectorService.handle(new MoveRobotCommand(robotCollector, ANY_POS))

        then: "do nothing"
            0 * pmValueRepository.save(_)
            robotCollector.currentPosition == ANY_POS
            robotCollector.distanceWithoutStopping == 0
    }

    def "should move wit no-stop when distance to next stop is shorter than stop interval"() {
        given: "robot in a given position"
            def robotCollector = RobotCollector
                    .loadRobot("test",
                    START_POS,
                    STOP_INTERVAL_AS_KM,
                    SPEED)

        and: "distance to next position is closer than stop distance interval"
        expect: "distance to next position is closer than stop distance interval"
            def distance = DistanceCalculator.distance(START_POS, END_POS)
            distance < robotCollector.stopDistanceInterval
        when: "asking to move robot to position same as current robot position"
            defaultRobotCollectorService.handle(new MoveRobotCommand(robotCollector, END_POS))
        then: "move without stopping"
            0 * pmValueRepository.save(_)
            robotCollector.currentPosition == END_POS
            robotCollector.distanceWithoutStopping == distance
    }

    def "should move and stop to read data when stops found between current position and next position"() {
        given: "robot in a given position"
            def robotCollector = RobotCollector.loadRobot("test",
                    START_POS, STOP_INTERVAL_AS_KM, SPEED)
        expect: "distance to next position is closer than stop distance interval"
            def distance = DistanceCalculator.distance(START_POS, FAR_END_POS)
            distance >= robotCollector.stopDistanceInterval
        and: "a certain number of stops between"
            def expectedStops = (int) (distance / STOP_INTERVAL_AS_KM)

        when: "asking to move robot to position same as current robot position"
            defaultRobotCollectorService.handle(new MoveRobotCommand(robotCollector, FAR_END_POS))
        then: "move without stopping"
            pmValueRepository.getAll().size() == expectedStops
            robotCollector.currentPosition == FAR_END_POS
            robotCollector.distanceWithoutStopping > 0
    }

    def "should fail when robot is stopped"() {
        given: "robot in a given position"
            def robotCollector = RobotCollector.loadRobot("test",
                    START_POS, STOP_INTERVAL_AS_KM, SPEED)
            robotCollector.stop()
        when: "asking to move robot to position same as current robot position"
            defaultRobotCollectorService.handle(new MoveRobotCommand(robotCollector, FAR_END_POS))
        then: "fails"
            def ex = thrown(PollutionCollectorException)
    }
}
