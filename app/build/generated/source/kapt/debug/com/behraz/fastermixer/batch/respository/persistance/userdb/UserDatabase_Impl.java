package com.behraz.fastermixer.batch.respository.persistance.userdb;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDatabase_Impl extends UserDatabase {
  private volatile UserDao _userDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `user_tb` (`phones` TEXT, `adminPhones` TEXT, `personId` TEXT NOT NULL, `name` TEXT, `token` TEXT NOT NULL, `profilePic` TEXT, `roleId` INTEGER, `personalCode` TEXT, `equipmentId` TEXT, PRIMARY KEY(`personId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e4c8be1f8ffb5acc13ef89995317b7ab')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `user_tb`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsUserTb = new HashMap<String, TableInfo.Column>(9);
        _columnsUserTb.put("phones", new TableInfo.Column("phones", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTb.put("adminPhones", new TableInfo.Column("adminPhones", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTb.put("personId", new TableInfo.Column("personId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTb.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTb.put("token", new TableInfo.Column("token", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTb.put("profilePic", new TableInfo.Column("profilePic", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTb.put("roleId", new TableInfo.Column("roleId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTb.put("personalCode", new TableInfo.Column("personalCode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTb.put("equipmentId", new TableInfo.Column("equipmentId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserTb = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserTb = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserTb = new TableInfo("user_tb", _columnsUserTb, _foreignKeysUserTb, _indicesUserTb);
        final TableInfo _existingUserTb = TableInfo.read(_db, "user_tb");
        if (! _infoUserTb.equals(_existingUserTb)) {
          return new RoomOpenHelper.ValidationResult(false, "user_tb(com.behraz.fastermixer.batch.models.User).\n"
                  + " Expected:\n" + _infoUserTb + "\n"
                  + " Found:\n" + _existingUserTb);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e4c8be1f8ffb5acc13ef89995317b7ab", "1c07b9bec0980802df06b9b7257a0b6a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "user_tb");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `user_tb`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public UserDao getUserDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }
}
