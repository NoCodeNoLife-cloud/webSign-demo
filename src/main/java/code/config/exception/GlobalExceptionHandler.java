package code.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	/**
	 * handle business exception.
	 *
	 * @param businessException businessException
	 *
	 * @return ResponseResult
	 */
	@ResponseBody
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<BusinessException> processBusinessException(BusinessException businessException) {
		log.error(businessException.getLocalizedMessage());
		return new ResponseEntity<>(businessException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * handle exception.
	 *
	 * @param exception exception
	 *
	 * @return ResponseResult
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Exception> processException(Exception exception) {
		log.error(exception.getLocalizedMessage(), exception);
		return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
