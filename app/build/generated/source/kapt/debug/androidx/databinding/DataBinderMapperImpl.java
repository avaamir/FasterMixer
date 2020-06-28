package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.behraz.fastermixer.batch.DataBinderMapperImpl());
  }
}
