package com.mysj.processingdata;

public class caculate {
    public static void main(String[] args) {
        double x = 26098900;
        double slc = 15188597.5983791;
        double mlc = 9852893.2151257;
        double totalS = 27244212000.0;
        double ns = 41.04;
        double read = 109886868;
        double write = 67470854;
        //System.out.println((slc-x)/slc);
        //System.out.println((mlc-x)/mlc);
        //System.out.println(slc/x);
        //System.out.println(x/slc);
        System.out.println((read + write)*ns);
        System.out.println("SLC:"+(read*1.55 + write*8.38));
        System.out.println("MLC:"+(read*2.75 + write*18.565));

    }
}