package code.config.sign;

import cn.hutool.core.text.CharSequenceUtil;
import code.config.exception.BusinessException;
import code.util.SignToolKit;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class SignAspect {
	/**
	 * SIGN_HEADER.
	 */
	private static final String SIGN_HEADER = "X-SIGN";

	/**
	 * This method is an aspect that verifies the signature of an HTTP request.
	 * It checks if the signature is present in the request header and compares it with the generated signature.
	 * If the signature is invalid, it throws a BusinessException.
	 */
	@Before("verifySignPointCut()")
	public void verify() {
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		String sign = request.getHeader(SIGN_HEADER);

		// must have sign in header
		if (CharSequenceUtil.isBlank(sign)) {
			throw new BusinessException("no signature in header: " + SIGN_HEADER);
		}

		// check signature
		try {
			String generatedSign = generatedSignature(request);
			if (!sign.equals(generatedSign)) {
				throw new BusinessException("invalid signature");
			}
		} catch (Throwable throwable) {
			throw new BusinessException("invalid signature");
		}
	}

	/**
	 * pointcut.
	 */
	@Pointcut("execution(@code.config.sign.Signature * *(..))")
	private void verifySignPointCut() {
		// nothing
	}

	/**
	 * Generates the signature of an HTTP request.
	 *
	 * @param request the HTTP request
	 *
	 * @return the generated signature
	 */
	private String generatedSignature(HttpServletRequest request) {
		// @RequestBody
		String bodyParam = null;
		if (request instanceof ContentCachingRequestWrapper) {
			bodyParam = new String(((ContentCachingRequestWrapper) request).getContentAsByteArray(), StandardCharsets.UTF_8);
		}

		// @RequestParam
		Map<String, String[]> requestParameterMap = request.getParameterMap();

		// @PathVariable
		String[] paths = null;
		ServletWebRequest webRequest = new ServletWebRequest(request, null);
		@SuppressWarnings("unchecked")
		Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		if (!CollectionUtils.isEmpty(uriTemplateVars)) {
			paths = uriTemplateVars.values().toArray(new String[0]);
		}
		return SignToolKit.sign(bodyParam, requestParameterMap, paths);
	}
}