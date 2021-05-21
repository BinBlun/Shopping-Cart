package com.ehanu.mycart.database;

public class DbSchema {
    public final class ProductTable{
        public static final String NAME = "cart_table";

        public final class Cols{
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String IMG_URL = "img_url";
            public static final String PRICE = "price";
            public static final String QUANTITY = "QUANTITY";
        }
    }
}
