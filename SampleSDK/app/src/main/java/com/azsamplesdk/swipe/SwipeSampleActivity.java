package com.azsamplesdk.swipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azsamplesdk.R;
import com.azsamplesdk.location.CLocation;
import com.azsdk.location.utils.ErrorModel;
import com.azsdk.location.utils.MyLocationService;
import com.azsdk.location.utils.ResponseModel;
import com.azsdk.swipe.SwipeHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SwipeSampleActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tvData)
    TextView tvData;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    DataListAdapter dataListAdapter;
    ArrayList<ResponseModel> arrayListResponseModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_swipe_sample);
            ButterKnife.bind(this);

            //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            tvData.setText("Please wait...");

            startService(new Intent(SwipeSampleActivity.this, MyLocationService.class));

            //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    startService(new Intent(SwipeSampleActivity.this, MyLocationService.class));

                    tvData.setText("Clear");
                }
            });


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SwipeSampleActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            initSwipe();
            setAdapterData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initSwipe() {
        try {
            int ButtonWidth = 100; // button width
            int ButtonText = 18; // text size


            SwipeHelper swipeHelper = new SwipeHelper(SwipeSampleActivity.this, recyclerView, ButtonWidth, ButtonText) {
                @Override
                public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                    underlayButtons.add(new SwipeHelper.UnderlayButton(
                            "Delete",
                            0,
                            Color.parseColor("#B30000"), // bg color
                            Color.parseColor("#FFFFFF"), // text color
                            new SwipeHelper.UnderlayButtonClickListener() {
                                @Override
                                public void onClick(int pos) {
                                    // TODO: onDelete

                                    if (dataListAdapter != null) {
                                        dataListAdapter.removeItem(pos);
                                    }
                                }
                            }
                    ));

                    underlayButtons.add(new SwipeHelper.UnderlayButton(
                            "Share",
                          0,
                            Color.parseColor("#6E8BAD"), // bg color
                            Color.parseColor("#333333"), // text color
                            new SwipeHelper.UnderlayButtonClickListener() {
                                @Override
                                public void onClick(int pos) {
                                    // TODO: OnTransfer
                                    String uri = "geo:" + arrayListResponseModel.get(pos).getLocationLatLong() + ","
                                            +arrayListResponseModel.get(pos).getLocationLatLong().getLongitude() + "?q=" + arrayListResponseModel.get(pos).getLocationLatLong().getLatitude()
                                            + "," + arrayListResponseModel.get(pos).getLocationLatLong().getLongitude();
                                    startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                                            Uri.parse(uri)));
                                }
                            }
                    ));
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setAdapterData() {
        try {

            if (arrayListResponseModel != null) {
                dataListAdapter = new DataListAdapter(SwipeSampleActivity.this, arrayListResponseModel);
                recyclerView.setAdapter(dataListAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.VersionViewHolder> {
        ArrayList<ResponseModel> mArrListResponseModel;
        Context mContext;


        public DataListAdapter(Context context, ArrayList<ResponseModel> arrayListFollowers) {
            mArrListResponseModel = arrayListFollowers;
            mContext = context;
        }

        @Override
        public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.raw_location_data, viewGroup, false);
            VersionViewHolder viewHolder = new VersionViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final VersionViewHolder versionViewHolder, final int i) {
            try {
                ResponseModel mResponseModel = mArrListResponseModel.get(i);



                versionViewHolder.tvName.setText(
                        "Mac : " + mResponseModel.getMacAdressId() +
                                "\nLat : " + mResponseModel.getLocationLatLong().getLatitude()+
                                "\nLan : " + mResponseModel.getLocationLatLong().getLongitude()
                );



                versionViewHolder.tvName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {

                            ResponseModel mResponseModel = mArrListResponseModel.get(i);
                            Toast.makeText(mContext,
                                    "Latitude : " + mResponseModel.getLocationLatLong().getLatitude()+
                                    "\nLongitude : " + mResponseModel.getLocationLatLong().getLongitude()
                                    , Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mArrListResponseModel.size();
        }


        public void removeItem(int position) {
            try {
                mArrListResponseModel.remove(position);
                notifyItemRemoved(position);
                dataListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        class VersionViewHolder extends RecyclerView.ViewHolder {
            TextView tvName;

            public VersionViewHolder(View itemView) {
                super(itemView);

                tvName = (TextView) itemView.findViewById(R.id.tvName);
            }

        }
    }


    @Override
    public void onStart() {
        try {
            super.onStart();
            EventBus.getDefault().unregister(this);
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        try {
            super.onStop();
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.tvData)
    public void clicktvData(TextView view) {
        view.setText("Clear");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseModel(ResponseModel responseModel) {
        Log.i("==onResponseModel==", "=====Call--1--==");
        if (responseModel != null) {

            arrayListResponseModel.add(responseModel);

            if(dataListAdapter !=null)
            {
                dataListAdapter.notifyDataSetChanged();
            }
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorModel(ErrorModel errorModel) {
        Log.i("==onErrorModel==", "=====Call--2--==");
    }


}
