package code.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
public class RequestCachingFilter extends OncePerRequestFilter {
	/**
	 * Overrides the method to perform filtering on the request and response.
	 * If it's the first request and the request is not already a ContentCachingRequestWrapper,
	 * wraps the request in a ContentCachingRequestWrapper before passing it to the filter chain.
	 *
	 * @param request     the HTTP request
	 * @param response    the HTTP response
	 * @param filterChain the filter chain to proceed with filtering
	 */
	@Override
	@SneakyThrows
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
		boolean isFirstRequest = !isAsyncDispatch(request);
		HttpServletRequest requestWrapper = request;

		// Wrap the request in ContentCachingRequestWrapper if it's the first request and not already wrapped
		if (isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
			requestWrapper = new ContentCachingRequestWrapper(request);
		}

		try {
			// Proceed with the filter chain using the modified request and response
			filterChain.doFilter(requestWrapper, response);
		} catch (Exception e) {
			// Log and handle any exceptions that occur during filtering
			log.error(e.getMessage(), e);
		}
	}
}
