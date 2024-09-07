-- 更新日時を自動で更新するトリガーを追加する
CREATE FUNCTION refresh_updated_at_step1() RETURNS trigger AS
$$
BEGIN
  IF NEW.updated_at = OLD.updated_at THEN
    NEW.updated_at := NULL;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION refresh_updated_at_step2() RETURNS trigger AS
$$
BEGIN
  IF NEW.updated_at IS NULL THEN
    NEW.updated_at := OLD.updated_at;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION refresh_updated_at_step3() RETURNS trigger AS
$$
BEGIN
  IF NEW.updated_at IS NULL THEN
    NEW.updated_at := CURRENT_TIMESTAMP;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Accountテーブル
CREATE TABLE account (
	id bigserial NOT NULL,
	"uuid" uuid NOT NULL,
	mail_address varchar(255) NOT NULL,
	"name" varchar(255) NULL,
	"password" varchar(255) NOT NULL,
	created_at timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
	deleted_at timestamp(6) NULL,
	CONSTRAINT account_pkey PRIMARY KEY (id),
	CONSTRAINT uk_mail_address UNIQUE (mail_address)
);

COMMENT ON TABLE account IS 'アカウントテーブル';
COMMENT ON COLUMN account.id IS 'アカウントID';
COMMENT ON COLUMN account.uuid IS 'アカウントUUID';
COMMENT ON COLUMN account.mail_address IS 'メールアドレス';
COMMENT ON COLUMN account.name IS 'アカウント名';
COMMENT ON COLUMN account.password IS 'ハッシュ化されたパスワード';
COMMENT ON COLUMN account.created_at IS '作成日時';
COMMENT ON COLUMN account.updated_at IS '更新日時';
COMMENT ON COLUMN account.deleted_at IS '削除日時';

CREATE TRIGGER refresh_account_updated_at_step1
  BEFORE UPDATE ON account FOR EACH ROW
  EXECUTE PROCEDURE refresh_updated_at_step1();
CREATE TRIGGER refresh_account_updated_at_step2
  BEFORE UPDATE OF updated_at ON account FOR EACH ROW
  EXECUTE PROCEDURE refresh_updated_at_step2();
CREATE TRIGGER refresh_account_updated_at_step3
  BEFORE UPDATE ON account FOR EACH ROW
  EXECUTE PROCEDURE refresh_updated_at_step3();

-- MicroControllerテーブル
CREATE TABLE micro_controller (
	id bigserial NOT NULL,
	"uuid" uuid NOT NULL,
	"interval" varchar(255) DEFAULT '60' NOT NULL,
	mac_address varchar(255) NOT NULL,
	"name" varchar(255) NULL,
	sdi12address varchar(255) NULL,
	created_at timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
	deleted_at timestamp(6) NULL,
	account_id int8 NULL,
	CONSTRAINT micro_controller_pkey PRIMARY KEY (id),
	CONSTRAINT fk_micro_controller FOREIGN KEY (account_id) REFERENCES account(id)
);

COMMENT ON TABLE micro_controller IS 'マイクロコントローラーテーブル';
COMMENT ON COLUMN micro_controller.id IS 'マイクロコントローラーID';
COMMENT ON COLUMN micro_controller.uuid IS 'マイクロコントローラーUUID';
COMMENT ON COLUMN micro_controller.interval IS '測定間隔(分)';
COMMENT ON COLUMN micro_controller.mac_address IS 'MACアドレス';
COMMENT ON COLUMN micro_controller.name IS '端末名';
COMMENT ON COLUMN micro_controller.sdi12address IS 'SDI-12アドレス';
COMMENT ON COLUMN micro_controller.created_at IS '作成日時';
COMMENT ON COLUMN micro_controller.updated_at IS '更新日時';
COMMENT ON COLUMN micro_controller.deleted_at IS '削除日時';

CREATE TRIGGER refresh_micro_controller_updated_at_step1
  BEFORE UPDATE ON micro_controller FOR EACH ROW
  EXECUTE PROCEDURE refresh_updated_at_step1();
CREATE TRIGGER refresh_micro_controller_updated_at_step2
  BEFORE UPDATE OF updated_at ON micro_controller FOR EACH ROW
  EXECUTE PROCEDURE refresh_updated_at_step2();
CREATE TRIGGER refresh_micro_controller_updated_at_step3
  BEFORE UPDATE ON micro_controller FOR EACH ROW
  EXECUTE PROCEDURE refresh_updated_at_step3();

-- MeasuredDataMasterテーブル
CREATE TABLE measured_data_master (
	id bigserial NOT NULL,
	voltage varchar(255) NULL,
	day_of_year varchar(255) NULL,
	created_at timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
	deleted_at timestamp(6) NULL,
	micro_controller_id int8 NULL,
	CONSTRAINT measured_data_master_pkey PRIMARY KEY (id),
	CONSTRAINT fk_measured_data_master FOREIGN KEY (micro_controller_id) REFERENCES micro_controller(id)
);

COMMENT ON TABLE measured_data_master IS 'マスター測定データテーブル';
COMMENT ON COLUMN measured_data_master.id IS 'マスター測定データID';
COMMENT ON COLUMN measured_data_master.voltage IS '電圧(mV)';
COMMENT ON COLUMN measured_data_master.day_of_year IS 'DOY(-)';
COMMENT ON COLUMN measured_data_master.created_at IS '作成日時';
COMMENT ON COLUMN measured_data_master.updated_at IS '更新日時';
COMMENT ON COLUMN measured_data_master.deleted_at IS '削除日時';
COMMENT ON COLUMN measured_data_master.micro_controller_id IS 'マイクロコントローラーID';

CREATE TRIGGER refresh_measured_data_master_updated_at_step1
  BEFORE UPDATE ON measured_data_master FOR EACH ROW
  EXECUTE PROCEDURE refresh_updated_at_step1();
CREATE TRIGGER refresh_measured_data_master_updated_at_step2
  BEFORE UPDATE OF updated_at ON measured_data_master FOR EACH ROW
  EXECUTE PROCEDURE refresh_updated_at_step2();
CREATE TRIGGER refresh_measured_data_master_updated_at_step3
  BEFORE UPDATE ON measured_data_master FOR EACH ROW
  EXECUTE PROCEDURE refresh_updated_at_step3();

-- EnvironmentalDataテーブル
CREATE TABLE environmental_data (
	id bigserial NOT NULL,
	atmospheric_pressure varchar(255) NULL,
	analog_value varchar(255) NULL,
	co2concentration varchar(255) NULL,
	relative_humidity varchar(255) NULL,
	temperature varchar(255) NULL,
	total_volatile_organic_compounds varchar(255) NULL,
	measured_data_master_id int8 NOT NULL,
	CONSTRAINT environmental_data_pkey PRIMARY KEY (id),
	CONSTRAINT fk_environmental_data FOREIGN KEY (measured_data_master_id) REFERENCES measured_data_master(id)
);

COMMENT ON TABLE environmental_data IS '環境データテーブル';
COMMENT ON COLUMN environmental_data.id IS '環境データID';
COMMENT ON COLUMN environmental_data.atmospheric_pressure IS '大気圧(hPa)';
COMMENT ON COLUMN environmental_data.analog_value IS 'アナログ値';
COMMENT ON COLUMN environmental_data.co2concentration IS '二酸化炭素濃度(ppm)';
COMMENT ON COLUMN environmental_data.relative_humidity IS '相対湿度(%)';
COMMENT ON COLUMN environmental_data.temperature IS '気温(°C)';
COMMENT ON COLUMN environmental_data.total_volatile_organic_compounds IS '総揮発性有機化合物(-)';
COMMENT ON COLUMN environmental_data.measured_data_master_id IS 'マスター測定データID';

-- センサーテーブル
CREATE TABLE sensor (
	id int8 NOT NULL,
	identification_code varchar(255) NOT NULL,
	sensor_name varchar(255) NOT NULL,
	vendor_name varchar(255) NOT NULL,
	CONSTRAINT sensor_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE sensor IS 'センサーテーブル';
COMMENT ON COLUMN sensor.id IS 'センサーID';
COMMENT ON COLUMN sensor.identification_code IS 'センサー識別子';
COMMENT ON COLUMN sensor.sensor_name IS 'センサー名';
COMMENT ON COLUMN sensor.vendor_name IS 'ベンダー名';

-- センサー初期データ
insert into sensor
    (
        id,
        identification_code,
        sensor_name,
        vendor_name
    )
values
    ('1','5WET ','WD-5(WET)','A・R・P'),
    ('2','5WTA ','WD-5(WTA)','A・R・P'),
    ('3','5WT  ','WD-5(WET)','A・R・P'),
    ('4','TDT  ','TDT','Acclima'),
    ('5','TR315','TDR','Acclima'),
    ('6','TER12','TEROS-12','METER Environment'),
    ('7','TER11','TEROS-11','METER Environment'),
    ('8','TER21','TEROS-21','METER Environment'),
    ('999','unknown','unknown','unknown');

-- SDI-12データテーブル
CREATE TABLE sdi12data (
	id bigserial NOT NULL,
	bulk_relative_permittivity varchar(255) NULL,
	gravitational_accelerationxaxis varchar(255) NULL,
	gravitational_accelerationyaxis varchar(255) NULL,
	gravitational_accelerationzaxis varchar(255) NULL,
	soil_bulk_electric_conductivity varchar(255) NULL,
	sdi_address varchar(255) NOT NULL,
	soil_temperature varchar(255) NULL,
	soil_pore_water_electric_conductivity varchar(255) NULL,
	volumetric_water_content varchar(255) NULL,
	measured_data_master_id int8 NOT NULL,
	sensor_id int8 NOT NULL,
	CONSTRAINT sdi12data_pkey PRIMARY KEY (id),
	CONSTRAINT fk_sensor FOREIGN KEY (sensor_id) REFERENCES sensor(id),
	CONSTRAINT fk_measured_data_master FOREIGN KEY (measured_data_master_id) REFERENCES measured_data_master(id)
);

COMMENT ON TABLE sdi12data IS 'SDI-12センサーデータテーブル';
COMMENT ON COLUMN sdi12data.id IS 'SDI-12データID';
COMMENT ON COLUMN sdi12data.bulk_relative_permittivity IS 'バルク比誘電率(-)';
COMMENT ON COLUMN sdi12data.gravitational_accelerationxaxis IS '重力加速度(X)';
COMMENT ON COLUMN sdi12data.gravitational_accelerationyaxis IS '重力加速度(Y)';
COMMENT ON COLUMN sdi12data.gravitational_accelerationzaxis IS '重力加速度(Z)';
COMMENT ON COLUMN sdi12data.soil_bulk_electric_conductivity IS 'バルク電気伝導度(μS/cm)';
COMMENT ON COLUMN sdi12data.sdi_address IS 'SDI-12アドレス(-)';
COMMENT ON COLUMN sdi12data.soil_temperature IS '地温(°C)';
COMMENT ON COLUMN sdi12data.soil_pore_water_electric_conductivity IS '土壌間隙水電気伝導度(μS/cm)';
COMMENT ON COLUMN sdi12data.volumetric_water_content IS '体積含水率(%)';
COMMENT ON COLUMN sdi12data.measured_data_master_id IS 'マスター測定データID';
COMMENT ON COLUMN sdi12data.sensor_id IS 'センサーID';