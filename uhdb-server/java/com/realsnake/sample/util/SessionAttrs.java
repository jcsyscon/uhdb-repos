package com.realsnake.sample.util;

import javax.servlet.http.HttpSession;

public class SessionAttrs {

    public static Long getUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }

    public static void setUserId(HttpSession session, Long userId) {
        session.setAttribute("userId", userId);
    }

}
