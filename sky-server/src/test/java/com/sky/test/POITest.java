package com.sky.test;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class POITest {
    /*
    * 通过POI创建Excel文件并且写入文件内容
    * */
    @Test
    public void writeTest() throws IOException {
        //在内存中创建一个Excel文件
        XSSFWorkbook excel = new XSSFWorkbook();
        //在Excel文件中创建一个Sheet页  通过poi创建的excel表格默认没有sheet页
        XSSFSheet sheet = excel.createSheet("info");
        //在Sheet中创建行对象,rownum编号从0开始
        XSSFRow row = sheet.createRow(1); //1代表第2行 下标从0开始的
        row.createCell(1).setCellValue("姓名");//创建单元格写入内容
        row.createCell(2).setCellValue("城市");
        //创建一个新行
        row = sheet.createRow(2);//第3行
        row.createCell(1).setCellValue("张三");//创建单元格写入内容 下标从0开始的
        row.createCell(2).setCellValue("厦门");
 
        row = sheet.createRow(3);//第4行
        row.createCell(1).setCellValue("李四");//创建单元格写入内容
        row.createCell(2).setCellValue("南京");
        //通过输出流将内存中的excel文件写入到磁盘
        FileOutputStream out = new FileOutputStream(new File("D://info.xlsx"));//设置文件
        excel.write(out);//写入到文件
        //关闭资源
        out.close();
        excel.close();
 
    }
    /*
     * 通过POI读取Excel文件中的内容
     * */
    @Test
    public void readTest() throws IOException {
        //读取磁盘上已经存在的excel文件
        FileInputStream fileInputStream = new FileInputStream(new File("D://info.xlsx"));
        XSSFWorkbook excel = new XSSFWorkbook(fileInputStream);
        //读取文件中的第一个sheet页
        XSSFSheet sheet = excel.getSheetAt(0);
        //获取有内容的最后一行的行号 获取的是下标索引
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            //获得某一行
            XSSFRow row = sheet.getRow(i);
            //获得单元格对象
            String cellValue1 = row.getCell(1).getStringCellValue();
            String cellValue2 = row.getCell(2).getStringCellValue();
            System.out.println(cellValue1+" "+cellValue2);
        }
        //关闭资源
        fileInputStream.close();
        excel.close();
    }
}