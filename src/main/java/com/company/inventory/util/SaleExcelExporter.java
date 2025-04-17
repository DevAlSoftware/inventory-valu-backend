package com.company.inventory.util;

import com.company.inventory.sale.model.Sale;
import com.company.inventory.saleDetail.model.SaleDetail;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class SaleExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Sale> sales;

    public SaleExcelExporter(List<Sale> sales) {
        this.sales = sales;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Ventas");

        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);

        createCell(row, 0, "Venta ID", style);
        createCell(row, 1, "Cliente", style);
        createCell(row, 2, "Fecha", style);
        createCell(row, 3, "Subtotal", style);
        createCell(row, 4, "Ganancia", style);
        createCell(row, 5, "Producto", style);
        createCell(row, 6, "Cantidad", style);
        createCell(row, 7, "Precio", style);
        createCell(row, 8, "% Ganancia", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }

        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        font.setFontHeight(12);
        style.setFont(font);

        for (Sale sale : sales) {
            double subtotal = 0;
            double total = 0;

            for (SaleDetail detail : sale.getSaleDetails()) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;

                double precio = detail.getProduct().getPrice();
                int cantidad = detail.getQuantity();
                double porcentaje = detail.getProfitPercentage();

                double subtotalDetalle = precio * cantidad;
                double totalDetalle = subtotalDetalle * (1 + porcentaje / 100);

                subtotal += subtotalDetalle;
                total += totalDetalle;

                createCell(row, columnCount++, sale.getId(), style);
                createCell(row, columnCount++, sale.getCustomer().getFullName(), style);
                createCell(row, columnCount++, sale.getSaleDate().toString(), style);
                createCell(row, columnCount++, subtotal, style);
                createCell(row, columnCount++, total - subtotal, style); // ganancia

                createCell(row, columnCount++, detail.getProduct().getName(), style);
                createCell(row, columnCount++, cantidad, style);
                createCell(row, columnCount++, precio, style);
                createCell(row, columnCount++, porcentaje, style);
            }
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
