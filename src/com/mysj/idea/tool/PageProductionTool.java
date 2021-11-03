package com.mysj.idea.tool;

/**
 * 生产页面的工具类：
 *      1、在程序执行前初始化该类，在进行页面分配时，通过该工具类获取对应的页面（SLC/MLC）
 *      2、外部通过相应的页面分配方法进行操作，每次操作都会更新总的统计数据
 *      3、参数：
 *              a.freeSLC 空闲的SLC页面
 *              b.usedSLC 使用中的SLC页面
 *              c.freeMLC 空闲的MLC页面
 *              d.usedMLC 使用中的MLC页面
 *      4、页面的总大小不能大于DRAM的大小
 *          *全为SLC-----128MB、全为MLC------256MB
 *          *或许一开始页面全是freeSLC，共128*1024/4个SLC页面
 */
public class PageProductionTool {

    //初始化的时候，64MB的SLC页面。128MB的MLC，满足(SLC*2 + MLC)<=64
    private int SLC = 16*1024;
    private int MLC = 32*1024;

}
