package com.behraz.fastermixer.batch;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.behraz.fastermixer.batch.databinding.ActivityAdminBindingImpl;
import com.behraz.fastermixer.batch.databinding.ActivityBatchNewBindingImpl;
import com.behraz.fastermixer.batch.databinding.ActivityChooseBatchBindingImpl;
import com.behraz.fastermixer.batch.databinding.ActivityChoosePompBindingImpl;
import com.behraz.fastermixer.batch.databinding.ActivityContactBindingImpl;
import com.behraz.fastermixer.batch.databinding.ActivityMixerBindingImpl;
import com.behraz.fastermixer.batch.databinding.ActivityPompBindingImpl;
import com.behraz.fastermixer.batch.databinding.ActivityTestBindingImpl;
import com.behraz.fastermixer.batch.databinding.FragmentChooseReportDateBindingImpl;
import com.behraz.fastermixer.batch.databinding.FragmentChooseReportEquipmentBindingImpl;
import com.behraz.fastermixer.batch.databinding.FragmentCustomerListBindingImpl;
import com.behraz.fastermixer.batch.databinding.FragmentEquipmentsBindingImpl;
import com.behraz.fastermixer.batch.databinding.FragmentFullReportBindingImpl;
import com.behraz.fastermixer.batch.databinding.FragmentMessageListBindingImpl;
import com.behraz.fastermixer.batch.databinding.FragmentMixerListBindingImpl;
import com.behraz.fastermixer.batch.databinding.FragmentReportBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemAdminEquipmentBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemChooseEquipmentBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemContactBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemCustomerBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemMessageBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemMessageVerticalBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemMixerBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemPackageBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemPlanBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemPompMixerBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemReportFullBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemTransactionHistoryBindingImpl;
import com.behraz.fastermixer.batch.databinding.ItemWeatherBindingImpl;
import com.behraz.fastermixer.batch.databinding.LayoutAdminManageAccountFragmentBindingImpl;
import com.behraz.fastermixer.batch.databinding.LayoutBatchFragmentBindingImpl;
import com.behraz.fastermixer.batch.databinding.LayoutContactsFragmentBindingImpl;
import com.behraz.fastermixer.batch.databinding.LayoutDashboardFragmentBindingImpl;
import com.behraz.fastermixer.batch.databinding.LayoutDilaogWeatherBindingImpl;
import com.behraz.fastermixer.batch.databinding.LayoutFragmentRequestsBindingImpl;
import com.behraz.fastermixer.batch.databinding.LayoutMapBindingImpl;
import com.behraz.fastermixer.batch.databinding.LayoutMixerBindingImpl;
import com.behraz.fastermixer.batch.databinding.LayoutRecordDialogBindingImpl;
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
  private static final int LAYOUT_ACTIVITYADMIN = 1;

  private static final int LAYOUT_ACTIVITYBATCHNEW = 2;

  private static final int LAYOUT_ACTIVITYCHOOSEBATCH = 3;

  private static final int LAYOUT_ACTIVITYCHOOSEPOMP = 4;

  private static final int LAYOUT_ACTIVITYCONTACT = 5;

  private static final int LAYOUT_ACTIVITYMIXER = 6;

  private static final int LAYOUT_ACTIVITYPOMP = 7;

  private static final int LAYOUT_ACTIVITYTEST = 8;

  private static final int LAYOUT_FRAGMENTCHOOSEREPORTDATE = 9;

  private static final int LAYOUT_FRAGMENTCHOOSEREPORTEQUIPMENT = 10;

  private static final int LAYOUT_FRAGMENTCUSTOMERLIST = 11;

  private static final int LAYOUT_FRAGMENTEQUIPMENTS = 12;

  private static final int LAYOUT_FRAGMENTFULLREPORT = 13;

  private static final int LAYOUT_FRAGMENTMESSAGELIST = 14;

  private static final int LAYOUT_FRAGMENTMIXERLIST = 15;

  private static final int LAYOUT_FRAGMENTREPORT = 16;

  private static final int LAYOUT_ITEMADMINEQUIPMENT = 17;

  private static final int LAYOUT_ITEMCHOOSEEQUIPMENT = 18;

  private static final int LAYOUT_ITEMCONTACT = 19;

  private static final int LAYOUT_ITEMCUSTOMER = 20;

  private static final int LAYOUT_ITEMMESSAGE = 21;

  private static final int LAYOUT_ITEMMESSAGEVERTICAL = 22;

  private static final int LAYOUT_ITEMMIXER = 23;

  private static final int LAYOUT_ITEMPACKAGE = 24;

  private static final int LAYOUT_ITEMPLAN = 25;

  private static final int LAYOUT_ITEMPOMPMIXER = 26;

  private static final int LAYOUT_ITEMREPORTFULL = 27;

  private static final int LAYOUT_ITEMTRANSACTIONHISTORY = 28;

  private static final int LAYOUT_ITEMWEATHER = 29;

  private static final int LAYOUT_LAYOUTADMINMANAGEACCOUNTFRAGMENT = 30;

  private static final int LAYOUT_LAYOUTBATCHFRAGMENT = 31;

  private static final int LAYOUT_LAYOUTCONTACTSFRAGMENT = 32;

  private static final int LAYOUT_LAYOUTDASHBOARDFRAGMENT = 33;

  private static final int LAYOUT_LAYOUTDILAOGWEATHER = 34;

  private static final int LAYOUT_LAYOUTFRAGMENTREQUESTS = 35;

  private static final int LAYOUT_LAYOUTMAP = 36;

  private static final int LAYOUT_LAYOUTMIXER = 37;

  private static final int LAYOUT_LAYOUTRECORDDIALOG = 38;

  private static final int LAYOUT_VIEWFASTERMIXERUSERPANEL = 39;

  private static final int LAYOUT_VIEWITEMPROGRESS = 40;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(40);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.activity_admin, LAYOUT_ACTIVITYADMIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.activity_batch_new, LAYOUT_ACTIVITYBATCHNEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.activity_choose_batch, LAYOUT_ACTIVITYCHOOSEBATCH);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.activity_choose_pomp, LAYOUT_ACTIVITYCHOOSEPOMP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.activity_contact, LAYOUT_ACTIVITYCONTACT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.activity_mixer, LAYOUT_ACTIVITYMIXER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.activity_pomp, LAYOUT_ACTIVITYPOMP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.activity_test, LAYOUT_ACTIVITYTEST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.fragment_choose_report_date, LAYOUT_FRAGMENTCHOOSEREPORTDATE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.fragment_choose_report_equipment, LAYOUT_FRAGMENTCHOOSEREPORTEQUIPMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.fragment_customer_list, LAYOUT_FRAGMENTCUSTOMERLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.fragment_equipments, LAYOUT_FRAGMENTEQUIPMENTS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.fragment_full_report, LAYOUT_FRAGMENTFULLREPORT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.fragment_message_list, LAYOUT_FRAGMENTMESSAGELIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.fragment_mixer_list, LAYOUT_FRAGMENTMIXERLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.fragment_report, LAYOUT_FRAGMENTREPORT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_admin_equipment, LAYOUT_ITEMADMINEQUIPMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_choose_equipment, LAYOUT_ITEMCHOOSEEQUIPMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_contact, LAYOUT_ITEMCONTACT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_customer, LAYOUT_ITEMCUSTOMER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_message, LAYOUT_ITEMMESSAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_message_vertical, LAYOUT_ITEMMESSAGEVERTICAL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_mixer, LAYOUT_ITEMMIXER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_package, LAYOUT_ITEMPACKAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_plan, LAYOUT_ITEMPLAN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_pomp_mixer, LAYOUT_ITEMPOMPMIXER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_report_full, LAYOUT_ITEMREPORTFULL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_transaction_history, LAYOUT_ITEMTRANSACTIONHISTORY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.item_weather, LAYOUT_ITEMWEATHER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.layout_admin_manage_account_fragment, LAYOUT_LAYOUTADMINMANAGEACCOUNTFRAGMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.layout_batch_fragment, LAYOUT_LAYOUTBATCHFRAGMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.layout_contacts_fragment, LAYOUT_LAYOUTCONTACTSFRAGMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.layout_dashboard_fragment, LAYOUT_LAYOUTDASHBOARDFRAGMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.layout_dilaog_weather, LAYOUT_LAYOUTDILAOGWEATHER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.layout_fragment_requests, LAYOUT_LAYOUTFRAGMENTREQUESTS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.layout_map, LAYOUT_LAYOUTMAP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.layout_mixer, LAYOUT_LAYOUTMIXER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.behraz.fastermixer.batch.R.layout.layout_record_dialog, LAYOUT_LAYOUTRECORDDIALOG);
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
        case  LAYOUT_ACTIVITYADMIN: {
          if ("layout/activity_admin_0".equals(tag)) {
            return new ActivityAdminBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_admin is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYBATCHNEW: {
          if ("layout/activity_batch_new_0".equals(tag)) {
            return new ActivityBatchNewBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_batch_new is invalid. Received: " + tag);
        }
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
        case  LAYOUT_ACTIVITYCONTACT: {
          if ("layout/activity_contact_0".equals(tag)) {
            return new ActivityContactBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_contact is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMIXER: {
          if ("layout/activity_mixer_0".equals(tag)) {
            return new ActivityMixerBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_mixer is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYPOMP: {
          if ("layout/activity_pomp_0".equals(tag)) {
            return new ActivityPompBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_pomp is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYTEST: {
          if ("layout/activity_test_0".equals(tag)) {
            return new ActivityTestBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_test is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTCHOOSEREPORTDATE: {
          if ("layout/fragment_choose_report_date_0".equals(tag)) {
            return new FragmentChooseReportDateBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_choose_report_date is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTCHOOSEREPORTEQUIPMENT: {
          if ("layout/fragment_choose_report_equipment_0".equals(tag)) {
            return new FragmentChooseReportEquipmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_choose_report_equipment is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTCUSTOMERLIST: {
          if ("layout/fragment_customer_list_0".equals(tag)) {
            return new FragmentCustomerListBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_customer_list is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTEQUIPMENTS: {
          if ("layout/fragment_equipments_0".equals(tag)) {
            return new FragmentEquipmentsBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_equipments is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTFULLREPORT: {
          if ("layout/fragment_full_report_0".equals(tag)) {
            return new FragmentFullReportBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_full_report is invalid. Received: " + tag);
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
        case  LAYOUT_FRAGMENTREPORT: {
          if ("layout/fragment_report_0".equals(tag)) {
            return new FragmentReportBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_report is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMADMINEQUIPMENT: {
          if ("layout/item_admin_equipment_0".equals(tag)) {
            return new ItemAdminEquipmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_admin_equipment is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMCHOOSEEQUIPMENT: {
          if ("layout/item_choose_equipment_0".equals(tag)) {
            return new ItemChooseEquipmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_choose_equipment is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMCONTACT: {
          if ("layout/item_contact_0".equals(tag)) {
            return new ItemContactBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_contact is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMCUSTOMER: {
          if ("layout/item_customer_0".equals(tag)) {
            return new ItemCustomerBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_customer is invalid. Received: " + tag);
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
        case  LAYOUT_ITEMPACKAGE: {
          if ("layout/item_package_0".equals(tag)) {
            return new ItemPackageBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_package is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMPLAN: {
          if ("layout/item_plan_0".equals(tag)) {
            return new ItemPlanBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_plan is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMPOMPMIXER: {
          if ("layout/item_pomp_mixer_0".equals(tag)) {
            return new ItemPompMixerBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_pomp_mixer is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMREPORTFULL: {
          if ("layout/item_report_full_0".equals(tag)) {
            return new ItemReportFullBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_report_full is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMTRANSACTIONHISTORY: {
          if ("layout/item_transaction_history_0".equals(tag)) {
            return new ItemTransactionHistoryBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_transaction_history is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMWEATHER: {
          if ("layout/item_weather_0".equals(tag)) {
            return new ItemWeatherBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_weather is invalid. Received: " + tag);
        }
        case  LAYOUT_LAYOUTADMINMANAGEACCOUNTFRAGMENT: {
          if ("layout/layout_admin_manage_account_fragment_0".equals(tag)) {
            return new LayoutAdminManageAccountFragmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_admin_manage_account_fragment is invalid. Received: " + tag);
        }
        case  LAYOUT_LAYOUTBATCHFRAGMENT: {
          if ("layout/layout_batch_fragment_0".equals(tag)) {
            return new LayoutBatchFragmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_batch_fragment is invalid. Received: " + tag);
        }
        case  LAYOUT_LAYOUTCONTACTSFRAGMENT: {
          if ("layout/layout_contacts_fragment_0".equals(tag)) {
            return new LayoutContactsFragmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_contacts_fragment is invalid. Received: " + tag);
        }
        case  LAYOUT_LAYOUTDASHBOARDFRAGMENT: {
          if ("layout/layout_dashboard_fragment_0".equals(tag)) {
            return new LayoutDashboardFragmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_dashboard_fragment is invalid. Received: " + tag);
        }
        case  LAYOUT_LAYOUTDILAOGWEATHER: {
          if ("layout/layout_dilaog_weather_0".equals(tag)) {
            return new LayoutDilaogWeatherBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_dilaog_weather is invalid. Received: " + tag);
        }
        case  LAYOUT_LAYOUTFRAGMENTREQUESTS: {
          if ("layout/layout_fragment_requests_0".equals(tag)) {
            return new LayoutFragmentRequestsBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_fragment_requests is invalid. Received: " + tag);
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
        case  LAYOUT_LAYOUTRECORDDIALOG: {
          if ("layout/layout_record_dialog_0".equals(tag)) {
            return new LayoutRecordDialogBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_record_dialog is invalid. Received: " + tag);
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
    static final SparseArray<String> sKeys = new SparseArray<String>(13);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "batch");
      sKeys.put(2, "contact");
      sKeys.put(3, "customer");
      sKeys.put(4, "data");
      sKeys.put(5, "equipment");
      sKeys.put(6, "message");
      sKeys.put(7, "mixer");
      sKeys.put(8, "plan");
      sKeys.put(9, "progress");
      sKeys.put(10, "user");
      sKeys.put(11, "viewData");
      sKeys.put(12, "viewModel");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(40);

    static {
      sKeys.put("layout/activity_admin_0", com.behraz.fastermixer.batch.R.layout.activity_admin);
      sKeys.put("layout/activity_batch_new_0", com.behraz.fastermixer.batch.R.layout.activity_batch_new);
      sKeys.put("layout/activity_choose_batch_0", com.behraz.fastermixer.batch.R.layout.activity_choose_batch);
      sKeys.put("layout/activity_choose_pomp_0", com.behraz.fastermixer.batch.R.layout.activity_choose_pomp);
      sKeys.put("layout/activity_contact_0", com.behraz.fastermixer.batch.R.layout.activity_contact);
      sKeys.put("layout/activity_mixer_0", com.behraz.fastermixer.batch.R.layout.activity_mixer);
      sKeys.put("layout/activity_pomp_0", com.behraz.fastermixer.batch.R.layout.activity_pomp);
      sKeys.put("layout/activity_test_0", com.behraz.fastermixer.batch.R.layout.activity_test);
      sKeys.put("layout/fragment_choose_report_date_0", com.behraz.fastermixer.batch.R.layout.fragment_choose_report_date);
      sKeys.put("layout/fragment_choose_report_equipment_0", com.behraz.fastermixer.batch.R.layout.fragment_choose_report_equipment);
      sKeys.put("layout/fragment_customer_list_0", com.behraz.fastermixer.batch.R.layout.fragment_customer_list);
      sKeys.put("layout/fragment_equipments_0", com.behraz.fastermixer.batch.R.layout.fragment_equipments);
      sKeys.put("layout/fragment_full_report_0", com.behraz.fastermixer.batch.R.layout.fragment_full_report);
      sKeys.put("layout/fragment_message_list_0", com.behraz.fastermixer.batch.R.layout.fragment_message_list);
      sKeys.put("layout/fragment_mixer_list_0", com.behraz.fastermixer.batch.R.layout.fragment_mixer_list);
      sKeys.put("layout/fragment_report_0", com.behraz.fastermixer.batch.R.layout.fragment_report);
      sKeys.put("layout/item_admin_equipment_0", com.behraz.fastermixer.batch.R.layout.item_admin_equipment);
      sKeys.put("layout/item_choose_equipment_0", com.behraz.fastermixer.batch.R.layout.item_choose_equipment);
      sKeys.put("layout/item_contact_0", com.behraz.fastermixer.batch.R.layout.item_contact);
      sKeys.put("layout/item_customer_0", com.behraz.fastermixer.batch.R.layout.item_customer);
      sKeys.put("layout/item_message_0", com.behraz.fastermixer.batch.R.layout.item_message);
      sKeys.put("layout/item_message_vertical_0", com.behraz.fastermixer.batch.R.layout.item_message_vertical);
      sKeys.put("layout/item_mixer_0", com.behraz.fastermixer.batch.R.layout.item_mixer);
      sKeys.put("layout/item_package_0", com.behraz.fastermixer.batch.R.layout.item_package);
      sKeys.put("layout/item_plan_0", com.behraz.fastermixer.batch.R.layout.item_plan);
      sKeys.put("layout/item_pomp_mixer_0", com.behraz.fastermixer.batch.R.layout.item_pomp_mixer);
      sKeys.put("layout/item_report_full_0", com.behraz.fastermixer.batch.R.layout.item_report_full);
      sKeys.put("layout/item_transaction_history_0", com.behraz.fastermixer.batch.R.layout.item_transaction_history);
      sKeys.put("layout/item_weather_0", com.behraz.fastermixer.batch.R.layout.item_weather);
      sKeys.put("layout/layout_admin_manage_account_fragment_0", com.behraz.fastermixer.batch.R.layout.layout_admin_manage_account_fragment);
      sKeys.put("layout/layout_batch_fragment_0", com.behraz.fastermixer.batch.R.layout.layout_batch_fragment);
      sKeys.put("layout/layout_contacts_fragment_0", com.behraz.fastermixer.batch.R.layout.layout_contacts_fragment);
      sKeys.put("layout/layout_dashboard_fragment_0", com.behraz.fastermixer.batch.R.layout.layout_dashboard_fragment);
      sKeys.put("layout/layout_dilaog_weather_0", com.behraz.fastermixer.batch.R.layout.layout_dilaog_weather);
      sKeys.put("layout/layout_fragment_requests_0", com.behraz.fastermixer.batch.R.layout.layout_fragment_requests);
      sKeys.put("layout/layout_map_0", com.behraz.fastermixer.batch.R.layout.layout_map);
      sKeys.put("layout/layout_mixer_0", com.behraz.fastermixer.batch.R.layout.layout_mixer);
      sKeys.put("layout/layout_record_dialog_0", com.behraz.fastermixer.batch.R.layout.layout_record_dialog);
      sKeys.put("layout/view_faster_mixer_user_panel_0", com.behraz.fastermixer.batch.R.layout.view_faster_mixer_user_panel);
      sKeys.put("layout/view_item_progress_0", com.behraz.fastermixer.batch.R.layout.view_item_progress);
    }
  }
}
