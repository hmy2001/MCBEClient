package jp.dip.hmy2001.mcbeClient.network.mcbe.json;

public class JwtHeader {

    public String alg;
    public String x5u;

    public JwtHeader(String alg, String x5u){
        this.alg = alg;
        this.x5u = x5u;
    }


}
