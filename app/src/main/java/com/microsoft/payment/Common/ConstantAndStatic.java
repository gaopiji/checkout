package com.microsoft.payment.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.microsoft.payment.R;
import com.microsoft.payment.SampleApp;
import com.microsoft.payment.bean.PersonBean;
import com.microsoft.payment.bean.Product;
import com.microsoft.payment.bean.ProductBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by v-pigao on 2/28/2018.
 */

public class ConstantAndStatic {


      public static UUID faceId;
      public static final String  PERSON_GROUP_ID = "PERSON_GROUP_ID";

      public static String IP ;
      public static  String HEAD_URL = "http://"+IP+"/";
      public static final String PAYMENT_QUERY_URL = "payment/query";

      public static PersonBean getPersonBean() {
            return mPersonBean;
      }

      public static void setPersonBean(PersonBean mPersonBean) {
            ConstantAndStatic.mPersonBean = mPersonBean;
      }


      public static volatile PersonBean mPersonBean;
      public static Set<Integer> choiceIds = new HashSet<>();
      public static Map<String,Product> productMap = new HashMap<>();

      public static final Product DEFAULT_PRODUCT = new Product("p0","Surface Laptop",700,R.drawable.thumbnail_1);

      public static void initProducts(Context context){

            Product product = new Product();
            product.goodsId = "3cdf42e8-533e-4c32-83f6-40bcff2ac824";
           // product.goodsId = "94d72f86-a53b-4949-bb79-453204ff73d1";
            product.name = "Surface Laptop (Intel Core i5, 4GB RAM, 128GB) - Platinum";
            product.price = 799;
            product.productDra =R.drawable.p01;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "cbfc63c5-2701-4c4f-8a98-19530e203f4d";
            //product.goodsId = "862bd0c0-2cd2-470c-af11-acb95eade658";
            product.name = "Microsoft Hololens";
            product.price = 3000;
            product.productDra = R.drawable.p02;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "93c88a90-9c8c-4c28-99d8-09a30c71daa7";
           // product.goodsId = "e1b08f13-4c31-4c19-8ca4-f090dcbc7d09";
            product.name = "Xbox One X 1TB Limited Edition Console";
            product.price = 498.99;
            product.productDra = R.drawable.p03;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "2d8cec0c-9f8b-4c4f-b382-ad4c9a8b8f7b";
           // product.goodsId = "a4c1d563-8383-49e0-b6da-45246a510c63";
            product.name = "Windows 10 Home (Download)";
            product.price = 199.99;
            product.productDra = R.drawable.p04;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "ed584954-bce9-43bd-8505-3d5f4fcfb1f9";
           // product.goodsId = "c41e125a-8536-4cfd-b351-bc1fdb387365";
            product.name = "Office 365 Home";
            product.price = 99.99;
            product.productDra = R.drawable.p05;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "cd0964e4-e6d5-4baa-bf9e-3fe415108461";
            product.name = "GNC Mega Men Multivitamin 180 Caplets";
            product.price = 29.99;
            product.productDra = R.drawable.p06;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "72d2f665-9577-42af-b46e-59528b461893";
            product.name = "Centrum Silver Adult (125 Count) Multivitamin";
            product.price = 15.77;
            product.productDra = R.drawable.p07;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "6af6f72d-26e4-4428-a6e3-41843dd151c3";
            product.name = "Patek Philippe Aquanaut42mm White Gold Watch Blue Strap";
            product.price = 56999;
            product.productDra = R.drawable.p08;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "9e4964db-4ee7-478b-ac61-2e56bdd1acef";
            product.name = "Garmin Fenix 5S - White with Carrara White Band";
            product.price = 544;
            product.productDra = R.drawable.p09;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "27e3f910-2ab3-4cea-932b-bcbcec1d2531";
            product.name = "JBL Charge 3 Waterproof Portable Bluetooth Speaker (Black)";
            product.price = 119.9;
            product.productDra = R.drawable.p10;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "586317b4-9117-4fb9-b1f4-d24b5013336d";
            product.name = "Casio Keyboards CTK 2400";
            product.price = 169.99;
            product.productDra = R.drawable.p11;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "94be12c2-1d94-4912-b1d5-86f6f31ac732";
            product.name = "Whirlpool 5.3 cu.ft Smart Top Load Washer";
            product.price = 1399;
            product.productDra = R.drawable.p12;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "4c7ba993-4a48-48c6-832d-dd052c122819";
            product.name = "Dyson V8 Absolute Cord-Free Vacuum";
            product.price = 448.49;
            product.productDra = R.drawable.p13;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "582b944f-949e-4818-bb54-e00116586065";
            product.name = "America's Best Designer Sale-get 2 pairs of eyeglasses for $69.95";
            product.price = 69.95;
            product.productDra = R.drawable.p14;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "8821d4b2-30c9-45dc-adfd-ca3904290de9";
            product.name = "Ray-Ban ERIKA RB7046 Eyeglasses";
            product.price = 140;
            product.productDra = R.drawable.p15;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "e30ffd04-6db0-4ea8-b91e-bf34a769b376";
            product.name = "Runway sunglasses Blue AW17 ";
            product.price = 520;
            product.productDra = R.drawable.p16;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "38a0831e-cbf4-4dae-ba7c-c75c538750e0";
            product.name = "Swarovski Fit Choker, White, Palladium Plating";
            product.price = 149;
            product.productDra = R.drawable.p17;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "e0ed524a-5f31-41c1-81f7-909cc57db422";
            product.name = "Estee Lauder Advanced Night Repair Recovery Complex";
            product.price = 77.45;
            product.productDra = R.drawable.p18;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "85f8121f-645b-482b-a26b-b9f19e168700";
            product.name = "La Mar The New Sheer Pressed Powder";
            product.price = 56999;
            product.productDra = R.drawable.p19;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "ebbb7988-5774-450e-a509-d04c82563bb6";
            product.name = "Philips Wet & Dry Electric Shaver";
            product.price = 121.53;
            product.productDra = R.drawable.p20;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "21ddc3e2-df65-40dc-bf1b-bf0fc256939f";
            product.name = "Gillette5 Men’S Razor Handle";
            product.price = 7.99;
            product.productDra = R.drawable.p21;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "59bd12f6-109e-42d0-a77a-3f22fd7db550";
            product.name = "The Legend of Zelda: Breath of the Wild - Nintendo Switch";
            product.price = 44.99;
            product.productDra = R.drawable.p22;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "2e98ece0-6512-41b6-94ea-4d61c711aa06";
            product.name = "World Of Tanks-Xbox 360 English Us Ntsc Dvd";
            product.price = 17;
            product.productDra = R.drawable.p23;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "ba026395-d437-475e-988e-71c7ddf60cf0";
            product.name = "AGV K3 SV Helmet - Liquefy";
            product.price = 356.76;
            product.productDra = R.drawable.p24;
            productMap.put(product.goodsId,product);

            product = new Product();
            product.goodsId = "a85fa20b-a4ab-46b6-8c6b-08a1cf961250";
            product.name = "Nike Air Vapormax Flyknit";
            product.price = 269;
            product.productDra = R.drawable.p25;
            productMap.put(product.goodsId,product);
      }

}
