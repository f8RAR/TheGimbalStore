package com.avnet.gears.codes.gimbal.store.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.avnet.gears.codes.gimbal.store.R;
import com.avnet.gears.codes.gimbal.store.async.response.processor.impl.ProductItemProcessor;
import com.avnet.gears.codes.gimbal.store.constant.GimbalStoreConstants;
import com.avnet.gears.codes.gimbal.store.handler.HttpConnectionAsyncTask;
import com.avnet.gears.codes.gimbal.store.utils.AndroidUtil;
import com.avnet.gears.codes.gimbal.store.utils.ServerURLUtil;

import java.util.Arrays;
import java.util.Map;

public class ProductDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String selectedProductId = "";
        Bundle intentBundle = getIntent().getExtras();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_product_details);
        if (intentBundle != null) {
            selectedProductId = intentBundle.getString(GimbalStoreConstants.INTENT_EXTRA_ATTR_KEY.SELECTED_PRODUCT_ID.toString(), "");
        }
        Log.d("DEBUG", "Displaying details of product : " + selectedProductId);
        // Make async calls
        if (!"".equals(selectedProductId)) {
            ProgressDialog dialog = AndroidUtil.showProgressDialog(this,
                    GimbalStoreConstants.DEFAULT_SPINNER_TITLE,
                    GimbalStoreConstants.DEFAULT_SPINNER_INFO_TEXT);
            ImageView productImage = (ImageView) findViewById(R.id.product_display_image);
            TextView dummyTextView = (TextView) findViewById(R.id.product_title);
            ProductItemProcessor productItemProcessor = new ProductItemProcessor(this, dialog,
                    productImage, dummyTextView);
            Map<String, String> paramsMap = ServerURLUtil.getBasicConfigParamsMap(getResources());
            paramsMap.put(GimbalStoreConstants.StoreParameterKeys.identifier.toString(),
                    GimbalStoreConstants.StoreParameterValues.top.toString());
            paramsMap.put(GimbalStoreConstants.StoreParameterKeys.type.toString(),
                    GimbalStoreConstants.StoreParameterValues.category.toString());
            paramsMap.put(GimbalStoreConstants.StoreParameterKeys.uniqueId.toString(),
                    selectedProductId);
            String cookieString = AndroidUtil.getPreferenceString(getApplicationContext(), GimbalStoreConstants.PREF_SESSION_COOKIE_PARAM_KEY);

            HttpConnectionAsyncTask asyncTask = new HttpConnectionAsyncTask(GimbalStoreConstants.HTTP_METHODS.GET,
                    Arrays.asList(new String[]{ServerURLUtil.getStoreServletServerURL(getResources())}),
                    Arrays.asList(paramsMap), cookieString,
                    productItemProcessor);
            asyncTask.execute(new String[]{});
        }
        // get product details to display
        // TODO mock and display data Aravindan

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        boolean result = AndroidUtil.processSettingsAction(this, id);

        //noinspection SimplifiableIfStatement
        if (!result) {
            switch (id) {
                case R.id.action_settings:
                    return true;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
