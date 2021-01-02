package com.behraz.fastermixer.batch.respository.persistance.messagedb;

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
import com.behraz.fastermixer.batch.models.Message;
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
public final class MessageDao_Impl implements MessageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Message> __insertionAdapterOfMessage;

  private final EntityDeletionOrUpdateAdapter<Message> __deletionAdapterOfMessage;

  private final EntityDeletionOrUpdateAdapter<Message> __updateAdapterOfMessage;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllMessage;

  private final SharedSQLiteStatement __preparedStmtOfSeenAllMessages;

  public MessageDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMessage = new EntityInsertionAdapter<Message>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `messages` (`id`,`sender`,`senderId`,`dateTime`,`content`,`isDelivered`,`priority`,`_isEvent`,`senderImage`,`userId`,`viewed`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Message value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getSender() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSender());
        }
        if (value.getSenderId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSenderId());
        }
        if (value.getDateTime() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDateTime());
        }
        if (value.getContent() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getContent());
        }
        final int _tmp;
        _tmp = value.isDelivered() ? 1 : 0;
        stmt.bindLong(6, _tmp);
        stmt.bindLong(7, value.getPriority());
        stmt.bindLong(8, value.get_isEvent());
        if (value.getSenderImage() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getSenderImage());
        }
        if (value.getUserId() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindLong(10, value.getUserId());
        }
        final int _tmp_1;
        _tmp_1 = value.getViewed() ? 1 : 0;
        stmt.bindLong(11, _tmp_1);
      }
    };
    this.__deletionAdapterOfMessage = new EntityDeletionOrUpdateAdapter<Message>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `messages` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Message value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfMessage = new EntityDeletionOrUpdateAdapter<Message>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `messages` SET `id` = ?,`sender` = ?,`senderId` = ?,`dateTime` = ?,`content` = ?,`isDelivered` = ?,`priority` = ?,`_isEvent` = ?,`senderImage` = ?,`userId` = ?,`viewed` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Message value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getSender() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSender());
        }
        if (value.getSenderId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSenderId());
        }
        if (value.getDateTime() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDateTime());
        }
        if (value.getContent() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getContent());
        }
        final int _tmp;
        _tmp = value.isDelivered() ? 1 : 0;
        stmt.bindLong(6, _tmp);
        stmt.bindLong(7, value.getPriority());
        stmt.bindLong(8, value.get_isEvent());
        if (value.getSenderImage() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getSenderImage());
        }
        if (value.getUserId() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindLong(10, value.getUserId());
        }
        final int _tmp_1;
        _tmp_1 = value.getViewed() ? 1 : 0;
        stmt.bindLong(11, _tmp_1);
        if (value.getId() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteAllMessage = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM messages";
        return _query;
      }
    };
    this.__preparedStmtOfSeenAllMessages = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE messages SET viewed=1 WHERE viewed=0";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Message item, final Continuation<? super Unit> p1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMessage.insert(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, p1);
  }

  @Override
  public Object insertAll(final List<Message> items, final Continuation<? super Unit> p1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMessage.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, p1);
  }

  @Override
  public Object delete(final Message item, final Continuation<? super Unit> p1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMessage.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, p1);
  }

  @Override
  public Object update(final Message item, final Continuation<? super Unit> p1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMessage.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, p1);
  }

  @Override
  public Object deleteAllMessage(final Continuation<? super Unit> p0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllMessage.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAllMessage.release(_stmt);
        }
      }
    }, p0);
  }

  @Override
  public void seenAllMessages() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfSeenAllMessages.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfSeenAllMessages.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Message>> getAllMessage() {
    final String _sql = "SELECT * FROM messages ORDER BY dateTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"messages"}, false, new Callable<List<Message>>() {
      @Override
      public List<Message> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSender = CursorUtil.getColumnIndexOrThrow(_cursor, "sender");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTime");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfIsDelivered = CursorUtil.getColumnIndexOrThrow(_cursor, "isDelivered");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfIsEvent = CursorUtil.getColumnIndexOrThrow(_cursor, "_isEvent");
          final int _cursorIndexOfSenderImage = CursorUtil.getColumnIndexOrThrow(_cursor, "senderImage");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfViewed = CursorUtil.getColumnIndexOrThrow(_cursor, "viewed");
          final List<Message> _result = new ArrayList<Message>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Message _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSender;
            _tmpSender = _cursor.getString(_cursorIndexOfSender);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpDateTime;
            _tmpDateTime = _cursor.getString(_cursorIndexOfDateTime);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final boolean _tmpIsDelivered;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDelivered);
            _tmpIsDelivered = _tmp != 0;
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            final int _tmp_isEvent;
            _tmp_isEvent = _cursor.getInt(_cursorIndexOfIsEvent);
            final String _tmpSenderImage;
            _tmpSenderImage = _cursor.getString(_cursorIndexOfSenderImage);
            final Integer _tmpUserId;
            if (_cursor.isNull(_cursorIndexOfUserId)) {
              _tmpUserId = null;
            } else {
              _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            }
            final boolean _tmpViewed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfViewed);
            _tmpViewed = _tmp_1 != 0;
            _item = new Message(_tmpId,_tmpSender,_tmpSenderId,_tmpDateTime,_tmpContent,_tmpIsDelivered,_tmpPriority,_tmp_isEvent,_tmpSenderImage,_tmpUserId,_tmpViewed);
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
  public Object exists(final int id, final Continuation<? super Message> p1) {
    final String _sql = "SELECT * FROM messages WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.execute(__db, false, new Callable<Message>() {
      @Override
      public Message call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSender = CursorUtil.getColumnIndexOrThrow(_cursor, "sender");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTime");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfIsDelivered = CursorUtil.getColumnIndexOrThrow(_cursor, "isDelivered");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfIsEvent = CursorUtil.getColumnIndexOrThrow(_cursor, "_isEvent");
          final int _cursorIndexOfSenderImage = CursorUtil.getColumnIndexOrThrow(_cursor, "senderImage");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfViewed = CursorUtil.getColumnIndexOrThrow(_cursor, "viewed");
          final Message _result;
          if(_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSender;
            _tmpSender = _cursor.getString(_cursorIndexOfSender);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpDateTime;
            _tmpDateTime = _cursor.getString(_cursorIndexOfDateTime);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final boolean _tmpIsDelivered;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDelivered);
            _tmpIsDelivered = _tmp != 0;
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            final int _tmp_isEvent;
            _tmp_isEvent = _cursor.getInt(_cursorIndexOfIsEvent);
            final String _tmpSenderImage;
            _tmpSenderImage = _cursor.getString(_cursorIndexOfSenderImage);
            final Integer _tmpUserId;
            if (_cursor.isNull(_cursorIndexOfUserId)) {
              _tmpUserId = null;
            } else {
              _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            }
            final boolean _tmpViewed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfViewed);
            _tmpViewed = _tmp_1 != 0;
            _result = new Message(_tmpId,_tmpSender,_tmpSenderId,_tmpDateTime,_tmpContent,_tmpIsDelivered,_tmpPriority,_tmp_isEvent,_tmpSenderImage,_tmpUserId,_tmpViewed);
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
