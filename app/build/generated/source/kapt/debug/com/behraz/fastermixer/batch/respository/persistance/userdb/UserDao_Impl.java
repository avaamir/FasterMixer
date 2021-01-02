package com.behraz.fastermixer.batch.respository.persistance.userdb;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.behraz.fastermixer.batch.models.User;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<User> __insertionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __deletionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __updateAdapterOfUser;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public UserDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `user_tb` (`id`,`name`,`personalCode`,`token`,`roleId`,`equipmentId`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getPersonalCode() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPersonalCode());
        }
        if (value.getToken() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getToken());
        }
        if (value.getRoleId() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getRoleId());
        }
        if (value.getEquipmentId() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, value.getEquipmentId());
        }
      }
    };
    this.__deletionAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `user_tb` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `user_tb` SET `id` = ?,`name` = ?,`personalCode` = ?,`token` = ?,`roleId` = ?,`equipmentId` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getPersonalCode() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPersonalCode());
        }
        if (value.getToken() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getToken());
        }
        if (value.getRoleId() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getRoleId());
        }
        if (value.getEquipmentId() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, value.getEquipmentId());
        }
        stmt.bindLong(7, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM user_tb";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final User item, final Continuation<? super Unit> p1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUser.insert(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, p1);
  }

  @Override
  public void insert1(final User item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUser.insert(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Object delete(final User item, final Continuation<? super Unit> p1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfUser.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, p1);
  }

  @Override
  public Object update(final User item, final Continuation<? super Unit> p1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUser.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, p1);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> p0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, p0);
  }

  @Override
  public void deleteAll1() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<User>> getUsers() {
    final String _sql = "SELECT * FROM user_tb";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"user_tb"}, false, new Callable<List<User>>() {
      @Override
      public List<User> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPersonalCode = CursorUtil.getColumnIndexOrThrow(_cursor, "personalCode");
          final int _cursorIndexOfToken = CursorUtil.getColumnIndexOrThrow(_cursor, "token");
          final int _cursorIndexOfRoleId = CursorUtil.getColumnIndexOrThrow(_cursor, "roleId");
          final int _cursorIndexOfEquipmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "equipmentId");
          final List<User> _result = new ArrayList<User>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final User _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpPersonalCode;
            _tmpPersonalCode = _cursor.getString(_cursorIndexOfPersonalCode);
            final String _tmpToken;
            _tmpToken = _cursor.getString(_cursorIndexOfToken);
            final Integer _tmpRoleId;
            if (_cursor.isNull(_cursorIndexOfRoleId)) {
              _tmpRoleId = null;
            } else {
              _tmpRoleId = _cursor.getInt(_cursorIndexOfRoleId);
            }
            final Integer _tmpEquipmentId;
            if (_cursor.isNull(_cursorIndexOfEquipmentId)) {
              _tmpEquipmentId = null;
            } else {
              _tmpEquipmentId = _cursor.getInt(_cursorIndexOfEquipmentId);
            }
            _item = new User(_tmpId,_tmpName,_tmpPersonalCode,_tmpToken,_tmpRoleId,_tmpEquipmentId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object exists(final int id, final Continuation<? super User> p1) {
    final String _sql = "SELECT * FROM user_tb WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.execute(__db, false, new Callable<User>() {
      @Override
      public User call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPersonalCode = CursorUtil.getColumnIndexOrThrow(_cursor, "personalCode");
          final int _cursorIndexOfToken = CursorUtil.getColumnIndexOrThrow(_cursor, "token");
          final int _cursorIndexOfRoleId = CursorUtil.getColumnIndexOrThrow(_cursor, "roleId");
          final int _cursorIndexOfEquipmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "equipmentId");
          final User _result;
          if(_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpPersonalCode;
            _tmpPersonalCode = _cursor.getString(_cursorIndexOfPersonalCode);
            final String _tmpToken;
            _tmpToken = _cursor.getString(_cursorIndexOfToken);
            final Integer _tmpRoleId;
            if (_cursor.isNull(_cursorIndexOfRoleId)) {
              _tmpRoleId = null;
            } else {
              _tmpRoleId = _cursor.getInt(_cursorIndexOfRoleId);
            }
            final Integer _tmpEquipmentId;
            if (_cursor.isNull(_cursorIndexOfEquipmentId)) {
              _tmpEquipmentId = null;
            } else {
              _tmpEquipmentId = _cursor.getInt(_cursorIndexOfEquipmentId);
            }
            _result = new User(_tmpId,_tmpName,_tmpPersonalCode,_tmpToken,_tmpRoleId,_tmpEquipmentId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, p1);
  }
}
