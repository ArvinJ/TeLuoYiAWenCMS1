package com.lgq.demo.common;

import com.lgq.demo.constant.DeviceType;
import com.lgq.demo.model.Courseware;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by liguoqing on 2016/1/18.
 */
public class CoursewareExcelView extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Object> datas = (List<Object>) model.get("datas");
        String[] keys = (String[])model.get("keys");
        String[] titles = (String[])model.get("titles");
        String fileName = (String)model.get("fileName");
        HSSFSheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(12);

        HSSFRow sheetRow = sheet.createRow(0);
        for (int i = 0; i <titles.length; i++) {
            HSSFCell cell = sheetRow.createCell(i);
            cell.setCellStyle(genHeaderStyle(workbook));
            HSSFRichTextString text = new HSSFRichTextString(titles[i]);
            cell.setCellValue(text);
            cell.setCellValue(titles[i]);
        }

        for (int i = 0; i < datas.size(); i++) {
            sheetRow = sheet.createRow(i+1);  //创建行
            Object obj = datas.get(i);
            Courseware courseware = (Courseware) obj;
            for (int j = 0; j < keys.length; j++) {
                if(keys[j].equals("pcDeviceType")) {
                    String deviceType = courseware.getDeviceType();
                    if(deviceType == null) {
                        sheetRow.createCell(j).setCellValue("");
                    } else {
                        if(deviceType.contains(DeviceType.PC.getName())) {
                            sheetRow.createCell(j).setCellValue("可");
                        } else {
                            sheetRow.createCell(j).setCellValue("不可");
                        }
                    }
                } else if(keys[j].equals("mobileDeviceType")) {
                    String deviceType = courseware.getDeviceType();
                    if (deviceType == null) {
                        sheetRow.createCell(j).setCellValue("");
                    } else {
                        if (deviceType.contains(DeviceType.MOBILE.getName())) {
                            sheetRow.createCell(j).setCellValue("可");
                        } else {
                            sheetRow.createCell(j).setCellValue("不可");
                        }
                    }
                } else if(keys[j].equals("isOpen")) {
                    boolean isOpened = courseware.isOpen();
                    if (isOpened) {
                        sheetRow.createCell(j).setCellValue("是");
                    } else {
                        sheetRow.createCell(j).setCellValue("否");
                    }
                } else if(keys[j].equals("transformStatus")) {
                    String status = courseware.getTransformStatus();
                    if(status.equals("1")) {
                        sheetRow.createCell(j).setCellValue("转换成功");
                    } else if(status.equals("-1")) {
                        sheetRow.createCell(j).setCellValue("转换失败");
                    } else {
                        sheetRow.createCell(j).setCellValue("转换中");
                    }
                } else {
                    Object value = getFieldValueByName(keys[j], obj);
                    if (value == null) {
                        value = "";
                    }
                    sheetRow.createCell(j).setCellValue(value.toString());
                }
            }
        }

        //设置下载时客户端Excel的名称ISO-8859-1
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename="+(request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0 ? new String(fileName.getBytes("utf-8"), "iso8859-1") : URLEncoder.encode(fileName, "UTF-8")) + ".xls");// 设定输出文件头
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }


    public HSSFCellStyle genHeaderStyle(HSSFWorkbook workbook) {
        // 生成一个样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        // 设置header样式
//        headerStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成header字体
        HSSFFont headerFont = workbook.createFont();
        headerFont.setColor(HSSFColor.VIOLET.index);
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        headerStyle.setFont(headerFont);

        return headerStyle;
    }

    /**
     *  通过对象的getXXX()方法的值（若对象中有boolean类型，isXXX，该方法存在问题）
     * @param fieldName
     * @param obj
     * @return
     */
    private Object getFieldValueByName(String fieldName, Object obj) {
        String firstLetter = fieldName.substring(0,1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);

        try {
            Method method = obj.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(obj, new Object[]{});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("属性不存在！");
        }
        return null;
    }

}
