package com.example.stamp_app.common.enums

enum class ErrorCode(
	val code: String,
	val message: String
) {
	// 通常の例外は 1xx で採番
	CONSTRAINT_VIOLATION_EXCEPTION("101", "リクエスト内容が不正です。"),
	ILLEGAL_ARGUMENT_EXCEPTION("102", "リクエスト内容が不正です。"),
	ILLEGAL_ACCESS_EXCEPTION("103", "不正なアクセスです。"),

	// カスタム例外は 2xx で採番
	EMS_DATABASE_EXCEPTION("201", "データ取得時にエラーが発生しました。"),
	EMS_RESOURCE_NOT_FOUND_EXCEPTION("202", "リソースが見つかりません。"),
	EMS_RESOURCE_DUPLICATION_EXCEPTION("203", "リソースが重複しています。"),

	// Catch できなかった例外
	EMS_UNCAUGHT_EXCEPTION("999", "予期せぬエラーが発生しました。");

}
