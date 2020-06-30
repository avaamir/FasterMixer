package com.behraz.fastermixer.batch;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.behraz.fastermixer.batch.databinding.ActivityChooseBatchBindingImpl;
import com.behraz.fastermixer.batch.databinding.ActivityChoosePompBindingImpl;
import com.behraz.fastermixer.batch.databinding.ActivityPompBindingImpl;
import com.behraz.fastermixer.batch.databinding.FragmentCustomerListBindingImpl;
import com.behraz.fastermixer.batch.databinding.FragmentMessageListBindingImpl;
import com.behraz.fastermixer.batch.databinding.FragmentMixerListBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemCustomerBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemEquipmentBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemMessageBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemMessageVerticalBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemMixerBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemPompMixerBindingImpl;
import com.behraz.fastermixer.batch.databinding.LayoutMapBindingImpl;
import com.behraz.fastermixer.batch.databinding.LayoutMixerBindingImpl;
import com.behraz.fastermixer.batch.databinding.ViewFasterMixerUserPanelBindingImpl;
import com.behraz.fastermixer.batch.databinding.ViewItemProgressBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYCHOOSEBATCH = 1;

  private static final int LAYOUT_ACTIVITYCHOOSEPOMP = 2;

  private static final int LAYOUT_ACTIVITYPOMP = 3;

  private static final int LAYOUT_FRAGMENTCUSTOMERLIST = 4;

  private static final int LAYOUT_FRAGMENTMESSAGELIST = 5;

  private static final int LAYOUT_FRAGMENTMIXERLIST = 6;

  private static final int LAYOUT_ITEMCUSTOMER = 7;

  private static final int LAYOUT_ITEMEQUIPMENT = 8;

  private static final int LAYOUT_ITEMMESSAGE = 9;

  private static final int LAYOUT_ITEMMESSAGEVERTICAL = 10;

  private static final int LAYOUT_ITEMMIXER = 11;

  private static final int LAYOUT_ITEMPOMPMIXER = 12;

  private static final int LAYOUT_LAYOUTMAP = 13;

  private static final int LAYOUT_LAYOUTMIXER = 14;

  private static final int LAYOUT_VIEWFASTERMIXERUSERPANEL = 15;

  private static final int LAYOUT_VIEWITEMPROGRESS = 16;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(16);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.activity_choose_batch, LAYOUT_ACTIVITYCHOOSEBATCH);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.activity_choose_pomp, LAYOUT_ACTIVITYCHOOSEPOMP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.activity_pomp, LAYOUT_ACTIVITYPOMP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.fragment_customer_list, LAYOUT_FRAGMENTCUSTOMERLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.fragment_message_list, LAYOUT_FRAGMENTMESSAGELIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.fragment_mixer_list, LAYOUT_FRAGMENTMIXERLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_customer, LAYOUT_ITEMCUSTOMER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_equipment, LAYOUT_ITEMEQUIPMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_message, LAYOUT_ITEMMESSAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_message_vertical, LAYOUT_ITEMMESSAGEVERTICAL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_mixer, LAYOUT_ITEMMIXER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_pomp_mixer, LAYOUT_ITEMPOMPMIXER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.layout_map, LAYOUT_LAYOUTMAP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.layout_mixer, LAYOUT_LAYOUTMIXER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.view_faster_mixer_user_panel, LAYOUT_VIEWFASTERMIXERUSERPANEL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.view_item_progress, LAYOUT_VIEWITEMPROGRESS);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYCHOOSEBATCH: {
          if ("layout/activity_choose_batch_0".equals(tag)) {
            return new ActivityChooseBatchBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_choose_batch is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYCHOOSEPOMP: {
          if ("layout/activity_choose_pomp_0".equals(tag)) {
            return new ActivityChoosePompBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_choose_pomp is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYPOMP: {
          if ("layout/activity_pomp_0".equals(tag)) {
            return new ActivityPompBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_pomp is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTCUSTOMERLIST: {
          if ("layout/fragment_customer_list_0".equals(tag)) {
            return new FragmentCustomerListBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_customer_list is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTMESSAGELIST: {
          if ("layout/fragment_message_list_0".equals(tag)) {
            return new FragmentMessageListBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_message_list is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTMIXERLIST: {
          if ("layout/fragment_mixer_list_0".equals(tag)) {
            return new FragmentMixerListBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_mixer_list is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMCUSTOMER: {
          if ("layout/item_customer_0".equals(tag)) {
            return new ItemCustomerBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_customer is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMEQUIPMENT: {
          if ("layout/item_equipment_0".equals(tag)) {
            return new ItemEquipmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_equipment is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMMESSAGE: {
          if ("layout/item_message_0".equals(tag)) {
            return new ItemMessageBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_message is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMMESSAGEVERTICAL: {
          if ("layout/item_message_vertical_0".equals(tag)) {
            return new ItemMessageVerticalBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_message_vertical is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMMIXER: {
          if ("layout/item_mixer_0".equals(tag)) {
            return new ItemMixerBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_mixer is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMPOMPMIXER: {
          if ("layout/item_pomp_mixer_0".equals(tag)) {
            return new ItemPompMixerBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_pomp_mixer is invalid. Received: " + tag);
        }
        case  LAYOUT_LAYOUTMAP: {
          if ("layout/layout_map_0".equals(tag)) {
            return new LayoutMapBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_map is invalid. Received: " + tag);
        }
        case  LAYOUT_LAYOUTMIXER: {
          if ("layout/layout_mixer_0".equals(tag)) {
            return new LayoutMixerBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_mixer is invalid. Received: " + tag);
        }
        case  LAYOUT_VIEWFASTERMIXERUSERPANEL: {
          if ("layout/view_faster_mixer_user_panel_0".equals(tag)) {
            return new ViewFasterMixerUserPanelBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for view_faster_mixer_user_panel is invalid. Received: " + tag);
        }
        case  LAYOUT_VIEWITEMPROGRESS: {
          if ("layout/view_item_progress_0".equals(tag)) {
            return new ViewItemProgressBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for view_item_progress is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(8);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "batch");
      sKeys.put(2, "customer");
      sKeys.put(3, "message");
      sKeys.put(4, "mixer");
      sKeys.put(5, "progress");
      sKeys.put(6, "user");
      sKeys.put(7, "viewModel");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(16);

    static {
      sKeys.put("layout/activity_choose_batch_0", com.behraz.fastermixer.batch.R.layout.activity_choose_batch);
      sKeys.put("layout/activity_choose_pomp_0", com.behraz.fastermixer.batch.R.layout.activity_choose_pomp);
      sKeys.put("layout/activity_pomp_0", com.behraz.fastermixer.batch.R.layout.activity_pomp);
      sKeys.put("layout/fragment_customer_list_0", com.behraz.fastermixer.batch.R.layout.fragment_customer_list);
      sKeys.put("layout/fragment_message_list_0", com.behraz.fastermixer.batch.R.layout.fragment_message_list);
      sKeys.put("layout/fragment_mixer_list_0", com.behraz.fastermixer.batch.R.layout.fragment_mixer_list);
      sKeys.put("layout/item_customer_0", com.behraz.fastermixer.batch.R.layout.item_customer);
      sKeys.put("layout/item_equipment_0", com.behraz.fastermixer.batch.R.layout.item_equipment);
      sKeys.put("layout/item_message_0", com.behraz.fastermixer.batch.R.layout.item_message);
      sKeys.put("layout/item_message_vertical_0", com.behraz.fastermixer.batch.R.layout.item_message_vertical);
      sKeys.put("layout/item_mixer_0", com.behraz.fastermixer.batch.R.layout.item_mixer);
      sKeys.put("layout/item_pomp_mixer_0", com.behraz.fastermixer.batch.R.layout.item_pomp_mixer);
      sKeys.put("layout/layout_map_0", com.behraz.fastermixer.batch.R.layout.layout_map);
      sKeys.put("layout/layout_mixer_0", com.behraz.fastermixer.batch.R.layout.layout_mixer);
      sKeys.put("layout/view_faster_mixer_user_panel_0", com.behraz.fastermixer.batch.R.layout.view_faster_mixer_user_panel);
      sKeys.put("layout/view_item_progress_0", com.behraz.fastermixer.batch.R.layout.view_item_progress);
    }
  }
}
