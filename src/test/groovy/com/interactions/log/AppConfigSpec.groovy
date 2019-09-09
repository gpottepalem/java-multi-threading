package com.interactions.log

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

/**
 * Specification for {@link AppConfig}
 *
 * @author gpottepalem
 * Created on Sep 07, 2019
 */
class AppConfigSpec extends Specification {

    @Subject
    @Shared
    AppConfig appConfig = AppConfig.instance

    void "AppConfig is singleton"() {
        expect: 'getInstance method returns same object instance when called multiple times'
        appConfig == AppConfig.instance
    }

    void "AppConfig holds right properties read from application properties file"() {
        expect: 'appConfig instance is not null'
        appConfig

        and: 'appConfig is populated and has properties set with right values'
        appConfig.numberOfCidAWriters == 2
        appConfig.numberOfCidBWriters == 3
        appConfig.commitLogFileName == 'commit.log'
    }

}
