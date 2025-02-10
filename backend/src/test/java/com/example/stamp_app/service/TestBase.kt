package com.example.stamp_app.service

import org.dbunit.database.DatabaseConfig
import org.dbunit.database.DatabaseConnection
import org.dbunit.dataset.IDataSet
import org.dbunit.dataset.ReplacementDataSet
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory
import org.dbunit.operation.DatabaseOperation
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import javax.sql.DataSource

@SpringBootTest
@ActiveProfiles("unit-test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // NOTE: 本クラスを継承したクラスを跨いでisMigratedが共有されるようにする
abstract class TestBase {

	@Autowired
	private lateinit var flyway: Flyway

	@Autowired
	private lateinit var dataSource: DataSource

	protected lateinit var dbUnitConnection: DatabaseConnection

	companion object {
		private val isMigrated = AtomicBoolean(false) // NOTE: 一度だけ実行するためのフラグ
	}

	/**
	 * DBのセットアップ
	 *
	 * テスト全体の実行前に1回だけflywayによるmigrationを行う
	 */
	@BeforeAll
	fun setupDatabase() {
		if (isMigrated.compareAndSet(false, true)) {

			// Flywayのマイグレーションを実行
			flyway.clean()
			flyway.migrate()
		}
	}

	/**
	 * DBUnitのセットアップ
	 */
	@BeforeEach
	fun setupDbUnit() {
		dbUnitConnection = DatabaseConnection(dataSource.connection, "PUBLIC") // publicスキーマを指定
			.apply {
				config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, PostgresqlDataTypeFactory())
			}
	}

	/**
	 * テスト用の初期データのInsert処理
	 *
	 * xmlに [null] と記載すると，nullとしてデータがインサートされる
	 * @param filePath Insertしたいxmlのファイルパス
	 */
	protected fun loadDatasetAndInsert(filePath: String) {
		val dataSet: IDataSet = FlatXmlDataSetBuilder().build(File(filePath))
		val replaced = ReplacementDataSet(dataSet).apply {
			addReplacementObject("[null]", null)
		}
		DatabaseOperation.INSERT.execute(dbUnitConnection, replaced)
	}
}