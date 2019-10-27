package com.application.queryhandler


import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.domain.exception.PollutionCollectorException
import com.domain.pm.model.PM25Report
import com.domain.pm.repository.PMValueRepository
import com.domain.robot.model.RobotCollector
import com.google.maps.model.LatLng
import spock.lang.Specification

class RobotCollectorQueryHandlerSpec extends Specification {
    @Subject
    RobotCollectorQueryHandler robotCollectorQueryHandler
    @Collaborator
    PMValueRepository repository = Mock()


    private static final LatLng ANY_POS = new LatLng(50, 50)
    private static final double STOP_INTERVAL_AS_KM = 0.1D
    private static final int SPEED = 50

    def "should fail when there is no any robot"() {
        when: "request report"
            robotCollectorQueryHandler.getReport()
        then: "fails"
            def ex = thrown(PollutionCollectorException)
            ex.message == "There is not any robot"
    }

    def "should return report response"() {
        given: "an existing robot"
            def robotCollector = RobotCollector.loadRobot("test",
                    ANY_POS,
                    STOP_INTERVAL_AS_KM,
                    SPEED)
            def pmReport = new PM25Report(10)
            repository.getCurrentReport() >> pmReport
        when: "request report"
            def reportResponse = robotCollectorQueryHandler.getReport()
        then: "empty report"
            reportResponse.level == pmReport.level
            reportResponse.location == robotCollector.currentPosition
            reportResponse.source == "Robot"

    }
}
