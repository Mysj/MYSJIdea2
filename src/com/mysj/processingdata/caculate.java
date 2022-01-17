package com.mysj.processingdata;

public class caculate {
    public static void main(String[] args) {

        double ReadSLC = 4.9291618E7;
        double WriteSLC = 1.7505137E7;
        double ReadMLC = 0;
        double WriteMLC = 0;
        double countSLCtoMLC = 0;
        double countMLCtoSLC = 0;
        double myLru = ReadSLC*0.216 + WriteSLC*0.839 + ReadMLC*0.325 + WriteMLC*1.672 +
                countSLCtoMLC*(0.216 + 1.672)*4*1024*8 + countMLCtoSLC*(0.325 + 0.839)*4*1024*8;

        double WriteNumber = 17505137;
        double ReadNumber = 49291618;
        double RemovePageSize = 0;
        double cost = 0.1663E7;
        double LruSlc = WriteNumber*0.839 + ReadNumber*0.216 + RemovePageSize*1024*8*4*0.839 + cost ;
        double LruMlc = WriteNumber*1.672 + ReadNumber*0.325 + RemovePageSize*1024*8*4*0.839 + cost;//   + 2*cost
        System.out.println(myLru);
        System.out.println(LruSlc);
        System.out.println(LruMlc);
        System.out.println((LruSlc-myLru)/LruSlc);
        System.out.println((LruMlc-myLru)/LruMlc);


        double x = 116771254.091999;
        double slc = 127865604.491999;
        double mlc = 2.78841802675E8;
        double totalS = 27244212000.0;
        double ns = 41.04;
        double read = 34883015;
        double write = 7892582;

        //System.out.println((slc-x)/slc);
        //System.out.println(x/slc);
        //System.out.println((read + write)*ns);
        //System.out.println(x*ns);
        //System.out.println("SLC:"+(read*1.55 + write*8.38));
        //System.out.println("MLC:"+(read*2.75 + write*18.565));

    }
}
