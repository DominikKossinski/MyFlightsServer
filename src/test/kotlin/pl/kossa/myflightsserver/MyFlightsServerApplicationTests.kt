package pl.kossa.myflightsserver

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(classes = [FirebaseConfigTest::class])
class MyFlightsServerApplicationTests {

    @Test
    fun contextLoads() {
    }

}
