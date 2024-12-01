package com.example.stamp_app.domain.common.entity

data class AppResponseObject(
	private val response: Any? = null,
	private val errors: MutableList<Error> = ArrayList()
) {

	fun addError(error: Error) {
		errors.add(error)
	}

	companion object {

		/**
		 * エラーを含んだレスポンスObjectを返す
		 *
		 * @param code エラーコード
		 * @param message エラーメッセージ
		 */
		fun createErrorResponse(code: String?, message: String?): AppResponseObject {
			val response = AppResponseObject()
			val error = Error(code, message)
			response.addError(error)
			return response
		}

	}
}
