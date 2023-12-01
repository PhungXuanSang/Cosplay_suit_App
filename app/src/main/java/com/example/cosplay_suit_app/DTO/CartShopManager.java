package com.example.cosplay_suit_app.DTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CartShopManager {
    private static CartShopManager instance;
    private Map<String, Set<String>> shopCartMap;
    private Set<String> listidshop;

    private CartShopManager() {
        // Private constructor to prevent instantiation
        shopCartMap = new HashMap<>();
        listidshop = new HashSet<>();
    }

    public static CartShopManager getInstance() {
        if (instance == null) {
            instance = new CartShopManager();
        }
        return instance;
    }

    public void addCartToShop(String idshop, String idcart) {
        listidshop.add(idshop);

        if (!shopCartMap.containsKey(idshop)) {
            shopCartMap.put(idshop, new HashSet<>());
        }
        shopCartMap.get(idshop).add(idcart);
    }

    public void removeCartFromShop(String idshop, String idcart) {
        if (shopCartMap.containsKey(idshop)) {
            shopCartMap.get(idshop).remove(idcart);
            if (shopCartMap.get(idshop).isEmpty()) {
                shopCartMap.remove(idshop);
                listidshop.remove(idshop);
            }
        }
    }

    public boolean isCartBelongsToShop(String idshop, String idcart) {
        return shopCartMap.containsKey(idshop) && shopCartMap.get(idshop).contains(idcart);
    }

    public Set<String> getListidshop() {
        return listidshop;
    }

    public Set<String> getCartListForShop(String idshop) {
        return shopCartMap.getOrDefault(idshop, new HashSet<>());
    }
    public void clearData() {
        shopCartMap.clear();
        listidshop.clear();
    }
}
