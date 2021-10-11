package models;

import org.json.simple.JSONObject;

public class Product {
    public String name;
    public int price;
    public String descricao;
    public int quantidade;
    public String productId;

    public Product(String name, int price, String descricao, int quantidade) {
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

    public String getProductInformation() {
        JSONObject productJsonRepresentation = new JSONObject();

        productJsonRepresentation.put("nome", this.name);
        productJsonRepresentation.put("preco", this.price);
        productJsonRepresentation.put("descricao", this.descricao);
        productJsonRepresentation.put("quantidade", this.quantidade);

        return productJsonRepresentation.toJSONString();
    }
}
