package pl.kossa.myflightsserver.extensions

import org.springframework.web.util.WebUtils
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest


fun HttpServletRequest.getCookie(name: String): Cookie? {
    return WebUtils.getCookie(this, name)
}