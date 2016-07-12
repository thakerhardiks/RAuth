package in.radix.rauth.rdbms;

import in.radix.rauth.core.DBName;

public class DatabaseFactory {

	public Database getDb(DBName dB) {
		if (dB == null) {
			return null;
		}

		if (dB == DBName.MySQL) {
			return new MySQL();
		}

		return null;
	}
}