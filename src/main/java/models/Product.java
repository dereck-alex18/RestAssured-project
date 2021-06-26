package models;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.json.simple.JSONObject;

public class Product {
    private String name;
    private String price;
    private String descricao;
    private String quantidade;
    private String productId;

    public Product(String name, String price, String descricao, String quantidade){
        this.name = name;
        this.price = price;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public void setProductId(String id){
        this.productId = id;
    }

    public String getIdProduct(){
        return this.productId;
    }

    public String getProductInformation(){
        JSONObject productJsonRepresentation = new JSONObject();

        productJsonRepresentation.put("nome", this.name);
        productJsonRepresentation.put("preco", this.price);
        productJsonRepresentation.put("descricao", this.descricao);
        productJsonRepresentation.put("quantidade", this.price);

        return productJsonRepresentation.toJSONString();
    }
}
