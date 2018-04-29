package com.microsoft.payment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.microsoft.payment.Common.CircleImageView;
import com.microsoft.payment.Common.ConstantAndStatic;
import com.microsoft.payment.Common.Utils.http.CallBackUtil;
import com.microsoft.payment.bean.PersonBean;
import com.microsoft.payment.bean.Product;

import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;

import static com.microsoft.payment.Common.Utils.http.OkhttpUtil.okHttpGetBitmap;
import static com.microsoft.payment.bean.PersonBean.CUSTOMER;
import static com.microsoft.payment.bean.PersonBean.MANAGER;
import static com.microsoft.payment.bean.PersonBean.VIP;

/**
 * Created by v-pigao on 2/28/2018.
 */

public class RecommendActivity extends Activity {

    public static final String CHOICE_PRODUCT_ID = "CHOICE_PRODUCT_ID";
    public static final String CHOICE_PRODUCT_POS = "CHOICE_PRODUCT_POS";
    public static final String CHOICE_PRODUCT_PRICE = "CHOICE_PRODUCT_PRICE";

    private TextView mhelloTxt;
 //   private TextView mgoodNewsTxt;
    private TextView mRecomendTxt;
    private TextView mExitTxt;
    private CircleImageView mHeadImg;


    private RelativeLayout mRecomendLayout1;
    private ImageView mRecomendImg1;
    private TextView mRecomendName1;
    private TextView mTimes1;
  //  private TextView mRealPrice1;
   // private TextView mOriginalPrice1;


    private RelativeLayout mRecomendLayout2;
    private ImageView mRecomendImg2;
    private TextView mRecomendName2;
    private TextView mTimes2;
  //  private TextView mRealPrice2;
  //  private TextView mOriginalPrice2;


    private RelativeLayout mRecomendLayout3;
    private ImageView mRecomendImg3;
    private TextView mRecomendName3;
    private TextView mTimes3;
 //   private TextView mRealPrice3;
  //  private TextView mOriginalPrice3;



    private TextView mOriginalGiftPrice1;
    private TextView mOriginalGiftPrice2;
    private TextView mOriginalGiftPrice3;
    private TextView mOriginalGiftPrice4;
    private TextView mOriginalGiftPrice5;
    private TextView mOriginalGiftPrice6;
    private TextView mOriginalGiftPrice7;
    private TextView mOriginalGiftPrice8;
    private TextView mOriginalGiftPrice9;
    private TextView mOriginalGiftPrice10;

    private ImageView mToBuyBtn1;
    private ImageView mToBuyBtn2;
    private ImageView mToBuyBtn3;
    private ImageView mToBuyBtn4;
    private ImageView mToBuyBtn5;
    private ImageView mToBuyBtn6;
    private ImageView mToBuyBtn7;
    private ImageView mToBuyBtn8;
    private ImageView mToBuyBtn9;
    private ImageView mToBuyBtn10;

    PersonBean mPersonBean;
    private int mDiscount;
    private double mRealPriceD1;
    private double mRealPriceD2;
    private double mRealPriceD3;

    private Product product1;
    private Product product2;
    private Product product3;
//    private Product product4;
//    private Product product5;
//    private Product product6;

//    private ImageView mRefreshButton1;
//    private ImageView mRefreshButton2;
    private RelativeLayout mHotLayout;

    Iterator<Map.Entry<String, Product>> mRecommendItr;
    int mPersonSize;

    public static final int GO_HOME = 906;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    toHome();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        mPersonBean = ConstantAndStatic.getPersonBean();
        if (mPersonBean == null)
            finish();

        mRecommendItr = ConstantAndStatic.productMap.entrySet().iterator();
        initView();
        initEvent();
    }

    void initView() {
        mhelloTxt = findViewById(R.id.helloTxt);
  //      mgoodNewsTxt = findViewById(R.id.goodNewsTxt);
        mRecomendTxt = findViewById(R.id.recomendTxt);
        mHeadImg = findViewById(R.id.headImg);
        mExitTxt = findViewById(R.id.exitTxt);

        mRecomendLayout1 = findViewById(R.id.recomendLayout1);
        mRecomendImg1 = findViewById(R.id.recomendImg1);
        mRecomendName1 = findViewById(R.id.recomendName1);
        mTimes1 = findViewById(R.id.times1);
      //  mRealPrice1 = findViewById(R.id.realPrice1);
      //  mOriginalPrice1 = findViewById(R.id.originalPrice1);


        mRecomendLayout2 = findViewById(R.id.recomendLayout2);
        mRecomendImg2 = findViewById(R.id.recomendImg2);
        mRecomendName2 = findViewById(R.id.recomendName2);
        mTimes2 = findViewById(R.id.times2);
     //   mRealPrice2 = findViewById(R.id.realPrice2);
      //  mOriginalPrice2 = findViewById(R.id.originalPrice2);


        mRecomendLayout3 = findViewById(R.id.recomendLayout3);
        mRecomendImg3 = findViewById(R.id.recomendImg3);
        mRecomendName3 = findViewById(R.id.recomendName3);
        mTimes3 = findViewById(R.id.times3);
      //  mRealPrice3 = findViewById(R.id.realPrice3);
       // mOriginalPrice3 = findViewById(R.id.originalPrice3);

        mOriginalGiftPrice1 = findViewById(R.id.originalgiftPrice1);
        mOriginalGiftPrice2 = findViewById(R.id.originalgiftPrice2);
        mOriginalGiftPrice3 = findViewById(R.id.originalgiftPrice3);
        mOriginalGiftPrice4 = findViewById(R.id.originalgiftPrice4);
        mOriginalGiftPrice5 = findViewById(R.id.originalgiftPrice5);
        mOriginalGiftPrice6 = findViewById(R.id.originalgiftPrice6);
        mOriginalGiftPrice7 = findViewById(R.id.originalgiftPrice7);
        mOriginalGiftPrice8 = findViewById(R.id.originalgiftPrice8);
        mOriginalGiftPrice9 = findViewById(R.id.originalgiftPrice9);
        mOriginalGiftPrice10 = findViewById(R.id.originalgiftPrice10);

        mToBuyBtn1 = findViewById(R.id.toBuyBtn1);
        mToBuyBtn2 = findViewById(R.id.toBuyBtn2);
        mToBuyBtn3 = findViewById(R.id.toBuyBtn3);
        mToBuyBtn4 = findViewById(R.id.toBuyBtn4);
        mToBuyBtn5 = findViewById(R.id.toBuyBtn5);
        mToBuyBtn6 = findViewById(R.id.toBuyBtn6);
        mToBuyBtn7 = findViewById(R.id.toBuyBtn7);
        mToBuyBtn8 = findViewById(R.id.toBuyBtn8);
        mToBuyBtn9 = findViewById(R.id.toBuyBtn9);
        mToBuyBtn10 = findViewById(R.id.toBuyBtn10);

        mHotLayout = findViewById(R.id.hotLayout);
    }

    void initEvent() {
        String name =  mPersonBean.getName();
        if(name.equals("Bjorn Olstad"))
            name = "Bjørn Olstad";
        mhelloTxt.setText(String.format(getString(R.string.hello_someone),name));
        mExitTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toHome();
            }
        });

        switch (mPersonBean.getPerson_type()) {
            case CUSTOMER:
                mDiscount = 20;
                break;
            case MANAGER:
                mDiscount = 30;
                break;
            case VIP:
                mDiscount = 40;
                break;
            default:
                mDiscount = 20;
                break;
        }

//        mgoodNewsTxt.setText(Html.fromHtml(String.format(getString(R.string.good_news), mDiscount + "%")));

        okHttpGetBitmap("http://"+ConstantAndStatic.IP+mPersonBean.getPhoto_url(), new CallBackUtil.CallBackBitmap() {
            @Override
            public void onFailure(Call call, Exception e) {
                mHeadImg.setImageResource(R.drawable.select_image);
            }

            @Override
            public void onResponse(Bitmap response) {
                mHeadImg.setImageBitmap(response);
            }
        });

        mPersonSize = mPersonBean.getGoodslist().size();

        switch (mPersonSize) {
            case 1:
                mRecomendLayout2.setVisibility(View.GONE);
                mRecomendLayout3.setVisibility(View.GONE);

                product1 = ConstantAndStatic.productMap.get(mPersonBean.getGoodslist().get(0).getGoodsId());
                if (product1 == null)
                    product1 = ConstantAndStatic.DEFAULT_PRODUCT;
                mRecomendImg1.setImageResource(product1.productDra);
                mRecomendName1.setText(product1.name);
                mTimes1.setText(mPersonBean.getGoodslist().get(0).getTimes() + "");
//                mRealPriceD1 = mPersonBean.getGoodslist().get(0).getPrice() * (100 - mDiscount) / 100;
//                mRealPrice1.setText("$ "+String.format("%.2f", mRealPriceD1));
//                mOriginalPrice1.setText("$ "+String.format("%.2f", mPersonBean.getGoodslist().get(0).getPrice()));
//                mOriginalPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                mToBuyBtn1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        toBuy(product1.goodsId,0,String.format("%.2f", mRealPriceD1));
//                    }
//                });

                break;
            case 2:
                mRecomendLayout3.setVisibility(View.GONE);
                product1 = ConstantAndStatic.productMap.get(mPersonBean.getGoodslist().get(0).getGoodsId());
                if (product1 == null)
                    product1 = ConstantAndStatic.DEFAULT_PRODUCT;
                mRecomendImg1.setImageResource(product1.productDra);
                mRecomendName1.setText(product1.name);
                mTimes1.setText(mPersonBean.getGoodslist().get(0).getTimes() + "");
//                mRealPriceD1 = mPersonBean.getGoodslist().get(0).getPrice() * (100 - mDiscount) / 100;
//                mRealPrice1.setText("$ "+String.format("%.2f", mRealPriceD1));
//                mOriginalPrice1.setText("$ "+String.format("%.2f", mPersonBean.getGoodslist().get(0).getPrice()));
//                mOriginalPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                mToBuyBtn1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        toBuy(product1.goodsId,0,String.format("%.2f", mRealPriceD1));
//                    }
//                });

                product2 = ConstantAndStatic.productMap.get(mPersonBean.getGoodslist().get(1).getGoodsId());
                if (product2 == null)
                    product2 = ConstantAndStatic.DEFAULT_PRODUCT;
                mRecomendImg2.setImageResource(product2.productDra);
                mRecomendName2.setText(product2.name);
                mTimes2.setText(mPersonBean.getGoodslist().get(1).getTimes() + "");
//                mRealPriceD2 = mPersonBean.getGoodslist().get(1).getPrice() * (100 - mDiscount) / 100;
//                mRealPrice2.setText("$ "+String.format("%.2f", mRealPriceD2));
//                mOriginalPrice2.setText("$ "+String.format("%.2f", mPersonBean.getGoodslist().get(1).getPrice()));
//                mOriginalPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                mToBuyBtn2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        toBuy(product2.goodsId,1,String.format("%.2f", mRealPriceD2));
//                    }
//                });
                break;

            case 0:
                mRecomendTxt.setText(R.string.no_recommend_new);
                mRecomendLayout1.setVisibility(View.GONE);
                mRecomendLayout2.setVisibility(View.GONE);
                mRecomendLayout3.setVisibility(View.GONE);
                mHotLayout.setVisibility(View.GONE);
                break;
            default:
                product1 = ConstantAndStatic.productMap.get(mPersonBean.getGoodslist().get(0).getGoodsId());
                if (product1 == null)
                    product1 = ConstantAndStatic.DEFAULT_PRODUCT;
                mRecomendImg1.setImageResource(product1.productDra);
                mRecomendName1.setText(product1.name);
                mTimes1.setText(mPersonBean.getGoodslist().get(0).getTimes() + "");
//                mRealPriceD1 = mPersonBean.getGoodslist().get(0).getPrice() * (100 - mDiscount) / 100;
//                mRealPrice1.setText("$ "+String.format("%.2f", mRealPriceD1));
//                mOriginalPrice1.setText("$ "+String.format("%.2f", mPersonBean.getGoodslist().get(0).getPrice()));
//                mOriginalPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                mToBuyBtn1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        toBuy(product1.goodsId,0,String.format("%.2f", mRealPriceD1));
//                    }
//                });

                product2 = ConstantAndStatic.productMap.get(mPersonBean.getGoodslist().get(1).getGoodsId());
                if (product2 == null)
                    product2 = ConstantAndStatic.DEFAULT_PRODUCT;
                mRecomendImg2.setImageResource(product2.productDra);
                mRecomendName2.setText(product2.name);
                mTimes2.setText(mPersonBean.getGoodslist().get(1).getTimes() + "");
//                mRealPriceD2 = mPersonBean.getGoodslist().get(1).getPrice() * (100 - mDiscount) / 100;
//                mRealPrice2.setText("$ "+String.format("%.2f", mRealPriceD2));
//                mOriginalPrice2.setText("$ "+String.format("%.2f", mPersonBean.getGoodslist().get(1).getPrice()));
//                mOriginalPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                mToBuyBtn2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        toBuy(product2.goodsId,1,String.format("%.2f", mRealPriceD2));
//                    }
//                });

                product3 = ConstantAndStatic.productMap.get(mPersonBean.getGoodslist().get(2).getGoodsId());
                if (product3 == null)
                    product3 = ConstantAndStatic.DEFAULT_PRODUCT;
                mRecomendImg3.setImageResource(product3.productDra);
                mRecomendName3.setText(product3.name);
                mTimes3.setText(mPersonBean.getGoodslist().get(2).getTimes() + "");
//                mRealPriceD3 = mPersonBean.getGoodslist().get(2).getPrice() * (100 - mDiscount) / 100;
//                mRealPrice3.setText("$ "+String.format("%.2f", mRealPriceD3));
//                mOriginalPrice3.setText("$ "+String.format("%.2f", mPersonBean.getGoodslist().get(2).getPrice()));
//                mOriginalPrice3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                mToBuyBtn3.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        toBuy(product3.goodsId,2,String.format("%.2f", mRealPriceD3));
//                    }
//                });
                break;

        }

        mOriginalGiftPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOriginalGiftPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOriginalGiftPrice3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOriginalGiftPrice4.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOriginalGiftPrice5.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOriginalGiftPrice6.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOriginalGiftPrice7.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOriginalGiftPrice8.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOriginalGiftPrice9.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mOriginalGiftPrice10.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        mToBuyBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBuy(1);
            }
        });
        mToBuyBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBuy(2);
            }
        });
        mToBuyBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBuy(3);
            }
        });
        mToBuyBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBuy(4);
            }
        });
        mToBuyBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBuy(5);
            }
        });
        mToBuyBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBuy(6);
            }
        });
        mToBuyBtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBuy(7);
            }
        });
        mToBuyBtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBuy(8);
            }
        });
        mToBuyBtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBuy(9);
            }
        });
        mToBuyBtn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBuy(10);
            }
        });

//        set3HotProduct();

//        mRefreshButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                set3HotProduct();
//            }
//        });
//        mRefreshButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                set3HotProduct();
//            }
//        });
    }

//    void set3HotProduct() {
//        product4 = choiceProduct();
//        mRecomendImg4.setImageResource(product4.productDra);
//        mRecomendName4.setText(product4.name);
//        mRealPrice4.setText("$ "+String.format("%.2f", product4.price));
//        mToBuyBtn4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toBuy(product4.goodsId,-1,String.format("%.2f", product4.price));
//            }
//        });
//
//        product5 = choiceProduct();
//        mRecomendImg5.setImageResource(product5.productDra);
//        mRecomendName5.setText(product5.name);
//        mRealPrice5.setText("$ "+String.format("%.2f", product5.price));
//        mToBuyBtn5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toBuy(product5.goodsId,-1,String.format("%.2f",  product5.price));
//            }
//        });
//
//        product6 = choiceProduct();
//        mRecomendImg6.setImageResource(product6.productDra);
//        mRecomendName6.setText(product6.name);
//        mRealPrice6.setText("$ "+String.format("%.2f", product6.price));
//        mToBuyBtn6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toBuy(product6.goodsId,-1,String.format("%.2f", product6.price));
//            }
//        });
//    }

//    Product choiceProduct() {
//        if (!mRecommendItr.hasNext()) {
//            mRecommendItr = ConstantAndStatic.productMap.entrySet().iterator();
//        }
//        while (mRecommendItr.hasNext()) {
//            Product temp = mRecommendItr.next().getValue();
//            if (canChoice(temp)) {
//                return temp;
//            }
//        }
//
//        if (!mRecommendItr.hasNext()) {
//            mRecommendItr = ConstantAndStatic.productMap.entrySet().iterator();
//            while (mRecommendItr.hasNext()) {
//                Product temp = mRecommendItr.next().getValue();
//                if (canChoice(temp)) {
//                    return temp;
//                }
//            }
//        }
//        return ConstantAndStatic.DEFAULT_PRODUCT;
//    }

//    boolean canChoice(Product product) {
//        switch (mPersonSize) {
//            case 0:
//                return true;
//            case 1:
//                return !product1.goodsId.equals(product.goodsId);
//            case 2:
//                return !product1.goodsId.equals(product.goodsId) && !product2.goodsId.equals(product.goodsId);
//            default:
//                return !product1.goodsId.equals(product.goodsId)
//                        && !product2.goodsId.equals(product.goodsId)
//                        && !product3.goodsId.equals(product.goodsId);
//
//        }
//    }



    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(GO_HOME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConstantAndStatic.choiceIds.clear();
        mHandler.sendEmptyMessageDelayed(GO_HOME,100*10000); // Return to home page after 100 seconds
    }

//    void toBuy(String id,int pos,String price) {
//        Intent intent = new Intent(RecommendActivity.this, PaySuccessActivity.class);
//        intent.putExtra(CHOICE_PRODUCT_ID,id);
//        intent.putExtra(CHOICE_PRODUCT_POS,pos);
//        intent.putExtra(CHOICE_PRODUCT_PRICE,price);
//        RecommendActivity.this.startActivity(intent);
//    }

    void toBuy(int pos) {
        Intent intent = new Intent(RecommendActivity.this, PaySuccessActivity.class);
        intent.putExtra(CHOICE_PRODUCT_POS,pos);
        RecommendActivity.this.startActivity(intent);
    }

    void toHome() {
        Intent intent = new Intent(RecommendActivity.this, MainActivity.class);
        RecommendActivity.this.startActivity(intent);
    }
}
