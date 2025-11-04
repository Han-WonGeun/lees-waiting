package waiting.lees_waiting.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

@SuppressWarnings("null")
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loginAdmin") == null) {
            // 로그인되지 않은 사용자
            response.sendRedirect("/admin/login?redirectURL=" + requestURI);
            return false;
        }
        return true;
    }
}
