package pl.kossa.myflightsserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import pl.kossa.myflightsserver.localization.NotificationsMessageSource
import java.util.*
import javax.servlet.http.HttpServletRequest

@Configuration
class LocalizationConfig {

    @Bean
    fun notificationsMessageSource(): NotificationsMessageSource {
        return NotificationsMessageSource()
    }

    @Bean
    fun localeResolver(): LocaleResolver {
        return HeaderLanguageResolver()
    }

    class HeaderLanguageResolver : AcceptHeaderLocaleResolver() {
        init {
            supportedLocales = listOf(Locale.US, Locale("pl", "PL"), Locale.GERMANY)
            defaultLocale = Locale.US
        }

        override fun resolveLocale(request: HttpServletRequest): Locale {
            val language = request.getHeader("Accept-Language") ?: return Locale.US
            return supportedLocales.find { it.toString() == language } ?: Locale.US
        }
    }

}