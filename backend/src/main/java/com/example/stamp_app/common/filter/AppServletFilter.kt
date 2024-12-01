package com.example.stamp_app.common.filter

import com.example.stamp_app.common.handler.AppFinalExceptionHandler
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.IOException
import java.util.*

@Component
class AppServletFilter : OncePerRequestFilter() {

	companion object {
		private val log = LoggerFactory.getLogger(AppFinalExceptionHandler::class.java)
	}

	@Throws(ServletException::class, IOException::class)
	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
		val requestWrapper = ContentCachingRequestWrapper(request)
		val responseWrapper = ContentCachingResponseWrapper(response)

		// リクエスト
		requestWrapper.getParameterNames()
		log.info("@ >>>>>Request: " + request.method + " " + request.requestURI)

		// リクエストボディ
		val requestBody = String(requestWrapper.contentAsByteArray, charset(requestWrapper.getCharacterEncoding()))
		if (requestBody.isNotEmpty()) {
			log.info("@ Request Body: $requestBody")
		}

		// リクエストパラメータ
		val builder = StringBuilder()
		val paramNames: Enumeration<*> = request.parameterNames
		while (paramNames.hasMoreElements()) {

			// パラメータ名を取得
			val paramName = paramNames.nextElement() as String
			builder.append(paramName)
			builder.append("=")

			// パラメータ値を取得
			val paramValues = request.getParameterValues(paramName)
			for (i in paramValues.indices) {
				if (i > 0) {
					builder.append(",")
				}
				builder.append(paramValues[i])
			}
			builder.append(",")
		}
		if (builder.isNotEmpty()) {
			log.info("@ Request Param: $builder")
		}

		// メインの処理
		filterChain.doFilter(requestWrapper, responseWrapper)

		// レスポンス
		log.info("@ <<<<<Request: " + request.method + " " + request.requestURI)
		responseWrapper.copyBodyToResponse()
	}
}
