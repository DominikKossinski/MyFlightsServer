package pl.kossa.myflightsserver.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.io.FileInputStream

@Service
class FirebaseConfig {

    @Primary
    @Bean
    fun firebaseInit() {
        val options = FirebaseOptions.builder()
        val serviceAccount = FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"))
        options.setCredentials(GoogleCredentials.fromStream(serviceAccount))
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options.build())
        }
    }
}