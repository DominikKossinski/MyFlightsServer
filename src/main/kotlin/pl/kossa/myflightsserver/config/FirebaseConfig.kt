package pl.kossa.myflightsserver.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.StorageOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.io.FileInputStream

@Service
class FirebaseConfig {

    @Bean
    @Primary
    fun firebaseInit() {
        val options = FirebaseOptions.builder()
        val serviceAccount = FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"))
        options.setCredentials(GoogleCredentials.fromStream(serviceAccount))
        options.setStorageBucket("testserver-6e4b4.appspot.com")
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options.build())
        }
    }

    @Bean
    fun storageOptions(): StorageOptions {
        val options = StorageOptions.newBuilder()
        val serviceAccount = FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"))
        options.setCredentials(GoogleCredentials.fromStream(serviceAccount))
        return options.build()
    }

}