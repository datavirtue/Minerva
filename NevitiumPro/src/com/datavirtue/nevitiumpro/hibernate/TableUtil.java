/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.hibernate;

import com.datavirtue.nevitiumpro.Util.Tools;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Administrator
 */
public class TableUtil {

    public static void removeAllCols(JTable jt, ArrayList<String> keeperCols) throws Exception {
        String cname;
        TableColumn tc;
        TableColumnModel cm = jt.getColumnModel();
        for (int x = 0; x < jt.getColumnCount(); x++) {
            cname = jt.getColumnName(x).trim();
            if (!Tools.arrayListContains(keeperCols, cname)) {
                tc = cm.getColumn(x);
                jt.removeColumn(tc);
                x--;
            }
        }
    }

    public static void orderColumns(JTable jt, ArrayList<String> colList) {
        String colName;
        String colTemp;
        for (int c = 0; c < colList.size(); c++) {
            colName = colList.get(c);
            for (int tc = 0; tc < jt.getColumnCount(); tc++) {
                colTemp = jt.getColumnName(tc);
                if (colTemp.equals(colName)) {
                    jt.moveColumn(tc, c);
                }
            }
        }

    }

    public static void sizeColumns(JTable jt, ArrayList<Integer> sizes) {
        int tableWidth, colWidth;
        tableWidth = jt.getWidth();
        for (int c = 0; c < jt.getColumnCount(); c++) {
            colWidth = (int) (((sizes.get(c)) * .01) * tableWidth);
            jt.getColumn(jt.getColumnName(c)).setPreferredWidth(colWidth);
        }
    }
}
