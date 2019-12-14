package com.uet.ooadloophole.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieService {
    public static Cookie updateCookie(HttpServletRequest request, String cookieName, String cookieValue, String path) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    cookie.setValue(cookieValue);
                    if (path != null) cookie.setPath(path);
                    return cookie;
                }
            }
        }
        return new Cookie(cookieName, cookieValue) {{
            setPath(path);
        }};
    }
}
